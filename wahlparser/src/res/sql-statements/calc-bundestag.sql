-- höchstzahlverfahren für sitzkontingente der länder (1. oberverteilung)
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

  -- höchstzahlverfahren für sitzkontingente der länder (1. oberverteilung)
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

  -- sitzkontingente der länder (1. oberverteilung)
    sitzeproland (bundesland, sitze, wahljahr) AS (
      SELECT
        bundesland,
        count(*),
        wahljahr
      FROM top598s
      GROUP BY bundesland, wahljahr
  ),

  -- anzahl der direktmandate pro partei und wahljahr bundesweit
  anzahldirektmandate (id, wahljahr, anzahldirkan) AS (
      SELECT
        p.id,
        p.wahljahr,
        count(DISTINCT k.id)
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
  ),

  -- parteien im bundestag = diejenigen parteien, die die 5% Hürde geschafft haben
  -- oder die mindestens drei direktmandate gewonnen haben
    parteienimbundestag AS (
    SELECT
      p.id,
      wahljahr
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
    SELECT id, wahljahr
    FROM anzahldirektmandate
    WHERE anzahldirkan >= 3
  ),

  -- anzahl zweitstimmen pro partei und bundesland
    zweitstimmen_btparteien (partei_id, stimmen, bundesland, wahljahr) AS (
      SELECT
        pbt.id,
        sum(anzahl) AS stimmen,
        wk.bundesland,
        pbt.wahljahr
      FROM parteienimbundestag pbt, zweitstimmenergebnisse z, wahlkreise wk
      WHERE pbt.id = z.partei_id
            AND z.wahlkreis_id = wk.id
      GROUP BY pbt.id, pbt.wahljahr, wk.bundesland
  ),

  -- höchstzahlverfahren verteilung der sitzkontigente auf parteien (1. unterverteilung)
    sitzepropartei_aux (partei_id, bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
    (
      SELECT
        partei_id,
        bundesland,
        0.5,
        stimmen,
        cast(stimmen AS NUMERIC) / 0.5,
        wahljahr
      FROM zweitstimmen_btparteien zp
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
      WHERE faktor <= (SELECT max(sitze)
                       FROM sitzeproland)
    )
  ),

  -- höchstzahlverfahren verteilung der sitzkontigente auf parteien (1. unterverteilung)
    sitzepropartei_sorted (partei_id, bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
      SELECT
        partei_id,
        bundesland,
        faktor,
        anzahl,
        aktuelles_ergebnis,
        wahljahr
      FROM
        (
          SELECT
            *,
            ROW_NUMBER()
            OVER (PARTITION BY
              bundesland, wahljahr
              ORDER BY aktuelles_ergebnis DESC) AS Row_ID
          FROM sitzepropartei_aux
        ) AS aux
      WHERE Row_ID <= (SELECT sitze
                       FROM sitzeproland sl
                       WHERE aux.bundesland = sl.bundesland
                             AND aux.wahljahr = sl.wahljahr)
      ORDER BY wahljahr ),

  -- verteilung der sitzkontigente auf parteien (1. unterverteilung)
    sitzepropartei (bundesland, partei_id, sitze, wahljahr) AS (
      SELECT
        bundesland,
        partei_id,
        count(*),
        wahljahr
      FROM sitzepropartei_sorted
      GROUP BY bundesland, wahljahr, partei_id
  ),

  -- mindestsitzzahl pro partei
  -- TODO ich glaube wir brauchen das doch nach ländern aufgeschlüsselt...
    mindestsitzzahl (partei_id, minsitzzahl, wahljahr) AS (
      SELECT partei_id,
        (case when sum(spp.sitze) > ad.anzahldirkan then sum(spp.sitze)
          else ad.anzahldirkan end),
        spp.wahljahr
      FROM sitzepropartei spp, anzahldirektmandate ad
      WHERE spp.partei_id = ad.id
      GROUP BY partei_id, spp.wahljahr, ad.anzahldirkan
  )


SELECT
  bundesland,
  partei_id,
  name,
  sitze,
  sitzepropartei.wahljahr
FROM sitzepropartei, parteien
WHERE sitzepropartei.partei_id = parteien.id
ORDER BY wahljahr, bundesland;

