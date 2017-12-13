SELECT *
FROM words
ORDER BY random()
limit 10;

INSERT INTO tokens (token, wahlkreis_nr, gueltig) VALUES
  ('token', ?, 'n');


INSERT INTO tokens (token, wahlkreis_nr, gueltig) VALUES
  ('Singpult-Snodgrass-Skalenteile-Singapur-Kohlenstoffe-Weiterfragen-Zinskosten-Herrenreiter-Smog', 100, 'n');

DELETE FROM tokens;


SELECT token, gueltig
FROM tokens
WHERE wahlkreis_nr = ?;
