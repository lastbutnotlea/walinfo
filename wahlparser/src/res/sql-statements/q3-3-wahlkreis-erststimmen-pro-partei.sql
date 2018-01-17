-- ERSTIMMEN PRO PARTEI AUF WAHLKREISEBENE


WITH stimmen_gesamt AS (
    SELECT
      sum(erst.anzahl) AS gesamtstimmen,
      erst.wahlkreis_id,
      k.wahljahr
    FROM erststimmenergebnisse erst, kandidaten k
    WHERE
          k.id = erst.kandidaten_id
          AND erst.kandidaten_id IS NOT NULL
    GROUP BY erst.wahlkreis_id, k.wahljahr
)

SELECT
  p.kuerzel,
  p.name,
  p.farbe,
  erst.anzahl AS anzahl_absolut,
  CAST(erst.anzahl AS NUMERIC) /
  (CAST((
          SELECT gesamtstimmen
          FROM stimmen_gesamt sg
          WHERE sg.wahljahr = p.wahljahr
                AND sg.wahlkreis_id = erst.wahlkreis_id
        ) AS NUMERIC))       AS anzahl_relativ
FROM erststimmenergebnisse erst, kandidaten k, parteien p
WHERE erst.wahlkreis_id = k.wahlkreis_id
      AND erst.kandidaten_id = k.id
      AND k.partei_id = p.id
      AND erst.kandidaten_id IS NOT NULL
ORDER BY k.wahljahr, k.wahlkreis_id, p.name;