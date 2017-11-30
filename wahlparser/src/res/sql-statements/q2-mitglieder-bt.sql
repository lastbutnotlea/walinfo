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
            OVER (PARTITION BY
              bundesland, wahljahr
              ORDER BY aktuelles_ergebnis DESC) AS anzahl_reihen
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
            OVER (PARTITION BY
              wahljahr, partei_id
              ORDER BY aktuelles_ergebnis DESC) AS anzahl_reihen
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
            OVER (PARTITION BY
              partei_id, wahljahr
              ORDER BY aktuelles_ergebnis DESC) AS anzahl_reihen
          FROM ausgleichsmandate_aux
        ) AS aux
      WHERE aktuelles_ergebnis >= (
        SELECT min_ergebnis
        FROM ausgleichsmandate_aux3 aa3
        WHERE aa3.wahljahr = aux.wahljahr
      )
      GROUP BY partei_id, wahljahr
      ORDER BY wahljahr ),

    direktkandidaten_bt_parteiland_counter (wahljahr, kandidat_id, partei_id, bundesland, counter) AS (
      SELECT
        k.wahljahr,
        kandidat_id,
        partei_id,
        bundesland,
        ROW_NUMBER()
        OVER (PARTITION BY k.partei_id, w.bundesland)
      FROM gewaehlte_erstkandidaten_schnell gk, kandidaten k, wahlkreise w
      WHERE gk.kandidat_id = k.id
            AND w.id = k.wahlkreis_id
  ),

  -- um kandidaten von den listen zu filtern, die es schon längst geschafft haben
    kandidaten_liste_partei_land(kandidaten_id, partei_id, listenplatz, bundesland ) AS (
      SELECT
        l.kandidaten_id,
        l.partei_id,
        ROW_NUMBER()
        OVER (PARTITION BY l.partei_id, l.bundesland
          ORDER BY l.listenplatz ASC),
        l.bundesland
      FROM listenplaetze l
      WHERE NOT EXISTS(
          SELECT *
          FROM gewaehlte_erstkandidaten_schnell gk
          WHERE gk.kandidat_id = l.kandidaten_id
      )
  ),

    bundestag_hoechstzahlverfahren (kandidat_id, partei_id, bundesland, faktor, max_stimmen, anzahl, counterErst, counterListe, wahljahr) AS (
    (
      SELECT
        -- nimm einen Direktkandidaten
        COALESCE(
            (
              SELECT dc.kandidat_id
              FROM direktkandidaten_bt_parteiland_counter dc
              WHERE dc.partei_id = zp.partei_id
                    AND dc.bundesland = zp.bundesland
                    AND dc.counter = 1
                    AND dc.wahljahr = zp.wahljahr
            ),
            -- oder einen Listenkanidaten
            (
              SELECT kl.kandidaten_id
              FROM kandidaten_liste_partei_land kl
              WHERE kl.partei_id = zp.partei_id --wahljahr steckt in der partei
                    AND listenplatz = 1
                    AND kl.bundesland = zp.bundesland
            )
        ),
        partei_id,
        bundesland,
        0.5,
        stimmen,
        -- im ersten durchlauf ist die prio immer maximal
        CAST(stimmen AS NUMERIC) / 0.5,
        -- wähle Direktkandidaten hoch
        CASE WHEN EXISTS(
            SELECT dc.kandidat_id
            FROM direktkandidaten_bt_parteiland_counter dc
            WHERE dc.partei_id = zp.partei_id
                  AND dc.bundesland = zp.bundesland
                  AND dc.counter = 1
                  AND dc.wahljahr = zp.wahljahr
        )
          THEN 2
        ELSE 1 END,
        -- oder Listenkandidaten
        CASE WHEN EXISTS(
            SELECT dc.kandidat_id
            FROM direktkandidaten_bt_parteiland_counter dc
            WHERE dc.partei_id = zp.partei_id
                  AND dc.bundesland = zp.bundesland
                  AND dc.counter = 1
                  AND dc.wahljahr = zp.wahljahr
        )
          THEN 1
        ELSE 2 END,
        wahljahr
      FROM zweitstimmen_btparteien zp
    )
    UNION ALL -- jetzt dasselbe nur rekursiv
    (
      SELECT
        COALESCE(
            (
              SELECT dc.kandidat_id
              FROM direktkandidaten_bt_parteiland_counter dc
              WHERE dc.partei_id = me.partei_id
                    AND dc.bundesland = me.bundesland
                    AND dc.wahljahr = me.wahljahr
                    AND dc.counter = me.counterErst
            ),
            (
              SELECT kl.kandidaten_id
              FROM kandidaten_liste_partei_land kl
              WHERE kl.partei_id = me.partei_id --wahljahr steckt in der partei
                    AND listenplatz = me.counterListe
                    AND kl.bundesland = me.bundesland
            )
        ),
        partei_id,
        bundesland,
        faktor + 1,
        max_stimmen,
        -- maximale prio für Direktkandidaten, sonst normale prio
        CASE WHEN EXISTS(
            SELECT dc.kandidat_id
            FROM direktkandidaten_bt_parteiland_counter dc
            WHERE dc.partei_id = me.partei_id
                  AND dc.bundesland = me.bundesland
                  AND dc.wahljahr = me.wahljahr
                  AND dc.counter = me.counterErst
        )
          THEN CAST(max_stimmen AS NUMERIC) / 0.5
        ELSE CAST(max_stimmen AS NUMERIC) / (faktor + 1) END,
        CASE WHEN EXISTS(
            SELECT dc.kandidat_id
            FROM direktkandidaten_bt_parteiland_counter dc
            WHERE dc.partei_id = me.partei_id
                  AND dc.bundesland = me.bundesland
                  AND dc.wahljahr = me.wahljahr
                  AND dc.counter = me.counterErst
        )
          THEN counterErst + 1
        ELSE counterErst END,
        CASE WHEN EXISTS(
            SELECT dc.kandidat_id
            FROM direktkandidaten_bt_parteiland_counter dc
            WHERE dc.partei_id = me.partei_id
                  AND dc.bundesland = me.bundesland
                  AND dc.wahljahr = me.wahljahr
                  AND dc.counter = me.counterErst
        )
          THEN counterListe
        ELSE counterListe + 1 END,
        wahljahr
      FROM bundestag_hoechstzahlverfahren me
      WHERE faktor <= (SELECT max(sitze)
                       FROM sitzeproland)
    )
  ),

  -- nur um die row number zu bekommen
    bundestag_aux (kandidat_id, partei_id, bundesland, wahljahr, rownum) AS (
      SELECT
        kandidat_id,
        partei_id,
        bundesland,
        wahljahr,
        ROW_NUMBER()
        OVER (PARTITION BY partei_id, wahljahr
          ORDER BY anzahl DESC)
      FROM bundestag_hoechstzahlverfahren
  ),

  -- der finale bundestag
    bundestag (kandidat_id, partei_id, bundesland, wahljahr) AS (
      SELECT
        kandidat_id,
        partei_id,
        bundesland,
        wahljahr
      FROM bundestag_aux aux
      WHERE rownum <= (
        SELECT anzahl_mandate
        FROM mandate_pro_partei
        WHERE wahljahr = aux.wahljahr
              AND aux.partei_id = mandate_pro_partei.partei_id
      )
  )

--------------------------------------

SELECT k.titel, k.name, k.vorname, k.namenszusatz, k.geburtsjahr, p.kuerzel
FROM bundestag bt, kandidaten k, parteien p
WHERE bt.kandidat_id = k.id
  AND bt.partei_id = p.id;

