DO $$
DECLARE
  kandidat    RECORD;
  updatedRows INT;
  currentRow  RECORD;
BEGIN
  FOR kandidat IN SELECT *
                  FROM erststimmenergebnisse e, Kandidaten k
                  WHERE e.kandidaten_id = k.id
                        AND k.wahljahr = 2017
  LOOP
    updatedRows := 0;
    FOR currentRow IN
      SELECT *
      FROM Wahlzettel
      WHERE kandidaten_id IS NULL and wahlkreis_id = kandidat.wahlkreis_id
      LIMIT kandidat.anzahl
      FOR UPDATE SKIP LOCKED
    LOOP
      UPDATE wahlzettel
        SET kandidaten_id = kandidat.kandidaten_id
        WHERE currentRow.id = id;
      updatedRows := updatedRows + 1;
    END LOOP;

    FOR i IN updatedRows..(kandidat.anzahl - 1) LOOP
      updatedRows := updatedRows + 1;
      INSERT INTO Wahlzettel (id, kandidaten_id, wahlkreis_id) VALUES (
        nextval('Wahlzettel_SEQ'),
        kandidat.kandidaten_id,
        kandidat.wahlkreis_id);
    END LOOP;

    RAISE NOTICE 'Updated % rows', updatedRows;
  END LOOP;
END$$;