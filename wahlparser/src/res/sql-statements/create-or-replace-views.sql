-- View für die Erststimmenergebnisse, verwendet als Datenbasis
-- abhängig von der config Tabelle entweder die aggregierten Stimmen
-- oder die Einzelstimmen
CREATE OR REPLACE VIEW erststimmenergebnisse_view (kandidaten_id, wahlkreis_id, anzahl) AS (
  -- falls wir aggregierte daten verwenden
  select e.*
  from erststimmenergebnisse e, global_config
  where global_config.verwende_einzelstimmen = 0

  -- falls nicht, nehmen wir die
  UNION ALL
  (
    SELECT e.kandidaten_id, e.wahlkreis_id, e.anzahl
    FROM erststimmenergebnisse e, global_config c, wahlkreise w
    WHERE c.verwende_einzelstimmen = 1
          AND e.wahlkreis_id = w.id
          AND w.wahljahr = 2013
  )

  UNION ALL
  -- Die Nicht aggregierten Daten sind für 2017
  (
    SELECT e.kandidaten_id, e.wahlkreis_id, count(*)
    FROM erststimmen e, global_config
    WHERE global_config.verwende_einzelstimmen = 1

    GROUP BY e.kandidaten_id, e.wahlkreis_id
  )
);


-- View für die Zweitstimmenergebnisse, verwendet als Datenbasis
-- abhängig von der config Tabelle entweder die aggregierten Stimmen
-- oder die Einzelstimmen
CREATE OR REPLACE VIEW zweitstimmenergebnisse_view(partei_id, wahlkreis_id, anzahl) AS (
  -- falls wir aggregierte daten verwenden
  select e.*
  from zweitstimmenergebnisse e, global_config
  where global_config.verwende_einzelstimmen = 0

  -- falls nicht, nehmen wir die
  UNION ALL
  (
    SELECT e.partei_id, e.wahlkreis_id, e.anzahl
    FROM zweitstimmenergebnisse e, global_config c, wahlkreise w
    WHERE c.verwende_einzelstimmen = 1
          AND e.wahlkreis_id = w.id
          AND w.wahljahr = 2013
  )

  UNION ALL
  -- Die Nicht aggregierten Daten sind für 2017
  (
    SELECT e.partei_id, e.wahlkreis_id, count(*)
    FROM zweitstimmen e, global_config
    WHERE global_config.verwende_einzelstimmen = 1

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

-- führt update auf global_config durch
UPDATE global_config SET verwende_einzelstimmen = 0;


