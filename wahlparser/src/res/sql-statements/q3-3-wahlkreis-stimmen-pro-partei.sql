WITH stimmen_gesamt AS (
    SELECT
      sum(zweit.anzahl) + sum(erst.anzahl) AS gesamtstimmen,
      erst.wahlkreis_id,
      k.wahljahr
    FROM erststimmenergebnisse erst, zweitstimmenergebnisse zweit, parteien p, kandidaten k
    WHERE zweit.partei_id = p.id
          AND erst.wahlkreis_id = zweit.wahlkreis_id
          AND k.id = erst.kandidaten_id
          AND k.partei_id = p.id
          AND zweit.partei_id IS NOT NULL
          AND erst.kandidaten_id IS NOT NULL
    GROUP BY erst.wahlkreis_id, k.wahljahr
)

SELECT
  erst.wahlkreis_id,
  p.kuerzel,
  erst.anzahl + zweit.anzahl AS anzahl_absolut,
  CAST(erst.anzahl + zweit.anzahl AS NUMERIC) /
  (CAST((
          SELECT gesamtstimmen
          FROM stimmen_gesamt sg
          WHERE sg.wahljahr = p.wahljahr
                AND sg.wahlkreis_id = erst.wahlkreis_id
        ) AS NUMERIC))       AS anzahl_relativ,

  k.wahljahr
FROM erststimmenergebnisse erst, zweitstimmenergebnisse zweit,
  parteien p, kandidaten k
WHERE erst.wahlkreis_id = zweit.wahlkreis_id
      AND erst.kandidaten_id = k.id
      AND zweit.partei_id = p.id
      AND k.partei_id = p.id
      AND zweit.partei_id IS NOT NULL
      AND erst.kandidaten_id IS NOT NULL
ORDER BY k.wahljahr, k.wahlkreis_id;