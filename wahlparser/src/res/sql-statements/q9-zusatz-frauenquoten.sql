-- Anteile Frauen/Männer im gesamten Bundestag und in einzelnen Wahlkreisen,
-- auch aufgeschlüsselt nach verschiedenen Altersgruppen

-- bislang: Frauenquote/Männerquote unter den KandidatenInnen auf Wahlkreisebene
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


-- VIELLEICHT NOCH IN MEHRERE ANFRAGEN AUFSPLITTEN???