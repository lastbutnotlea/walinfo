-- View für die Rohdaten der Erstimmen

CREATE OR REPLACE VIEW erststimmenergebnisse_view (kandidaten_id, wahlkreis_id, anzahl) AS (

  -- daten von 2013 liegen nur aggregiert vor
  (
    SELECT e.kandidaten_id, e.wahlkreis_id, e.anzahl
    FROM erststimmenergebnisse e, wahlkreise w
          WHERE e.wahlkreis_id = w.id
          AND w.wahljahr = 2013
  )

  -- rohdaten von 2017
  UNION ALL
  (
    SELECT e.kandidaten_id, e.wahlkreis_id, CAST(count(*) AS INTEGER)
    FROM erststimmen e
    GROUP BY e.kandidaten_id, e.wahlkreis_id
  )
);


-- View für die Rohdaten der Zweitstimmen

CREATE OR REPLACE VIEW zweitstimmenergebnisse_view(partei_id, wahlkreis_id, anzahl) AS (

  -- daten von 2013 liegen nur aggregiert vor
  (
    SELECT e.partei_id, e.wahlkreis_id, e.anzahl
    FROM zweitstimmenergebnisse e, wahlkreise w
          WHERE e.wahlkreis_id = w.id
          AND w.wahljahr = 2013
  )

  -- rohdaten von 2017
  UNION ALL
  (
    SELECT e.partei_id, e.wahlkreis_id, count(*)
    FROM zweitstimmen e
    GROUP BY e.partei_id, e.wahlkreis_id
  )
);


-- View für die gewählten Erstkandidaten, greift auf die oben erstellte
-- View für die Erstimmenergebnisse zu
CREATE OR REPLACE VIEW gewaehlte_erstkandidaten (wahljahr, kandidat_id) AS (
  WITH maximaleStimmenWahlkreis (wahljahr, id, maximal) AS (
      SELECT
        w.wahljahr,
        w.id,
        max(e.anzahl)
      FROM erststimmenergebnisse_view e, wahlkreise w
      WHERE e.wahlkreis_id = w.id
      GROUP BY w.wahljahr, w.id
  )
  SELECT
    w.wahljahr,
    k.id
  FROM erststimmenergebnisse_view e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m
  WHERE e.kandidaten_id = k.id
        AND e.wahlkreis_id = w.id
        AND m.maximal = e.anzahl
        AND m.id = w.id
        AND k.wahljahr = m.wahljahr
        AND w.wahljahr = m.wahljahr
);


-- View für die gewählten Erstkandidaten, greift direkt auf die aggregierten
-- Ergebnisse zu und ist damit deutlich schneller
CREATE OR REPLACE VIEW gewaehlte_erstkandidaten_schnell (wahljahr, kandidat_id) AS (
  WITH maximaleStimmenWahlkreis (wahljahr, id, maximal) AS (
      SELECT
        w.wahljahr,
        w.id,
        max(e.anzahl)
      FROM erststimmenergebnisse e, wahlkreise w
      WHERE e.wahlkreis_id = w.id
      GROUP BY w.wahljahr, w.id
  )
  SELECT
    w.wahljahr,
    k.id
  FROM erststimmenergebnisse e, kandidaten k, wahlkreise w, maximaleStimmenWahlkreis m
  WHERE e.kandidaten_id = k.id
        AND e.wahlkreis_id = w.id
        AND m.maximal = e.anzahl
        AND m.id = w.id
        AND k.wahljahr = m.wahljahr
        AND w.wahljahr = m.wahljahr
);



