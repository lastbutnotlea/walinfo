SELECT
  wk.id,
  wk.nummer,
  wk.name,
  wk.bundesland,
  bl.name
FROM wahlkreise wk, bundeslaender bl
WHERE wk.bundesland = bl.kuerzel
AND wahljahr = 2017
ORDER BY wk.nummer;
