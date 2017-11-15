/*
CREATE VIEW erste_oberverteilung AS (
  WITH anfangsdivisor (anfangsdivisor, wahljahr) AS (
      SELECT sum(anzahl) / 598, wahljahr
      FROM dt_bevölkerung
      GROUP BY wahljahr
  ),

      anfangsverteilung (kuerzel, anzahl, ungerundet, gerundet, wahljahr) AS
    (
        SELECT
          kuerzel,
          anzahl,
          anzahl / anfangsdivisor,
          round(anzahl / anfangsdivisor),
          wahljahr
        FROM dt_bevölkerung, anfangsdivisor
        WHERE dt_bevölkerung.wahljahr = anfangsdivisor.wahljahr
    ),

      divisorkandidaten AS
    (
        SELECT *
        FROM anfangsverteilung
    )

  SELECT *
  FROM divisorkandidaten
);

*/

WITH RECURSIVE sitzeproland_aux (bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
  (
    SELECT
      bundesland,
      0.5,
      anzahl,
      cast(anzahl AS NUMERIC) / 0.5,
      wahljahr
    FROM dt_bevölkerung
  )
  UNION ALL
  (
      SELECT
        bundesland,
        faktor + 1,
        anzahl,
        anzahl / (faktor + 1),
        wahljahr
      FROM sitzeproland_aux
      where faktor < 600
  )
),

top598s (bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
    (
     SELECT *
     FROM sitzeproland_aux
     WHERE wahljahr = 2013
     ORDER BY aktuelles_ergebnis DESC
     LIMIT 598
    )
    UNION ALL
    (
      SELECT *
      FROM sitzeproland_aux
      WHERE wahljahr = 2017
      ORDER BY aktuelles_ergebnis DESC
      LIMIT 598
    )

  )

(
  SELECT
    bundesland,
    count(*)
  FROM top598s
  WHERE wahljahr = 2017
  GROUP BY bundesland
);
