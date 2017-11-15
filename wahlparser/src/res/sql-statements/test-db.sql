with test(bezeichnung, ist , soll) as (
  SELECT
    'Gültige Erststimmen 2017' ,
    sum(e.anzahl)              ,
    (46389615)
  FROM erststimmenergebnisse e, kandidaten k
  WHERE e.kandidaten_id = k.id
        AND k.wahljahr = 2017
        AND e.kandidaten_id IS NOT NULL

  UNION ALL

  SELECT
    'Gültige Erststimmen 2013',
    sum(e.anzahl),
    (43625042)
  FROM erststimmenergebnisse e, kandidaten k
  WHERE e.kandidaten_id = k.id
        AND k.wahljahr = 2013

  UNION ALL

  SELECT
    'Ungültige Erststimmen 2013',
    sum(e.anzahl),
    (684883)
  FROM erststimmenergebnisse e, wahlkreise w
  WHERE e.kandidaten_id IS NULL
        AND w.wahljahr = 2013
        AND w.id = e.wahlkreis_id

  UNION ALL

  SELECT
    'Ungültige Zweitstimmen 2017',
    sum(e.anzahl),
    (586726)
  FROM erststimmenergebnisse e, wahlkreise w
  WHERE e.kandidaten_id IS NULL
        AND w.wahljahr = 2017
        AND w.id = e.wahlkreis_id

  UNION ALL

    select 'Alle Stimmen 2013 & 2017', sum(e.anzahl), (44309925 + 46976341) from erststimmenergebnisse e

  UNION ALL
    SELECT
    'Gültige Zweitstimmen 2017' ,
    sum(e.anzahl)              ,
    (46515492)
    FROM zweitstimmenergebnisse e, parteien p
    WHERE e.partei_id = p.id
                          AND p.wahljahr = 2017

  UNION ALL

  SELECT
  'Gültige Zweitstimmen 2013',
  sum(e.anzahl),
  (43726856)
  FROM zweitstimmenergebnisse e, parteien p
  WHERE e.partei_id = p.id
        AND p.wahljahr = 2013

  UNION ALL

  SELECT
  'Ungültige Zweitstimmen 2013',
  sum(e.anzahl),
  (583069)
  FROM zweitstimmenergebnisse e, wahlkreise w
  WHERE e.partei_id IS NULL
                        AND w.wahljahr = 2013
                        AND w.id = e.wahlkreis_id

 UNION ALL

 SELECT
 'Ungültige Zweitstimmen 2017',
                              sum(e.anzahl),
  (460849)
  FROM zweitstimmenergebnisse e, wahlkreise w
  WHERE e.partei_id IS NULL
                        AND w.wahljahr = 2017
                        AND w.id = e.wahlkreis_id

    union all
  select 'CSU Erststimmen 2017', sum(e.anzahl), 12447656 from zweitstimmenergebnisse e, parteien p
  where p.id = e.partei_id
    and p.kuerzel = 'CDU'
    and p.wahljahr = 2017

  union ALL
    select 'Inkonsistenz Wahljahr: Kandidaten Wahlkreise', count(*), 0 FROM erststimmenergebnisse e,
      kandidaten k, wahlkreise w
      where k.id = e.kandidaten_id
        and e.wahlkreis_id = w.id
      and k.wahljahr <> w.wahljahr

  union ALL
  select 'Inkonsistenz Wahljahr: Parteien Wahlkreise', count(*), 0 FROM zweitstimmenergebnisse e,
    parteien p, wahlkreise w
  where p.id = e.partei_id
        and e.wahlkreis_id = w.id
        and p.wahljahr <> w.wahljahr

  union ALL
    select 'Aggregation korrekt Erststimmen 2017', count(*), 46389615 from erststimmen e
      where e.kandidaten_id is not null

  union ALL
    select 'Aggregation korrekt Zweitstimmen 2017', count(*), 46515492 from zweitstimmen e
      where e.partei_id is not null
)
  select case when soll = ist then 'OK' else 'ERROR' END, bezeichnung, soll, ist from test;