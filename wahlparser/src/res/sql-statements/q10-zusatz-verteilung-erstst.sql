-- Wie ist die Verteilung der Erststimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  (cast(count(*) AS NUMERIC)
   / (SELECT count(*)
      FROM GEWAEHLTE_ERSTKANDIDATEN)) * 100 AS Anteile_Prozent
FROM parteien p, kandidaten k, GEWAEHLTE_ERSTKANDIDATEN gk
WHERE k.id = gk.kandidat_id
      AND k.partei_id = p.id
GROUP BY p.kuerzel;