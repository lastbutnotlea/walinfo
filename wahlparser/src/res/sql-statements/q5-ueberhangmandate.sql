-- ÜBERHANGMANDATE


-- höchstzahlverfahren für sitzkontingente der länder (1. oberverteilung)
WITH RECURSIVE sitzeproland_aux (bundesland, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
  (
    SELECT
      bundesland,
      0.5,
      anzahl,
      cast(anzahl AS NUMERIC) / 0.5,
      wahljahr
    FROM dt_bevoelkerung
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

  -- anzahl der direktmandate pro partei und wahljahr pro bundesland
  /*
      anzahldirektmandate_land (partei_id, bundesland, wahljahr, anzahldirkan) AS (
        SELECT
          p.id,
          wk.bundesland,
          p.wahljahr,
          count(DISTINCT k.id)
        FROM parteien p, erststimmenergebnisse e, kandidaten k, wahlkreise wk
        WHERE p.id = k.partei_id
              AND k.id = e.kandidaten_id
              AND k.wahljahr = p.wahljahr
              AND e.wahlkreis_id = wk.id
              AND e.anzahl = (
          SELECT max(anzahl)
          FROM erststimmenergebnisse e2, kandidaten k2
          WHERE e2.kandidaten_id = k2.id
                AND k.wahljahr = k2.wahljahr
                AND e.wahlkreis_id = e2.wahlkreis_id
        )
        GROUP BY p.id, wk.bundesland, p.wahljahr
    ),
    */

    anzahldirektmandate_land (partei_id, bundesland, wahljahr, anzahldirkan) AS (
      SELECT
        p.id,
        wk.bundesland,
        ge.wahljahr,
        count(*)
      FROM Parteien p, kandidaten k, wahlkreise wk, gewaehlte_erstkandidaten_schnell ge
      WHERE ge.kandidat_id = k.id
            AND k.wahlkreis_id = wk.id
            AND k.partei_id = p.id
      GROUP BY wk.bundesland, ge.wahljahr, p.id ),

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
    SELECT
      partei_id,
      wahljahr
    FROM (SELECT
            partei_id,
            wahljahr,
            sum(anzahldirkan) AS sumdirkan
          FROM anzahldirektmandate_land
          GROUP BY partei_id, wahljahr
         ) AS TEMP
    WHERE sumdirkan >= 3
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
        CAST(stimmen AS NUMERIC) / 0.5,
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
            OVER (
              PARTITION BY
                bundesland, wahljahr
              ORDER BY aktuelles_ergebnis DESC ) AS anzahl_reihen
          FROM sitzepropartei_aux
        ) AS aux
      WHERE anzahl_reihen <= (SELECT sitze
                              FROM sitzeproland sl
                              WHERE aux.bundesland = sl.bundesland
                                    AND aux.wahljahr = sl.wahljahr)
      ORDER BY wahljahr ),

  -- verteilung der sitzkontigente auf parteien (1. unterverteilung)
    sitzepropartei_land (bundesland, partei_id, sitze, wahljahr) AS (
      SELECT
        bundesland,
        partei_id,
        count(*),
        wahljahr
      FROM sitzepropartei_sorted
      GROUP BY bundesland, wahljahr, partei_id
  ),

  -- mindestsitzzahl pro partei auf landesebene
    mindestsitzzahl_land (partei_id, bundesland, minsitzzahl, wahljahr) AS (
      SELECT
        spp.partei_id,
        spp.bundesland,
        greatest(sitze, (
          SELECT anzahldirkan
          FROM anzahldirektmandate_land dir
          WHERE dir.partei_id = spp.partei_id
                AND dir.bundesland = spp.bundesland
        )),
        spp.wahljahr
      FROM sitzepropartei_land spp
  ),

  -- mindestsitzzahl pro partei auf bundesebene
    mindestsitzzahl (partei_id, minsitzzahl, wahljahr) AS (
      SELECT
        partei_id,
        sum(minsitzzahl),
        wahljahr
      FROM mindestsitzzahl_land
      GROUP BY partei_id, wahljahr
  ),

  -- höchstzahlverfahren ausgleichsmandate (2. oberverteilung)
  -- erstelle höchstzahltabelle
    ausgleichsmandate_aux (partei_id, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
    (
      SELECT
        partei_id,
        0.5,
        sum(stimmen),
        CAST(sum(stimmen) AS NUMERIC) / 0.5,
        wahljahr
      FROM zweitstimmen_btparteien
      GROUP BY partei_id, wahljahr
    )
    UNION ALL
    (
      SELECT
        partei_id,
        faktor + 1,
        anzahl,
        anzahl / (faktor + 1),
        wahljahr
      FROM ausgleichsmandate_aux
      -- reicht auch das max an mindestsitzzahlen???
      WHERE faktor < 600
    )
  ),

  -- höchstzahlverfahren ausgleichsmandate (2. oberverteilung)
  -- wähle von jeder partei die größten x einträge, sodass x der mindestsitzzahl entspricht
    ausgleichsmandate_aux2 (partei_id, faktor, anzahl, aktuelles_ergebnis, wahljahr) AS (
      SELECT
        partei_id,
        faktor,
        anzahl,
        aktuelles_ergebnis,
        wahljahr
      FROM
        (
          SELECT
            *,
            ROW_NUMBER()
            OVER (
              PARTITION BY
                wahljahr, partei_id
              ORDER BY aktuelles_ergebnis DESC ) AS anzahl_reihen
          FROM ausgleichsmandate_aux
        ) AS aux
      WHERE anzahl_reihen <= (SELECT minsitzzahl
                              FROM mindestsitzzahl ms
                              WHERE ms.partei_id = aux.partei_id
                                    AND ms.wahljahr = aux.wahljahr)
      ORDER BY wahljahr
  ),

  -- höchstzahlverfahren ausgleichsmandate (2. oberverteilung)
  -- suche für jedes wahljahr den kleinsten eintrag in der sortierten und
  -- gefilterten höchstzahltabelle
    ausgleichsmandate_aux3 (partei_id, min_ergebnis, wahljahr) AS (
      SELECT
        partei_id,
        aktuelles_ergebnis,
        wahljahr
      FROM ausgleichsmandate_aux2 aa1
      WHERE aktuelles_ergebnis = (
        SELECT min(aktuelles_ergebnis)
        FROM ausgleichsmandate_aux2 aa2
        WHERE aa1.wahljahr = aa2.wahljahr
      )
  ),

  -- höchstzahlverfahren ausgleichsmandate (2. oberverteilung)
    mandate_pro_partei (partei_id, anzahl_mandate, wahljahr) AS (
      SELECT
        partei_id,
        count(anzahl_reihen),
        wahljahr
      FROM
        (
          SELECT
            *,
            ROW_NUMBER()
            OVER (
              PARTITION BY
                partei_id, wahljahr
              ORDER BY aktuelles_ergebnis DESC ) AS anzahl_reihen
          FROM ausgleichsmandate_aux
        ) AS aux
      WHERE aktuelles_ergebnis >= (
        SELECT min_ergebnis
        FROM ausgleichsmandate_aux3 aa3
        WHERE aa3.wahljahr = aux.wahljahr
      )
      GROUP BY partei_id, wahljahr
      ORDER BY wahljahr )

SELECT
  s.bundesland,
  b.name,
  p.kuerzel,
  p.name,
  p.farbe,
  mins.minsitzzahl - s.sitze AS ueberhandmandate
FROM sitzepropartei_land s, mindestsitzzahl_land mins, bundeslaender b, parteien p
WHERE s.bundesland = mins.bundesland
  AND s.partei_id = mins.partei_id
  AND s.wahljahr = mins.wahljahr
AND b.kuerzel = s.bundesland
AND p.id = s.partei_id
AND mins.minsitzzahl - s.sitze > 0
ORDER BY p.name, s.bundesland;

