package sqlbuild;

import static sqlbuild.Replace.getErststimmenTable;
import static sqlbuild.Replace.getZweitstimmenTable;

public class WahlkreiseSQL {

    public static String getWahlkreisQuery(int jahr) {
        return "SELECT   " +
                "  wk.id,   " +
                "  wk.nummer,   " +
                "  wk.name,   " +
                "  wk.bundesland,   " +
                "  bl.name   " +
                "FROM wahlkreise wk, bundeslaender bl   " +
                "WHERE wk.bundesland = bl.kuerzel   " +
                "AND wahljahr = " + jahr + " " +
                "ORDER BY wk.nummer" +
                ";";
    }

    public static String getWkWahlbeteiligungQuery(int jahr, int wknr, String modus) {
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
                "  greatest(werst.summe_erststimmen, wzweit.summe_zweitstimmen) as anzahl_waehler, " +
                "  wk.anzahl_wahlberechtigte " +
                "FROM wahlkreise wk, waehler_pro_wahlkreis_erststimmen werst, " +
                "  waehler_pro_wahlkreis_zweitstimmen wzweit " +
                "WHERE wk.id = werst.wahlkreis_id " +
                "      AND wk.id = wzweit.wahlkreis_id " +
                "      AND wk.nummer = " + wknr + " " +
                "      AND wk.wahljahr = " + jahr +
                ";";
    }

    public static String getWkDirektmandatQuery(int jahr, int wknr, String modus) {
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
                "  p.name,  " +
                "  p.farbe  " +
                "FROM " + getErststimmenTable(modus) + " e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m, parteien p  " +
                "WHERE e.kandidaten_id = k.id  " +
                "      AND e.wahlkreis_id = w.id  " +
                "      AND m.maximal = e.anzahl  " +
                "      AND m.wahlkreis_id = w.id  " +
                "      AND k.wahljahr = m.wahljahr  " +
                "      AND w.wahljahr = m.wahljahr  " +
                "      AND k.wahlkreis_id = e.wahlkreis_id  " +
                "      AND k.partei_id = p.id " +
                "      AND w.nummer = " + wknr + " " +
                "      AND k.wahljahr = " + jahr +
                ";";
    }

    public static String getWkStimmenProParteiQuery(int jahr, int wknr, String modus) {
        return "WITH stimmen_gesamt AS ( " +
                "    SELECT " +
                "      sum(zweit.anzahl) + sum(erst.anzahl) AS gesamtstimmen, " +
                "      erst.wahlkreis_id, " +
                "      k.wahljahr " +
                "    FROM " + getErststimmenTable(modus) + " erst, " + getZweitstimmenTable(modus) + " zweit, parteien p, kandidaten k " +
                "    WHERE zweit.partei_id = p.id " +
                "          AND erst.wahlkreis_id = zweit.wahlkreis_id " +
                "          AND k.id = erst.kandidaten_id " +
                "          AND k.partei_id = p.id " +
                "          AND zweit.partei_id IS NOT NULL " +
                "          AND erst.kandidaten_id IS NOT NULL " +
                "    GROUP BY erst.wahlkreis_id, k.wahljahr " +
                ") " +
                " " +
                "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe, " +
                "  erst.anzahl + zweit.anzahl AS anzahl_absolut, " +
                "  CAST(erst.anzahl + zweit.anzahl AS NUMERIC) / " +
                "  (CAST(( " +
                "          SELECT gesamtstimmen " +
                "          FROM stimmen_gesamt sg " +
                "          WHERE sg.wahljahr = p.wahljahr " +
                "                AND sg.wahlkreis_id = erst.wahlkreis_id " +
                "        ) AS NUMERIC))       AS anzahl_relativ " +
                "FROM " + getErststimmenTable(modus) + " erst, " + getZweitstimmenTable(modus) + " zweit, " +
                "  parteien p, kandidaten k, wahlkreise wk " +
                "WHERE erst.wahlkreis_id = zweit.wahlkreis_id " +
                "      AND erst.kandidaten_id = k.id " +
                "      AND zweit.partei_id = p.id " +
                "      AND k.partei_id = p.id " +
                "      AND zweit.partei_id IS NOT NULL " +
                "      AND erst.kandidaten_id IS NOT NULL " +
                "      AND k.wahljahr = " + jahr + " " +
                "      AND wk.id = erst.wahlkreis_id " +
                "      AND wk.nummer = " + wknr +
                ";"
                ;
    }

    public static String getWkVergleichVorjahrQuery(int wknr, String modus) {
        return "WITH stimmen_partei AS ( " +
                "    SELECT zweit.anzahl + erst.anzahl as gesamtstimmen, " +
                "      erst.wahlkreis_id, " +
                "      p.id as partei_id, " +
                "      k.wahljahr " +
                "    FROM " + getZweitstimmenTable(modus) + " zweit, " + getErststimmenTable(modus) + " erst, " +
                "      parteien p, kandidaten k " +
                "    WHERE zweit.wahlkreis_id = erst.wahlkreis_id " +
                "          AND zweit.partei_id = p.id " +
                "          AND erst.kandidaten_id = k.id " +
                "          AND k.partei_id = p.id " +
                "          AND zweit.partei_id IS NOT NULL " +
                "          AND erst.kandidaten_id IS NOT NULL " +
                ") " +
                " " +
                "SELECT " +
                "  p1.kuerzel, " +
                "  p1.name, " +
                "  p1.farbe, " +
                "  sp1.gesamtstimmen as stimmen_2013, " +
                "  sp2.gesamtstimmen as stimmen_2017 " +
                "FROM stimmen_partei sp1, stimmen_partei sp2, parteien p1, parteien p2, " +
                "  wahlkreise w1, wahlkreise w2 " +
                "WHERE sp1.wahlkreis_id = w1.id " +
                "  AND sp2.wahlkreis_id = w2.id " +
                "  AND w1.nummer = w2.nummer " +
                "AND sp1.partei_id = p1.id " +
                "AND sp2.partei_id = p2.id " +
                "AND p1.kuerzel = p2.kuerzel " +
                "AND sp1.wahljahr = 2013 " +
                "AND sp2.wahljahr = 2017 " +
                "AND w1.nummer = " + wknr +
                ";"
                ;
    }

}
