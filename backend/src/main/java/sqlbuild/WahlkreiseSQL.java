package sqlbuild;

import static sqlbuild.Replace.getErststimmenTable;
import static sqlbuild.Replace.getZweitstimmenTable;

public class WahlkreiseSQL {

    public static String getWahlkreisQuery(int jahr) {
        return "SELECT id, nummer, name, bundesland FROM wahlkreise " +
                "WHERE wahljahr = " + jahr + ";";
    }

    public static String getWkWahlbeteiligungQuery(int jahr, int wkid, String modus) {
        return "WITH waehler_pro_wahlkreis_erststimmen AS ( " +
                "    SELECT " +
                "      ee.wahlkreis_id, " +
                "      sum(ee.anzahl) as summe_erststimmen, " +
                "      wahljahr " +
                "    FROM " + getErststimmenTable(modus) + " ee,  wahlkreise wk " +
                "    WHERE wk.id = ee.wahlkreis_id " +
                "    GROUP BY ee.wahlkreis_id, wahljahr " +
                "), " +
                " " +
                "    waehler_pro_wahlkreis_zweitstimmen AS ( " +
                "      SELECT " +
                "        ze.wahlkreis_id, " +
                "        sum(ze.anzahl) as summe_zweitstimmen, " +
                "        wahljahr " +
                "      FROM " + getZweitstimmenTable(modus) + " ze,  wahlkreise wk " +
                "      WHERE wk.id = ze.wahlkreis_id " +
                "      GROUP BY ze.wahlkreis_id, wahljahr " +
                "  ) " +
                " " +
                "SELECT " +
                "  wk.id, " +
                "  wk.nummer, " +
                "  wk.name, " +
                "  wk.bundesland, " +
                "  greatest(werst.summe_erststimmen, wzweit.summe_zweitstimmen) as anzahl_waehler, " +
                "  wk.anzahl_wahlberechtigte " +
                "FROM wahlkreise wk, waehler_pro_wahlkreis_erststimmen werst, " +
                "  waehler_pro_wahlkreis_zweitstimmen wzweit " +
                "WHERE wk.id = werst.wahlkreis_id " +
                "      AND wk.id = wzweit.wahlkreis_id " +
                "      AND wk.id = " + wkid + " " +
                "      AND wk.wahljahr = " + jahr +
                ";";
    }

    public static String getWkDirektmandatQuery(int jahr, int wkid, String modus) {
        return "WITH maximaleStimmenWahlkreis (wahljahr, wahlkreis_id, maximal) AS (  " +
                "    SELECT  " +
                "      w.wahljahr,  " +
                "      w.id,  " +
                "      max(e.anzahl)  " +
                "    FROM " + getErststimmenTable(modus) + " e, wahlkreise w  " +
                "    WHERE e.wahlkreis_id = w.id  " +
                "    GROUP BY w.wahljahr, w.id  " +
                ")  " +
                "  " +
                "SELECT  " +
                "  k.titel,  " +
                "  k.name,  " +
                "  k.vorname,  " +
                "  k.namenszusatz,  " +
                "  k.geburtsjahr,  " +
                "  p.kuerzel,  " +
                "  p.name  " +
                "FROM " + getErststimmenTable(modus) + " e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m, parteien p  " +
                "WHERE e.kandidaten_id = k.id  " +
                "      AND e.wahlkreis_id = w.id  " +
                "      AND m.maximal = e.anzahl  " +
                "      AND m.wahlkreis_id = w.id  " +
                "      AND k.wahljahr = m.wahljahr  " +
                "      AND w.wahljahr = m.wahljahr  " +
                "      AND k.wahlkreis_id = e.wahlkreis_id  " +
                "      AND k.partei_id = p.id " +
                "      AND w.id = " + wkid + " " +
                "      AND k.wahljahr = " + jahr +
                ";";
    }

}
