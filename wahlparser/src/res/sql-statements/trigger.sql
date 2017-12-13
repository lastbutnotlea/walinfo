CREATE OR REPLACE FUNCTION update_erststimmenerg()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  UPDATE erststimmenergebnisse SET
    anzahl = anzahl + 1
    WHERE wahlkreis_id = NEW.wahlkreis_id
    AND kandidaten_id = NEW.kandidaten_id;
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;


CREATE TRIGGER neue_erststimme AFTER INSERT ON erststimmen
FOR EACH ROW EXECUTE PROCEDURE update_erststimmenerg();


CREATE OR REPLACE FUNCTION update_zweitstimmenerg()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  UPDATE zweitstimmenergebnisse SET
    anzahl = anzahl + 1
    WHERE wahlkreis_id = NEW.wahlkreis_id
    AND partei_id = NEW.partei_id;
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER neue_zweitstimme AFTER INSERT ON zweitstimmen
  FOR EACH ROW EXECUTE PROCEDURE update_zweitstimmenerg();


-- DROP TRIGGER  neue_erststimme ON erststimmen;
-- DROP TRIGGER  neue_zweitstimme ON zweitstimmen;


  ---------TEST-----------

  INSERT INTO erststimmen VALUES (5981, 552);

  SELECT count(*) FROM erststimmen;

  SELECT * from erststimmenergebnisse
WHERE kandidaten_id = 5981
AND wahlkreis_id = 552;

INSERT INTO zweitstimmen VALUES (18, 52);

SELECT * FROM zweitstimmenergebnisse
WHERE partei_id = 18
AND wahlkreis_id = 52;