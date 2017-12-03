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
  k.titel,
  k.name,
  k.vorname,
  k.namenszusatz,
  k.geburtsjahr,
  p.kuerzel,
  p.name,
  p.farbe
FROM erststimmenergebnisse e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m, parteien p
WHERE e.kandidaten_id = k.id
      AND e.wahlkreis_id = w.id
      AND m.maximal = e.anzahl
      AND m.wahlkreis_id = w.id
      AND k.wahljahr = m.wahljahr
      AND w.wahljahr = m.wahljahr
      AND k.wahlkreis_id = e.wahlkreis_id
      AND k.partei_id = p.id;