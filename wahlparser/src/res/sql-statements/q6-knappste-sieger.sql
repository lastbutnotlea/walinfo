WITH maximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS (
    SELECT
      w.wahljahr,
      w.id,
      max(e.anzahl)
    FROM erststimmenergebnisse e, wahlkreise w
    WHERE e.wahlkreis_id = w.id
    GROUP BY w.wahljahr, w.id
),

    siegerErststimmen (wahljahr, wahlkreis_id, kandidaten_id, maxErst) AS (
      SELECT
        k.wahljahr,
        e.wahlkreis_id,
        k.wahlkreis_id,
        max.maxErst
      FROM maximaleErststimmen max, erststimmenergebnisse e, kandidaten k
      WHERE max.wahlkreis_id = e.wahlkreis_id
            AND e.anzahl = max.maxErst
            AND e.kandidaten_id = k.id
            AND k.wahljahr = max.wahljahr
            AND k.wahlkreis_id = e.wahlkreis_id
  ),

    ohneSieger (wahljahr, wahlkreis_id, kandidaten_id) AS (
    (
      SELECT
        wahljahr,
        wahlkreis_id,
        id
      FROM kandidaten
      WHERE wahlkreis_id IS NOT NULL
    )
    EXCEPT (
      SELECT wahljahr, wahlkreis_id, kandidaten_id
      FROM siegerErststimmen
    )
  ),

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

    zweiterErststimmen (wahljahr, wahlkreis_id, kandidaten_id, zweitmaxErst) AS (
      SELECT
        os.wahljahr,
        e.wahlkreis_id,
        os.wahlkreis_id,
        zweitmax.maxErst
      FROM zweitmaximaleErststimmen zweitmax, erststimmenergebnisse e, ohneSieger os
      WHERE zweitmax.wahlkreis_id = e.wahlkreis_id
            AND e.anzahl = zweitmax.maxErst
            AND e.kandidaten_id = os.kandidaten_id
            AND os.wahljahr = zweitmax.wahljahr
            AND os.wahlkreis_id = e.wahlkreis_id
  )

      SELECT
        k.titel,
        k.name,
        k.vorname,
        k.namenszusatz,
        k.geburtsjahr,
        p.kuerzel,
        p.name,
        s.maxErst - z.zweitmaxErst as unterschied
      FROM siegerErststimmen s, zweiterErststimmen z, kandidaten k, parteien p
      WHERE
        s.kandidaten_id = k.id
        AND k.partei_id = p.id
        AND s.wahlkreis_id = z.wahlkreis_id
        AND s.wahljahr = z.wahljahr
      ORDER BY unterschied ASC
      LIMIT 10;





