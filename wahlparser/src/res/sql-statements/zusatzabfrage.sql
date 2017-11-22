--(Frauenquote gewählte Direktkandidaten) / (Frauenquote Direktkandidaten) => Ist dieser Wert größer als 1 => „Frauenbonus“, sonst: „Männerbonus“
WITH geschlechtKandidaten(wahljahr, geschlecht, anzahl) AS (
    SELECT
      gk.wahljahr,
      k.geschlecht,
      count(*)
    FROM kandidaten k, GEWAEHLTE_ERSTKANDIDATEN gk
    WHERE k.id = gk.kandidat_id
    GROUP BY k.geschlecht, gk.wahljahr
)
SELECT
  wahljahr,
  (
    CAST(anzahl AS NUMERIC)
    /
    (
      SELECT SUM(anzahl)
      FROM geschlechtKandidaten gk2
      WHERE gk2.wahljahr = gk.wahljahr
        AND gk2.geschlecht is not null
    )
  )
  / --as Frauenquote_Gewaehlt,
  (
    CAST((
           SELECT count(*)
           FROM kandidaten kk
           WHERE kk.geschlecht = 'w'
                 AND kk.wahlkreis_id IS NOT NULL
                 AND kk.wahljahr = gk.wahljahr
         ) AS NUMERIC)
    /
    (
      SELECT count(*)
      FROM kandidaten kk
      WHERE kk.wahlkreis_id IS NOT NULL
        AND kk.geschlecht is not null
            AND kk.wahljahr = gk.wahljahr
    )
  ) AS CHANCE_FRAUEN_GEWAEHT_ZU_WERDEN
FROM geschlechtKandidaten gk
WHERE geschlecht = 'w';

--Anteile Frauen/Männer im gesamten Bundestag und in einzelnen Wahlkreisen, auch aufgeschlüsselt nach verschiedenen Altersgruppen
SELECT
  w.wahljahr,
  w.id,
  w.name,
  k.geschlecht,
  count(*) as anzahl,
  CAST(count(*) AS NUMERIC) /
  (SELECT count(*)
   FROM kandidaten k2, wahlkreise w2
   WHERE k2.wahlkreis_id = w2.id
    AND w2.id = w.id) as anteil
FROM kandidaten k, wahlkreise w
WHERE k.wahlkreis_id = w.id
GROUP BY w.wahljahr, w.id, w.name, k.geschlecht
ORDER BY w.id ASC;

--Wie ist die Verteilung der Erststimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  (cast(count(*) AS NUMERIC)
   / (SELECT count(*)
      FROM GEWAEHLTE_ERSTKANDIDATEN)) * 100 AS Anteile_Prozent
FROM parteien p, kandidaten k, GEWAEHLTE_ERSTKANDIDATEN gk
WHERE k.id = gk.kandidat_id
      AND k.partei_id = p.id
GROUP BY p.kuerzel;