package sqlbuild;

import static sqlbuild.Replace.getErststimmenTable;
import static sqlbuild.Replace.getGewaehlteErstkandidatenTable;
import static sqlbuild.Replace.getZweitstimmenTable;

public class WahlanalysenSQL {

    public static String getKnappsteSiegerQuery(int jahr, String modus) {
        return "WITH maximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS ( " +
                "    SELECT " +
                "      w.wahljahr, " +
                "      w.id, " +
                "      max(e.anzahl) " +
                "    FROM " + getErststimmenTable(modus) + " e, wahlkreise w " +
                "    WHERE e.wahlkreis_id = w.id " +
                "    GROUP BY w.wahljahr, w.id " +
                "), " +
                " " +
                "    siegerErststimmen (wahljahr, wahlkreis_id, kandidaten_id, maxErst) AS ( " +
                "      SELECT " +
                "        k.wahljahr, " +
                "        e.wahlkreis_id, " +
                "        k.id, " +
                "        max.maxErst " +
                "      FROM maximaleErststimmen max, " + getErststimmenTable(modus) + " e, kandidaten k " +
                "      WHERE max.wahlkreis_id = e.wahlkreis_id " +
                "            AND e.anzahl = max.maxErst " +
                "            AND e.kandidaten_id = k.id " +
                "            AND k.wahljahr = max.wahljahr " +
                "            AND k.wahlkreis_id = e.wahlkreis_id " +
                "  ), " +
                " " +
                "    ohneSieger (wahljahr, wahlkreis_id, kandidaten_id) AS ( " +
                "    ( " +
                "      SELECT " +
                "        wahljahr, " +
                "        wahlkreis_id, " +
                "        id AS kandidaten_id " +
                "      FROM kandidaten " +
                "      WHERE wahlkreis_id IS NOT NULL " +
                "    ) " +
                "    EXCEPT ( " +
                "      SELECT " +
                "        wahljahr, " +
                "        wahlkreis_id, " +
                "        kandidaten_id " +
                "      FROM siegerErststimmen " +
                "    ) " +
                "  ), " +
                " " +
                "    zweitmaximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS ( " +
                "      SELECT " +
                "        w.wahljahr, " +
                "        w.id, " +
                "        max(e.anzahl) " +
                "      FROM " + getErststimmenTable(modus) + " e, wahlkreise w, ohneSieger os " +
                "      WHERE e.wahlkreis_id = w.id " +
                "            AND os.kandidaten_id = e.kandidaten_id " +
                "      GROUP BY w.wahljahr, w.id " +
                "  ), " +
                " " +
                "    zweiterErststimmen (wahljahr, wahlkreis_id, kandidaten_id, zweitmaxErst) AS ( " +
                "      SELECT " +
                "        os.wahljahr, " +
                "        e.wahlkreis_id, " +
                "        os.kandidaten_id, " +
                "        zweitmax.maxErst, " +
                "        p.kuerzel " +
                "      FROM zweitmaximaleErststimmen zweitmax, " + getErststimmenTable(modus) + " e, ohneSieger os, kandidaten k, parteien p " +
                "      WHERE zweitmax.wahlkreis_id = e.wahlkreis_id " +
                "            AND e.anzahl = zweitmax.maxErst " +
                "            AND e.kandidaten_id = os.kandidaten_id " +
                "            AND os.wahljahr = zweitmax.wahljahr " +
                "            AND os.wahlkreis_id = e.wahlkreis_id " +
                "  AND os.kandidaten_id = k.id " +
                "  AND  k.partei_id = p.id " +
                "  ORDER BY p.kuerzel " +
                "  ), " +
                " " +
                "    knappste_sieger (wahljahr, kandidaten_id, partei_id, unterschied) AS ( " +
                "      SELECT " +
                "        z.wahljahr, " +
                "        s.kandidaten_id, " +
                "        p.id, " +
                "        s.maxErst - z.zweitmaxErst AS unterschied " +
                "      FROM siegerErststimmen s, zweiterErststimmen z, parteien p, kandidaten k1 " +
                "      WHERE " +
                "        s.wahlkreis_id = z.wahlkreis_id " +
                "        AND s.wahljahr = z.wahljahr " +
                "        AND s.kandidaten_id = k1.id " +
                "        AND k1.partei_id = p.id " +
                "  ), " +
                " " +
                "    knappste_verlierer(wahljahr, kandidaten_id, partei_id, unterschied) AS ( " +
                "      SELECT " +
                "        p.wahljahr, " +
                "        os.kandidaten_id, " +
                "        p.id, " +
                "        s.maxErst - e.anzahl AS unterschied " +
                "      FROM siegerErststimmen s, " + getErststimmenTable(modus) + " e, parteien p, kandidaten k, ohneSieger os " +
                "      WHERE " +
                "        s.wahlkreis_id = e.wahlkreis_id " +
                "          AND s.wahljahr = p.wahljahr " +
                "    AND e.kandidaten_id = os.kandidaten_id " +
                "    AND os.kandidaten_id = k.id " +
                "    AND k.partei_id = p.id " +
                " " +
                "  ), " +
                " " +
                "    siegerundverlierer(wahljahr, kandidaten_id, partei_id, unterschied, siegeroderverlierer) AS ( " +
                "    (  SELECT ks.*, 's' " +
                "    FROM knappste_sieger ks ) " +
                " " +
                "    UNION ALL " +
                "    ( " +
                "      SELECT kv.*, 'v' " +
                "      FROM knappste_verlierer kv " +
                "    ) " +
                "  ), " +
                " " +
                "  aux (wahljahr, kandidaten_id, partei_id, unterschied, anzahl_reihen, siegeroderverlierer) AS ( " +
                "    SELECT " +
                "      wahljahr, " +
                "      kandidaten_id, " +
                "      partei_id, " +
                "      unterschied, " +
                "      ROW_NUMBER() " +
                "      OVER ( " +
                "        PARTITION BY " +
                "          partei_id, wahljahr " +
                "        ORDER BY siegeroderverlierer ASC, unterschied ASC ) AS anzahl_reihen, " +
                "      siegeroderverlierer " +
                "    FROM siegerundverlierer " +
                "  ) " +
                " " +
                "SELECT " +
                "  k.titel, " +
                "   k.name, " +
                "   k.vorname, " +
                "   k.namenszusatz, " +
                "   k.geburtsjahr, " +
                "   p.kuerzel, " +
                "   p.name, " +
                "   p.farbe, " +
                "  aux.siegeroderverlierer, " +
                "   aux.unterschied " +
                " FROM aux aux, parteien p, kandidaten k " +
                " WHERE " +
                "   aux.anzahl_reihen <= 10 " +
                "   AND aux.kandidaten_id = k.id " +
                "   AND aux.partei_id = p.id " +
                "   AND p.wahljahr = " + jahr +
                ";"
                ;
    }

    public static String getFrauenbonusQuery(String modus) {
        return "WITH geschlechtGewaehlteKandidaten(wahljahr, geschlecht, anzahl) AS ( " +
                "    SELECT " +
                "      gk.wahljahr, " +
                "      k.geschlecht, " +
                "      count(*) " +
                "    FROM kandidaten k, " + getGewaehlteErstkandidatenTable(modus) + " gk " +
                "    WHERE k.id = gk.kandidat_id " +
                "    GROUP BY k.geschlecht, gk.wahljahr " +
                ") " +
                "SELECT " +
                "  ( " +
                "    CAST(anzahl AS NUMERIC) " +
                "    / " +
                "    ( " +
                "      SELECT SUM(anzahl) " +
                "      FROM geschlechtGewaehlteKandidaten gk2 " +
                "      WHERE gk2.wahljahr = gk.wahljahr " +
                "            AND gk2.geschlecht is not null " +
                "    ) " +
                "  ) " +
                "  /  " +
                "  ( " +
                "    CAST(( " +
                "           SELECT count(*) " +
                "           FROM kandidaten k " +
                "           WHERE k.geschlecht = 'w' " +
                "                 AND k.wahlkreis_id IS NOT NULL " +
                "                 AND k.wahljahr = gk.wahljahr " +
                "         ) AS NUMERIC) " +
                "    / " +
                "    ( " +
                "      SELECT count(*) " +
                "      FROM kandidaten k " +
                "      WHERE k.wahlkreis_id IS NOT NULL " +
                "            AND k.geschlecht is not null " +
                "            AND k.wahljahr = gk.wahljahr " +
                "    ) " +
                "  ) AS CHANCE_FRAUEN_GEWAEHLT_ZU_WERDEN " +
                "FROM geschlechtGewaehlteKandidaten gk " +
                "WHERE geschlecht = 'w';"
                ;
    }

    public static String getVerteilungErststimmenQuery(int jahr, String modus) {
        return "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe, " +
                "  sum(e.anzahl) as stimmen_absolut, " +
                "  CAST(sum(e.anzahl) AS NUMERIC) / CAST( " +
                "      (SELECT sum(anzahl) FROM " + getErststimmenTable(modus) + ") AS NUMERIC " +
                "  ) as stimmen_relativ " +
                "FROM parteien p, " + getErststimmenTable(modus) + " e, kandidaten k " +
                "WHERE e.kandidaten_id = k.id " +
                "AND k.partei_id = p.id " +
                "AND p.wahljahr = " + jahr + " " +
                "GROUP BY p.id, p.kuerzel, p.name " +
                "ORDER BY stimmen_absolut"
                ;
    }

    public static String getVerteilungZweitstimmenQuery(int jahr, String modus) {
        return "SELECT " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe, " +
                "  sum(z.anzahl) AS stimmen_absolut, " +
                "  CAST(sum(z.anzahl) AS NUMERIC) / CAST( " +
                "      (SELECT sum(anzahl) FROM " + getZweitstimmenTable(modus) + ") AS NUMERIC " +
                "  ) as stimmen_relativ " +
                "FROM parteien p, " + getZweitstimmenTable(modus) + " z " +
                "WHERE z.partei_id = p.id " +
                "AND p.wahljahr = " + jahr + " " +
                "GROUP BY p.id, p.kuerzel, p.name " +
                "ORDER BY stimmen_absolut"
                ;
    }

}
