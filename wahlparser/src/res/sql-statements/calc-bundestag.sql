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


-- höchstzahlverfahren für sitze pro bundesland
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
    WHERE faktor < 600
  )
),

  -- höchstzahlverfahren für sitze pro bundesland
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
  ),

    sitzeproland (bundesland, sitze, wahljahr) AS (
      SELECT
        bundesland,
        count(*),
        wahljahr
      FROM top598s
      WHERE wahljahr = 2017
      GROUP BY bundesland, wahljahr
  ),

    parteienimbundestag AS (
      SELECT p.id, wahljahr
      FROM parteien p, zweitstimmenergebnisse z
      WHERE p.id = z.partei_id
      GROUP BY p.id, wahljahr
      HAVING sum(anzahl) >= 0.05 * (
        SELECT sum(anzahl)
        FROM zweitstimmenergebnisse z2, parteien p2
        WHERE z2.partei_id = p2.id
        AND p2.wahljahr = p.wahljahr
      )
      UNION
      SELECT p.id, p.wahljahr
      FROM parteien p, erststimmenergebnisse e, kandidaten k
      WHERE p.id = k.partei_id
        AND k.id = e.kandidaten_id
        AND k.wahljahr = p.wahljahr
        AND e.anzahl = (
        SELECT max(anzahl)
        FROM erststimmenergebnisse e2, kandidaten k2
        WHERE e2.kandidaten_id = k2.id
        AND k.wahljahr = k2.wahljahr
        AND e.wahlkreis_id = e2.wahlkreis_id
      )
      GROUP BY p.id, p.wahljahr
      HAVING count(DISTINCT k.id) >= 3
  ),

    zweitstimmen_btparteien AS (
      SELECT
        pbt.id,
        sum(anzahl),
        wk.bundesland,
        pbt.wahljahr
      FROM parteienimbundestag pbt, zweitstimmenergebnisse z, wahlkreise wk
      WHERE pbt.id = z.partei_id
        AND z.wahlkreis_id = wk.id
      GROUP BY pbt.id, pbt.wahljahr
  ),

    -- höchstzahlverfahren für sitze pro partei pro bundesland
    sitzepropartei_aux (partei_id, bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
    (
      SELECT
        zp.id,
        sl.bundesland,
        0.5,
        sl.sitze,
        cast(sl.sitze AS NUMERIC) / 0.5,
        sl.wahljahr
      FROM zweitstimmen_btparteien zp, sitzeproland sl
      WHERE sl.bundesland = zp.bundesland
    )
    UNION ALL
    (
      SELECT
        partei_id,
        bundesland,
        faktor + 1,
        anzahl,
        anzahl / (faktor + 1),
        wahljahr
      FROM sitzepropartei_aux
      WHERE faktor < anzahl
    )
  ),

  -- höchstzahlverfahren für sitze pro bundesland
  -- TODO, nicht fertig!!
    sitzepropartei_sorted (bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
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

SELECT *, name
FROM parteienimbundestag, parteien
WHERE parteienimbundestag.id = parteien.id
ORDER BY parteienimbundestag.wahljahr;