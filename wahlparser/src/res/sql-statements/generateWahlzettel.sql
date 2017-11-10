with leereTabelle(nummer) AS (
  SELECT * from generate_series(1, (select max(anzahl) from erststimmenergebnisse))
)
  INSERT INTO erststimmen(kandidaten_id, wahlkreis_id, gueltig)
    SELECT e.kandidaten_id , e.wahlkreis_id, 'J'
    FROM erststimmenergebnisse e, leereTabelle u
    WHERE u.nummer <= e.anzahl
  ;

with leereTabelle(nummer) AS (
    SELECT * from generate_series(1, (select max(anzahl) from zweitstimmenergebnisse))
)
SELECT e.wahlkreis_id, e.partei_id
  FROM zweitstimmenergebnisse e, leereTabelle u
  WHERE u.nummer <= e.anzahl;
