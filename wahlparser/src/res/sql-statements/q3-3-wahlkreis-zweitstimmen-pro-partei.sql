-- ZWEITSTIMEN PRO PARTEI AUF WAHLKREISEBENE


WITH stimmen_gesamt AS (
    SELECT
      sum(zweit.anzahl) as zweitstimmen,
      zweit.wahlkreis_id,
      p.wahljahr
    FROM zweitstimmenergebnisse zweit, parteien p
    WHERE zweit.partei_id = p.id
          AND zweit.partei_id IS NOT NULL
    GROUP BY zweit.wahlkreis_id, p.wahljahr
)

SELECT
  p.kuerzel,
  p.name,
  p.farbe,
  zweit.anzahl AS anzahl_absolut,
  CAST(zweit.anzahl AS NUMERIC) /
  (CAST((
          SELECT zweitstimmen
          FROM stimmen_gesamt sg
          WHERE sg.wahljahr = p.wahljahr
                AND sg.wahlkreis_id = zweit.wahlkreis_id
        ) AS NUMERIC))       AS anzahl_relativ
FROM zweitstimmenergebnisse zweit, parteien p
WHERE zweit.partei_id = p.id
      AND zweit.partei_id IS NOT NULL
ORDER BY p.wahljahr, zweit.wahlkreis_id, p.name;