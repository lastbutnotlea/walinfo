with unendlichkeit(nummer) AS (
  SELECT * from generate_series(1, (select max(anzahl) from erststimmenergebnisse))
)
    SELECT e.wahlkreis_id, e.kandidaten_id
    FROM erststimmenergebnisse e, unendlichkeit u
    WHERE u.nummer <= e.anzahl
  ;

with unendlichkeit(nummer) AS (
    SELECT * from generate_series(1, (select max(anzahl) from zweitstimmenergebnisse))
)
SELECT e.wahlkreis_id, e.partei_id
  FROM zweitstimmenergebnisse e, unendlichkeit u
  WHERE u.nummer <= e.anzahl;