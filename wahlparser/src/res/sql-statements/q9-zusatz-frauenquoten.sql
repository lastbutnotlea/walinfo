-- Anteile Frauen/Männer in den einzelnen Wahlkreisen

SELECT
  count(*) as anzahl_maenner,
  (SELECT count(*)
   FROM kandidaten k2, wahlkreise w2
   WHERE k2.wahlkreis_id = w2.id
         AND w2.id = w.id) as gesamtanzahl
FROM kandidaten k, wahlkreise w
WHERE k.wahlkreis_id = w.id
  AND w.wahljahr = 2017
  AND k.geschlecht = 'm'
GROUP BY w.wahljahr, w.id, k.geschlecht;

