-- KNAPPSTE SIEGER UND KNAPPSTE VERLIERER


-- maximale anzahl an erststimmen pro wahlkreis
WITH maximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS (
    SELECT
      w.wahljahr,
      w.id,
      max(e.anzahl)
    FROM erststimmenergebnisse e, wahlkreise w
    WHERE e.wahlkreis_id = w.id
    GROUP BY w.wahljahr, w.id
),

    -- sieger in den erststimmen pro wahlkreis
    siegerErststimmen (wahljahr, wahlkreis_id, kandidaten_id, maxErst) AS (
      SELECT
        k.wahljahr,
        e.wahlkreis_id,
        k.id,
        max.maxErst
      FROM maximaleErststimmen max, erststimmenergebnisse e, kandidaten k
      WHERE max.wahlkreis_id = e.wahlkreis_id
            AND e.anzahl = max.maxErst
            AND e.kandidaten_id = k.id
            AND k.wahljahr = max.wahljahr
            AND k.wahlkreis_id = e.wahlkreis_id
  ),

    -- kandidaten ohne erststimmensieger
    ohneSieger (wahljahr, wahlkreis_id, kandidaten_id) AS (
    (
      SELECT
        wahljahr,
        wahlkreis_id,
        id AS kandidaten_id
      FROM kandidaten
      WHERE wahlkreis_id IS NOT NULL
    )
    EXCEPT (
      SELECT
        wahljahr,
        wahlkreis_id,
        kandidaten_id
      FROM siegerErststimmen
    )
  ),

    -- zweit maximale anzahl an erststimmen pro wahlkreis
    zweitmaximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS (
      SELECT
        w.wahljahr,
        w.id,
        max(e.anzahl)
      FROM erststimmenergebnisse e, wahlkreise w, ohneSieger os
      WHERE e.wahlkreis_id = w.id
            AND os.kandidaten_id = e.kandidaten_id
      GROUP BY w.wahljahr, w.id
  ),

    -- zweitplatzierter in den erststimmen pro wahlkreis
    zweiterErststimmen (wahljahr, wahlkreis_id, kandidaten_id, zweitmaxErst) AS (
      SELECT
        os.wahljahr,
        e.wahlkreis_id,
        os.kandidaten_id,
        zweitmax.maxErst,
        p.kuerzel
      FROM zweitmaximaleErststimmen zweitmax, erststimmenergebnisse e, ohneSieger os, kandidaten k, parteien p
      WHERE zweitmax.wahlkreis_id = e.wahlkreis_id
            AND e.anzahl = zweitmax.maxErst
            AND e.kandidaten_id = os.kandidaten_id
            AND os.wahljahr = zweitmax.wahljahr
            AND os.wahlkreis_id = e.wahlkreis_id
  AND os.kandidaten_id = k.id
  AND  k.partei_id = p.id
  ORDER BY p.kuerzel
  ),

    -- knappste sieger (= abstand zwischen sieger und zweitplatzierter am kleinsten)
    knappste_sieger (wahljahr, kandidaten_id, partei_id, unterschied) AS (
      SELECT
        z.wahljahr,
        s.kandidaten_id,
        p.id,
        s.maxErst - z.zweitmaxErst AS unterschied
      FROM siegerErststimmen s, zweiterErststimmen z, parteien p, kandidaten k1
      WHERE
        s.wahlkreis_id = z.wahlkreis_id
        AND s.wahljahr = z.wahljahr
        AND s.kandidaten_id = k1.id
        AND k1.partei_id = p.id
  ),

    -- knappste verlierer
    knappste_verlierer(wahljahr, kandidaten_id, partei_id, unterschied) AS (
      SELECT
        p.wahljahr,
        os.kandidaten_id,
        p.id,
        s.maxErst - e.anzahl AS unterschied
      FROM siegerErststimmen s, erststimmenergebnisse e, parteien p, kandidaten k, ohneSieger os
      WHERE
        s.wahlkreis_id = e.wahlkreis_id
          AND s.wahljahr = p.wahljahr
    AND e.kandidaten_id = os.kandidaten_id
    AND os.kandidaten_id = k.id
    AND k.partei_id = p.id

  ),

    -- vereint sieger und verlierer
    siegerundverlierer(wahljahr, kandidaten_id, partei_id, unterschied, siegeroderverlierer) AS (
    (  SELECT ks.*, 's'
    FROM knappste_sieger ks )

    UNION ALL
    (
      SELECT kv.*, 'v'
      FROM knappste_verlierer kv
    )
  ),

  -- hilfstabelle
  aux (wahljahr, kandidaten_id, partei_id, unterschied, anzahl_reihen, siegeroderverlierer) AS (
    SELECT
      wahljahr,
      kandidaten_id,
      partei_id,
      unterschied,
      ROW_NUMBER()
      OVER (
        PARTITION BY
          partei_id, wahljahr
        ORDER BY siegeroderverlierer ASC, unterschied ASC ) AS anzahl_reihen,
      siegeroderverlierer
    FROM siegerundverlierer
  )


-- selektiert die ersten 10 einträge pro partei -> es wird mit verlierern aufgefüllt,
-- wenn nicht genügend sieger pro partei vorhanden
SELECT
  k.titel,
   k.name,
   k.vorname,
   k.namenszusatz,
   k.geburtsjahr,
   p.kuerzel,
   p.name,
  p.farbe,
  aux.siegeroderverlierer,
   aux.unterschied
 FROM aux aux, parteien p, kandidaten k
 WHERE
   aux.anzahl_reihen <= 10
   AND aux.kandidaten_id = k.id
   AND aux.partei_id = p.id;







