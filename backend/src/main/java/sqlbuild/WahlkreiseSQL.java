package sqlbuild;

import static sqlbuild.Replace.getErststimmenTable;
import static sqlbuild.Replace.getZweitstimmenTable;

public class WahlkreiseSQL {

    public static String getWahlkreisQuery(int jahr, Integer wknr) {
        return "SELECT   " +
                "  wk.id,   " +
                "  wk.nummer,   " +
                "  wk.name,   " +
                "  wk.bundesland,   " +
                "  bl.name   " +
                "FROM wahlkreise wk, bundeslaender bl   " +
                "WHERE wk.bundesland = bl.kuerzel   " +
                "AND wahljahr = " + jahr + " " +
                (wknr != null ? "AND wk.nummer = " + wknr : "") +
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

    /*
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
    */

    public static String getWkErststimmenProParteiQuery(int jahr, int wknr, String modus) {
        return "WITH stimmen_gesamt AS ( " +
                "    SELECT " +
                "      sum(erst.anzahl) AS gesamtstimmen, " +
                "      erst.wahlkreis_id, " +
                "      k.wahljahr " +
                "    FROM " + getErststimmenTable(modus) + " erst, kandidaten k " +
                "    WHERE " +
                "          k.id = erst.kandidaten_id " +
                "          AND erst.kandidaten_id IS NOT NULL " +
                "    GROUP BY erst.wahlkreis_id, k.wahljahr " +
                ") " +
                " " +
                "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe, " +
                "  erst.anzahl AS anzahl_absolut, " +
                "  CAST(erst.anzahl AS NUMERIC) / " +
                "  (CAST(( " +
                "          SELECT gesamtstimmen " +
                "          FROM stimmen_gesamt sg " +
                "          WHERE sg.wahljahr = p.wahljahr " +
                "                AND sg.wahlkreis_id = erst.wahlkreis_id " +
                "        ) AS NUMERIC))       AS anzahl_relativ " +
                "FROM " + getErststimmenTable(modus) + " erst, kandidaten k, parteien p, wahlkreise wk " +
                "WHERE erst.wahlkreis_id = k.wahlkreis_id " +
                "      AND erst.kandidaten_id = k.id " +
                "      AND k.partei_id = p.id " +
                "      AND erst.kandidaten_id IS NOT NULL " +
                "      AND k.wahljahr = " + jahr + " " +
                "      AND k.wahlkreis_id = wk.id " +
                "      AND wk.nummer = "+ wknr +
                ";"
                ;
    }

    public static String getWkZweitstimmenProParteiQuery(int jahr, int wknr, String modus) {
        return "WITH stimmen_gesamt AS ( " +
                "    SELECT " +
                "      sum(zweit.anzahl) as zweitstimmen, " +
                "      zweit.wahlkreis_id, " +
                "      p.wahljahr " +
                "    FROM " + getZweitstimmenTable(modus) + " zweit, parteien p " +
                "    WHERE zweit.partei_id = p.id " +
                "          AND zweit.partei_id IS NOT NULL " +
                "    GROUP BY zweit.wahlkreis_id, p.wahljahr " +
                ") " +
                " " +
                "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe, " +
                "  zweit.anzahl AS anzahl_absolut, " +
                "  CAST(zweit.anzahl AS NUMERIC) / " +
                "  (CAST(( " +
                "          SELECT zweitstimmen " +
                "          FROM stimmen_gesamt sg " +
                "          WHERE sg.wahljahr = p.wahljahr " +
                "                AND sg.wahlkreis_id = zweit.wahlkreis_id " +
                "        ) AS NUMERIC))       AS anzahl_relativ " +
                "FROM " + getZweitstimmenTable(modus) + " zweit, parteien p, wahlkreise wk " +
                "WHERE zweit.partei_id = p.id " +
                "      AND zweit.partei_id IS NOT NULL " +
                "      AND p.wahljahr = " + jahr + " " +
                "      AND zweit.wahlkreis_id = wk.id " +
                "      AND wk.nummer = " + wknr +
                ";"
                ;
    }

    /*
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
    */

    public static String getWkVergleichVorjahrErstQuery(int wknr, String modus) {
        return "WITH stimmen_partei AS ( " +
                "    SELECT erst.anzahl as gesamtstimmen, " +
                "      erst.wahlkreis_id, " +
                "      p.id as partei_id, " +
                "      p.wahljahr " +
                "    FROM " + getErststimmenTable(modus) + " erst, parteien p, kandidaten k " +
                "      WHERE " +
                "          erst.kandidaten_id = k.id " +
                "          AND k.partei_id = p.id " +
                "          AND erst.kandidaten_id IS NOT NULL " +
                ") " +
                wkVergleichVorjahrAux(wknr) +
                "; "
                ;
    }

    public static String getWkVergleichVorjahrZweitQuery(int wknr, String modus) {
        return "WITH stimmen_partei AS ( " +
                "    SELECT zweit.anzahl as gesamtstimmen, " +
                "      zweit.wahlkreis_id, " +
                "      p.id as partei_id, " +
                "      p.wahljahr " +
                "    FROM " + getZweitstimmenTable(modus) + " zweit, parteien p " +
                "    WHERE zweit.partei_id = p.id " +
                "          AND zweit.partei_id IS NOT NULL " +
                ")" +
                wkVergleichVorjahrAux(wknr) +
                ";"
                ;
    }

    private static String wkVergleichVorjahrAux(int wknr) {
        return "SELECT " +
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
                "AND w1.nummer = " + wknr
                ;
    }

    /*
    public static String getWkSiegerErststimmenQuery(int jahr, int wknr, String modus){
        return "WITH maximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS ( " +
                "    SELECT " +
                "      w.wahljahr, " +
                "      w.id, " +
                "      max(e.anzahl) " +
                "    FROM " + getErststimmenTable(modus) + " e, wahlkreise w " +
                "    WHERE e.wahlkreis_id = w.id " +
                "    GROUP BY w.wahljahr, w.id " +
                ") " +
                " " +
                "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe " +
                "FROM parteien p, maximaleErststimmen max, " + getErststimmenTable(modus) + " e, kandidaten k " +
                "WHERE p.id = k.partei_id " +
                "      AND max.wahlkreis_id = e.wahlkreis_id " +
                "      AND e.anzahl = max.maxErst " +
                "      AND e.kandidaten_id = k.id " +
                "      AND k.wahljahr = max.wahljahr " +
                "      AND k.wahlkreis_id = e.wahlkreis_id " +
                "      AND k.wahljahr = " + jahr + " " +
                "      AND e.wahlkreis_id = " + wknr +
                ";"
                ;
    }
    */


    public static String getWkSiegerZweitstimmenQuery(int jahr, int wknr, String modus) {
        return "WITH maximaleZweitstimmen (wahljahr, wahlkreis_id, maxZweit) AS (  " +
                "      SELECT  " +
                "        w.wahljahr,  " +
                "        w.id,  " +
                "        max(z.anzahl)  " +
                "      FROM " + getZweitstimmenTable(modus) + " z, wahlkreise w  " +
                "      WHERE z.wahlkreis_id = w.id  " +
                "      GROUP BY w.wahljahr, w.id  " +
                "  )  " +
                "  " +
                "SELECT  " +
                "  p.kuerzel,  " +
                "  p.name,  " +
                "  p.farbe  " +
                "FROM wahlkreise wk, parteien p, maximaleZweitstimmen max, " + getZweitstimmenTable(modus) + " z  " +
                "WHERE p.id = z.partei_id  " +
                "      AND max.wahlkreis_id = z.wahlkreis_id  " +
                "      AND z.anzahl = max.maxZweit  " +
                "      AND p.wahljahr = max.wahljahr" +
                "      AND p.wahljahr = " + jahr + " " +
                "      AND z.wahlkreis_id = wk.id " +
                "      AND wk.nummer = " + wknr +
                ";" ;
    }
}
