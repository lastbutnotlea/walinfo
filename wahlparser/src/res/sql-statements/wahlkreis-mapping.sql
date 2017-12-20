SELECT wk2.nummer
FROM wahlkreise wk1, wahlkreise wk2
WHERE wk1.wahljahr = 2013
AND wk2.wahljahr <> 2013
AND wk1.nummer = 203
AND wk1.name = wk2.name;