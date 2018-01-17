-- ZUSATZANFRAGE 'FRAUENBONUS'


--(Frauenquote gewählte Direktkandidaten) / (Frauenquote Direktkandidaten)
-- => Ist dieser Wert größer als 1 => „Frauenbonus“, sonst: „Männerbonus“

WITH geschlechtGewaehlteKandidaten(wahljahr, geschlecht, anzahl) AS (
    SELECT
      gk.wahljahr,
      k.geschlecht,
      count(*)
    FROM kandidaten k, gewaehlte_erstkandidaten_schnell gk
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
      FROM geschlechtGewaehlteKandidaten gk2
      WHERE gk2.wahljahr = gk.wahljahr
            AND gk2.geschlecht is not null
    )
  )
  /
  (
    CAST((
           SELECT count(*)
           FROM kandidaten k
           WHERE k.geschlecht = 'w'
                 AND k.wahlkreis_id IS NOT NULL
                 AND k.wahljahr = gk.wahljahr
         ) AS NUMERIC)
    /
    (
      SELECT count(*)
      FROM kandidaten k
      WHERE k.wahlkreis_id IS NOT NULL
            AND k.geschlecht is not null
            AND k.wahljahr = gk.wahljahr
    )
  ) AS CHANCE_FRAUEN_GEWAEHLT_ZU_WERDEN
FROM geschlechtGewaehlteKandidaten gk
WHERE geschlecht = 'w';