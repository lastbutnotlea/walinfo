WITH maximaleZweitstimmen (wahljahr, wahlkreis_id, maxZweit) AS (
      SELECT
        w.wahljahr,
        w.id,
        max(z.anzahl)
      FROM zweitstimmenergebnisse z, wahlkreise w
      WHERE z.wahlkreis_id = w.id
      GROUP BY w.wahljahr, w.id
  )

SELECT
  p.kuerzel,
  p.name,
  p.farbe
FROM parteien p, maximaleZweitstimmen max, zweitstimmenergebnisse z
WHERE p.id = z.partei_id
      AND max.wahlkreis_id = z.wahlkreis_id
      AND z.anzahl = max.maxZweit
      AND p.wahljahr = max.wahljahr