-- WAHLKREISMAPPING FEHLT NOCH!
-- vielleicht so: ID Nummer Nummer ID

WITH stimmen_partei AS (
    SELECT erst.anzahl as gesamtstimmen,
      erst.wahlkreis_id,
      p.id as partei_id,
      p.wahljahr
    FROM erststimmenergebnisse erst, parteien p, kandidaten k
      WHERE
          erst.kandidaten_id = k.id
          AND k.partei_id = p.id
          AND erst.kandidaten_id IS NOT NULL
)

SELECT
  p1.kuerzel,
  p1.name,
  p1.farbe,
  sp1.gesamtstimmen as stimmen_2013,
  sp2.gesamtstimmen as stimmen_2017
FROM stimmen_partei sp1, stimmen_partei sp2, parteien p1, parteien p2,
  wahlkreise w1, wahlkreise w2
WHERE sp1.wahlkreis_id = w1.id
  AND sp2.wahlkreis_id = w2.id
  AND w1.name LIKE w2.name
AND sp1.partei_id = p1.id
AND sp2.partei_id = p2.id
AND p1.kuerzel = p2.kuerzel
AND sp1.wahljahr = 2013
AND sp2.wahljahr = 2017
AND w2.nummer = 194;


