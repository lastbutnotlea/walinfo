-- TOKENS


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


-- token gültig?
SELECT wk.id
FROM tokens, wahlkreise wk
WHERE verwendet = 'n'
AND tokens.wahlkreis_nr = wk.nummer
AND wk.wahljahr = 2017
AND token = ?;

-- kandidatenid okay?
SELECT *
FROM kandidaten k
WHERE k.wahlkreis_id = ?
AND k.id = ?;

-- stimmabgabe erststimme
INSERT INTO erststimmen VALUES (?, ?);

-- parteiid okay?
SELECT *
FROM parteien p, zweitstimmenergebnisse z
WHERE z.partei_id = p.id
AND z.wahlkreis_id = ?
AND p.id = ?;

-- stimmabgabe zweistimmen
INSERT INTO zweitstimmen VALUES(?, ?);

-- token wurde verwendet
UPDATE tokens SET
  verwendet = 'j' WHERE token = ?;
