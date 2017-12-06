-- WAHLKREISMAPPING FEHLT NOCH!
-- vielleicht so: ID Nummer Nummer ID

WITH stimmen_partei AS (
    SELECT zweit.anzahl + erst.anzahl as gesamtstimmen,
      erst.wahlkreis_id,
      p.id as partei_id,
      k.wahljahr
    FROM zweitstimmenergebnisse zweit, erststimmenergebnisse erst,
      parteien p, kandidaten k
    WHERE zweit.wahlkreis_id = erst.wahlkreis_id
          AND zweit.partei_id = p.id
          AND erst.kandidaten_id = k.id
          AND k.partei_id = p.id
          AND zweit.partei_id IS NOT NULL
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
  AND w1.nummer = w2.nummer
AND sp1.partei_id = p1.id
AND sp2.partei_id = p2.id
AND p1.kuerzel = p2.kuerzel
AND sp1.wahljahr = 2013
AND sp2.wahljahr = 2017;


