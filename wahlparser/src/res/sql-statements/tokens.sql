SELECT *
FROM words
ORDER BY random()
limit 10;

INSERT INTO tokens (token, wahlkreis_nr, verwendet) VALUES
  ('token', ?, 'n');


INSERT INTO tokens (token, wahlkreis_nr, verwendet) VALUES
  ('Singpult-Snodgrass-Skalenteile-Singapur-Kohlenstoffe-Weiterfragen-Zinskosten-Herrenreiter-Smog', 100, 'n');

DELETE FROM tokens;


SELECT token, verwendet
FROM tokens
WHERE wahlkreis_nr = ?;


SELECT *
FROM tokens
WHERE token = ?;

-- wählbare kandidaten
SELECT
  k.id,
  k.titel,
  k.name,
  k.vorname,
  k.namenszusatz,
  k.geburtsjahr,
  p.id,
  p.kuerzel,
  p.name,
  p.farbe
FROM kandidaten k, wahlkreise wk, parteien p
WHERE k.wahlkreis_id = wk.id
  AND k.partei_id = p.id
  AND k.wahljahr = 2017
AND wk.nummer = ?;

-- wählbare parteien
SELECT DISTINCT
  p.id,
  p.kuerzel,
  p.name,
  p.farbe
  FROM parteien p, wahlkreise wk, zweitstimmenergebnisse z
WHERE z.wahlkreis_id = wk.id
AND z.partei_id = p.id
  AND p.wahljahr = 2017
AND wk.nummer = ?;
