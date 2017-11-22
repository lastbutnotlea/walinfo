--(Frauenquote gewählte Direktkandidaten) / (Frauenquote Direktkandidaten) 
-- => Ist dieser Wert größer als 1 => „Frauenbonus“, sonst: „Männerbonus“
WITH geschlechtKandidaten(wahljahr, geschlecht, anzahl) AS (
    SELECT
      gk.wahljahr,
      k.geschlecht,
      count(*)
    FROM kandidaten k, gewaehlte_erstkandidaten gk
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