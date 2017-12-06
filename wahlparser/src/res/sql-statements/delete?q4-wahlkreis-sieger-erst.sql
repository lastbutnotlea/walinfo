WITH maximaleErststimmen (wahljahr, wahlkreis_id, maxErst) AS (
    SELECT
      w.wahljahr,
      w.id,
      max(e.anzahl)
    FROM erststimmenergebnisse e, wahlkreise w
    WHERE e.wahlkreis_id = w.id
    GROUP BY w.wahljahr, w.id
)

SELECT
  p.kuerzel,
  p.name,
  p.farbe
FROM parteien p, maximaleErststimmen max, erststimmenergebnisse e, kandidaten k
WHERE p.id = k.partei_id
      AND max.wahlkreis_id = e.wahlkreis_id
      AND e.anzahl = max.maxErst
      AND e.kandidaten_id = k.id
      AND k.wahljahr = max.wahljahr
      AND k.wahlkreis_id = e.wahlkreis_id;