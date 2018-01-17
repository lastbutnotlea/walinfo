-- GENERATE WAHLZETTEL


-- hilfstabelle
with placeholder1(nummer) AS (
  SELECT * from generate_series(1, (select max(anzahl) from erststimmenergebnisse))
)
  -- erzeuge einzelstimmen für alle erststimmen
  INSERT INTO erststimmen(kandidaten_id, wahlkreis_id)
    SELECT e.kandidaten_id , e.wahlkreis_id
    FROM erststimmenergebnisse e, placeholder1 p1, wahlkreise w
    WHERE p1.nummer <= e.anzahl
      and w.id = e.wahlkreis_id
      and w.wahljahr = 2017;
;

-- hilfstabelle
with placeholder2(nummer) AS (
    SELECT * from generate_series(1, (select max(anzahl) from zweitstimmenergebnisse))
)
  -- erzeuge einzelnstimmen für alle zweitstimmen
  INSERT INTO zweitstimmen(partei_id, wahlkreis_id)
    SELECT e.partei_id, e.wahlkreis_id
    FROM zweitstimmenergebnisse e, placeholder2 p2, wahlkreise w
    WHERE p2.nummer <= e.anzahl
      and w.id = e.wahlkreis_id
      and w.wahljahr = 2017
;

-- add foreign keys
/*
ALTER TABLE erststimmen
    ADD FOREIGN KEY (kandidaten_id) REFERENCES kandidaten(id);

ALTER TABLE erststimmen
    ADD FOREIGN KEY (wahlkreis_id) REFERENCES wahlkreise(nummer);

ALTER TABLE zweitstimmen
  ADD FOREIGN KEY (partei_id) REFERENCES parteien(id);

ALTER TABLE zweitstimmen
  ADD FOREIGN KEY (wahlkreis_id) REFERENCES wahlkreise(nummer);
*/