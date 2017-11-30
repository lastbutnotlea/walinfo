-- später in q3-1!!

WITH waehler_pro_wahlkreis_erststimmen AS (
    SELECT
      ee.wahlkreis_id,
      sum(ee.anzahl) as summe_erststimmen,
      wahljahr
    FROM erststimmenergebnisse ee,  wahlkreise wk
    WHERE wk.id = ee.wahlkreis_id
    GROUP BY ee.wahlkreis_id, wahljahr
),

    waehler_pro_wahlkreis_zweitstimmen AS (
      SELECT
        ze.wahlkreis_id,
        sum(ze.anzahl) as summe_zweitstimmen,
        wahljahr
      FROM zweitstimmenergebnisse ze,  wahlkreise wk
      WHERE wk.id = ze.wahlkreis_id
      GROUP BY ze.wahlkreis_id, wahljahr
  )

SELECT
  wk.anzahl_wahlberechtigte,
  CAST(werst.summe_erststimmen AS NUMERIC) / CAST(wk.anzahl_wahlberechtigte AS NUMERIC) AS wahlbeteiligung_erststimmen,
  CAST(wzweit.summe_zweitstimmen AS NUMERIC) / CAST(wk.anzahl_wahlberechtigte AS NUMERIC) AS wahlbeteiligung_zweitstimmen,
  wk.wahljahr,
  CAST(werst.summe_erststimmen AS NUMERIC) / CAST(wk.anzahl_wahlberechtigte AS NUMERIC) -
  CAST(wzweit.summe_zweitstimmen AS NUMERIC) / CAST(wk.anzahl_wahlberechtigte AS NUMERIC) as test
FROM wahlkreise wk, waehler_pro_wahlkreis_erststimmen werst,
  waehler_pro_wahlkreis_zweitstimmen wzweit
WHERE wk.id = werst.wahlkreis_id
      AND wk.id = wzweit.wahlkreis_id;

WITH wahlberechtigte_bundesweit AS (
    SELECT sum(anzahl_wahlberechtigte) as wahlberechtigte,
      wahljahr
    FROM wahlkreise
    GROUP BY wahljahr
),

    wahlbeteiligung_zweitstimmen AS (
      SELECT sum(anzahl) as zweitstimmen, wahljahr
      FROM zweitstimmenergebnisse, wahlkreise
      WHERE zweitstimmenergebnisse.wahlkreis_id = wahlkreise.id
      GROUP BY wahljahr
  )
SELECT wb.wahlberechtigte, wz.zweitstimmen, wz.wahljahr,
  CASt(wz.zweitstimmen as NUMERIC) / CAST(wb.wahlberechtigte as NUMERIC) as wahlberechtigung
FROM wahlberechtigte_bundesweit wb, wahlbeteiligung_zweitstimmen wz
WHERE wb.wahljahr = wz.wahljahr;

-- oh wow das ist gar nicht so trivial wie ich dachte. wie sollen wir die
-- wahlbeteiligung berechnen wenn wir nicht wissen welche erststimme zu
-- welcher zweitstimme gehört???

-- unterschiede zwischen wahlbeteiligung in den erststimmen und wahlbeteiligung
-- in den zweitstimmen marginal -> können wir das einfach mitteln und gut ist?
-- aber ist das dann wirklich die wahlbeteiligung?