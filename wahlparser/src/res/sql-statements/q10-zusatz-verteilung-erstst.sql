-- Verteilung Erststimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  p.name,
  CAST(sum(e.anzahl) AS NUMERIC) / CAST(
      (SELECT sum(anzahl) FROM erststimmenergebnisse) AS NUMERIC
  ) as anteilErststimmen
FROM parteien p, erststimmenergebnisse e, kandidaten k
WHERE e.kandidaten_id = k.id
AND k.partei_id = p.id
GROUP BY p.id, p.kuerzel, p.name
ORDER BY anteilErststimmen DESC;

-- Verteilung Zweitstimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  p.name,
  CAST(sum(z.anzahl) AS NUMERIC) / CAST(
      (SELECT sum(anzahl) FROM zweitstimmenergebnisse) AS NUMERIC
  ) as anteilZweitstimmen
FROM parteien p, zweitstimmenergebnisse z
WHERE z.partei_id = p.id
GROUP BY p.id, p.kuerzel, p.name
ORDER BY anteilZweitstimmen DESC;
