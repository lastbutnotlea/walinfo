-- Verteilung Erststimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  p.name,
  p.farbe,
  sum(e.anzahl) as stimmen_absolut,
  CAST(sum(e.anzahl) AS NUMERIC) / CAST(
      (SELECT sum(anzahl) FROM erststimmenergebnisse) AS NUMERIC
  ) as stimmen_relativ
FROM parteien p, erststimmenergebnisse e, kandidaten k
WHERE e.kandidaten_id = k.id
AND k.partei_id = p.id
GROUP BY p.id, p.kuerzel, p.name;

-- Verteilung Zweitstimmen auf Deutschlandebene
SELECT
  p.kuerzel,
  p.name,
  p.farbe,
  sum(z.anzahl) AS stimmen_absolut,
  CAST(sum(z.anzahl) AS NUMERIC) / CAST(
      (SELECT sum(anzahl) FROM zweitstimmenergebnisse) AS NUMERIC
  ) as stimmen_relativ
FROM parteien p, zweitstimmenergebnisse z
WHERE z.partei_id = p.id
GROUP BY p.id, p.kuerzel, p.name;
