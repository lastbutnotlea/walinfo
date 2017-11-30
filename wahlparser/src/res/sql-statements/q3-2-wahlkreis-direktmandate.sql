WITH maximaleStimmenWahlkreis (wahljahr, wahlkreis_id, maximal) AS (
    SELECT
      w.wahljahr,
      w.id,
      max(e.anzahl)
    FROM erststimmenergebnisse e, wahlkreise w
    WHERE e.wahlkreis_id = w.id
    GROUP BY w.wahljahr, w.id
)

SELECT
  k.id,
  k.name,
  k.vorname,
  k.wahljahr
FROM erststimmenergebnisse e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m
WHERE e.kandidaten_id = k.id
      AND e.wahlkreis_id = w.id
      AND m.maximal = e.anzahl
      AND m.wahlkreis_id = w.id
      AND k.wahljahr = m.wahljahr
      AND w.wahljahr = m.wahljahr
      AND k.wahlkreis_id = e.wahlkreis_id;