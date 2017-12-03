SELECT
  wk.id,
  wk.nummer,
  wk.name,
  wk.bundesland,
  bl.name
FROM wahlkreise wk, bundeslaender bl
WHERE wk.bundesland = bl.kuerzel
ORDER BY wk.nummer;
