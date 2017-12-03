-- Anteile Frauen/Männer in den einzelnen Wahlkreisen

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

