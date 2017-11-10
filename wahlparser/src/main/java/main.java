import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.*;
import java.util.Map;

public class main {

    public static void main(String[]args){
        Datamodel data = new Datamodel();

        // Parse bundeslaender Daten
        parseBundeslaender(data);

        // Parse Parteien Kurzschreibweisen
        parseKurzschreibweisenParteien(data);

        parseWahldaten(data);

        // Parse Bewerber 2017
        parseBewerber2017(data);

        // Parse Bewerber 2013
        parseBewerber2013(data);

        // checkWahlkreise2013(data); ist falsch

        System.out.println("SQL:\n");
        PrintStream sqlFileStream;

        try {
            sqlFileStream = new PrintStream(new FileOutputStream(
                    new File("wahlparser/src/res/sql-statements/insert-statements.sql")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Generating SQL");
        //Print SQL Insert Statements
        generateInsertSqlFile(data, sqlFileStream);

        System.out.println("Done with inserts\n\nStarting with Wahlzettel");

//        StimmGenerator generator = new StimmGenerator(data);
//        try {
//             generator.writeInsertStatements(new File("wahlparser/src/res/sql-statements/insert-wahlzettel.sql"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("Done");
    }

    private static void generateInsertSqlFile(Datamodel data, PrintStream sqlFileStream) {
        // Bundesländer
        for (Bundesland land : data.laender) {
            sqlFileStream.println("INSERT INTO Bundeslaender (kuerzel, name) VALUES ('" +
                    land.getKurzschreibweise() + "', '" + land.getName() + "');");
        }

        sqlFileStream.println();

        // Parteien
        for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
            for (Partei partei : data.parteien.get(jahr)) {
                sqlFileStream.println("INSERT INTO Parteien (id, kuerzel, name, wahljahr) VALUES (" +
                        partei.getNumber() + ", '" + partei.getKurzschreibweise() + "', '" + partei.getName() + "', " +
                        jahr.getKurzschreibweiseLang() + ");");
            }
        }

        sqlFileStream.println();

        // Wahlkreise
            for (Wahlkreis kreis : data.wahlkreise.get(data.wahl2017)) {
                sqlFileStream.println("INSERT INTO Wahlkreise (nummer, name, bundesland) VALUES (" +
                        kreis.getNummer() + ", '" + kreis.getName() + "', '" +
                        kreis.getLand().getKurzschreibweise() + "');");
                for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
                    int anzahl = 0;
                    for (Wahlkreis k : data.wahlkreise.get(jahr)) {
                        if (k.getNummer() == kreis.getNummer()) {
                            anzahl = k.getWahlberechtigte();
                        }
                    }
                    if (anzahl == 0) {
                        throw  new RuntimeException();
                    }
                    sqlFileStream.println("INSERT INTO Wahlberechtigte (wahlkreis_id, wahljahr, anzahl_wahlberechtigte) VALUES (" +
                            kreis.getNummer() + ", " + jahr.getKurzschreibweiseLang() + ", " + anzahl + ");");
                }
            }

        sqlFileStream.println();

        // Kandidaten
        for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
            for (Bewerber bewe : data.bewerber.get(jahr)) {
                sqlFileStream.println("INSERT INTO Kandidaten (id, name, vorname, titel, namenszusatz, geburtsjahr, beruf, geschlecht, partei_id, wahlkreis_id, wahljahr) VALUES (" +
                    bewe.getId() + ", '" + bewe.getNachname() + "', '" + bewe.getVorname() + "', " +
                        (bewe.getTitel() != null && !bewe.getTitel().isEmpty() ? "'" + bewe.getTitel() + "'" : null) + ", " +
                        (bewe.getNamenszusatz() != null && !bewe.getNamenszusatz().isEmpty() ? "'" + bewe.getNamenszusatz() + "'" : null) + ", " + bewe.getJahrgang() + ", " +
                        (bewe.getBeruf() != null && !bewe.getBeruf().isEmpty() ? "'" + bewe.getBeruf() + "'" : null) + ", " +
                        (bewe.getGeschlecht() != null ? "'" + bewe.getGeschlecht() + "'" : null) +
                        ", " + (bewe.getPartei() != null ? bewe.getPartei().getNumber() : null) + ", " +
                        (bewe.getWahlkreis() != null ? bewe.getWahlkreis().getNummer() : null)
                        + ", " + jahr.getKurzschreibweiseLang() + ");");

                for (Partei partei : data.parteien.get(jahr)) {
                    for (Map.Entry<Bundesland, Landesliste> entry : partei.getLandeslisten().entrySet()) {
                        for (Map.Entry<Integer, Bewerber> entry2 : entry.getValue().getEintraege().entrySet()) {
                            if (entry2.getValue() == bewe) {
                                sqlFileStream.println("INSERT INTO Listenplaetze (kandidaten_id, bundesland, listenplatz) VALUES (" +
                                        bewe.getId() + ", '" + entry.getKey().getKurzschreibweise() + "', " + entry2.getKey() + ");");
                            }
                        }
                    }
                }
            }
        }

        sqlFileStream.println();

        // Zweitstimmenergebnisse
        int anzahl = 0;
        for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
            for (Wahlkreis kreis : data.wahlkreise.get(jahr)) {
                for (WahlkreisErgebnis ergebnis : kreis.getErgebnisse()) {
                    if (ergebnis.getPartei() == null) {
                        anzahl++;
                    } else {
                        sqlFileStream.println("INSERT INTO Zweitstimmenergebnisse (partei_id, wahlkreis_id, anzahl) VALUES (" +
                                ergebnis.getPartei().getNumber() + ", " + kreis.getNummer() + ", " + ergebnis.getZweitstimmen() + ");");
                    }
                }
            }
        }

        // Erststimmenergebnis
        for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
            for (Wahlkreis kreis : data.wahlkreise.get(jahr)) {
                for (WahlkreisErgebnis ergebnis : kreis.getErgebnisse()) {
                    for (Bewerber bewe : data.bewerber.get(jahr)) {
                        if (bewe.getWahlkreis() == kreis && bewe.getPartei() == ergebnis.getPartei()) {
                            sqlFileStream.println("INSERT INTO Erststimmenergebnisse (kandidaten_id, wahlkreis_id, anzahl) VALUES (" +
                                bewe.getId() + ", " + kreis.getNummer() + ", " + ergebnis.getAnzahlErststimmen() + ");");
                        }
                    }
                }
            }
        }
    }

    private static void checkWahlkreise2013(Datamodel data) {
        try (Reader reader = new FileReader(new File("wahlparser/src/res/Wahldaten/Wahlkreise.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(',').withFirstRecordAsHeader());

            for (CSVRecord record : parser) {
                Integer wahlkreisnummer = getInt(record.get("Wahlkreisnummer"));
                String wahlkreisname = record.get("Wahlkreisname");
                String bundesland = record.get("Bundesland");

                Wahlkreis kreis = data.getWahlkreis(data.wahl2013, wahlkreisnummer);

                kreis.setLand(data.getBundesland(bundesland));
                kreis.setName(wahlkreisname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseBundeslaender(Datamodel data) {
        try (Reader reader = new FileReader(new File("wahlparser/src/res/bundeslaender.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(';'));

            for (CSVRecord record : parser) {
                String kurz = record.get(0);
                String lang = record.get(1);

                data.laender.add(new Bundesland(kurz, lang));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseKurzschreibweisenParteien(Datamodel data) {
        try (Reader reader = new FileReader(new File("wahlparser/src/res/parteien_liste.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(';'));

            for (CSVRecord record : parser) {
                String kurz = record.get(0);
                String lang = record.get(1);

                data.parteienKurzschreibweise.put(lang, kurz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseWahldaten(Datamodel data) {
        try (FileReader fileReader = new FileReader(new File("wahlparser/src/res/complete.json"))) {

            JsonObject mainObject = Json.createReader(fileReader).readObject();

            JsonObject wahlkreiseJson = mainObject.getJsonObject("wahlkreise");
            for (Wahljahr jahr : new Wahljahr[] {data.wahl2013, data.wahl2017}) {
                for (int i = 1; i <= 299; i++) {
                    JsonObject wahlkreisJson = wahlkreiseJson.getJsonObject(Integer.toString(i));
                    Wahlkreis wahlkreis = new Wahlkreis(
                            i,
                            wahlkreisJson.getString("Name"),
                            data.getBundesland(wahlkreisJson.getString("Bundesland")),
                            wahlkreisJson.getInt("Wahlberechtigte_" + jahr.getKurzschreibweise()),
                            wahlkreisJson.getInt("Waehler_" + jahr.getKurzschreibweise()),
                            wahlkreisJson.getInt("Gueltige_" + jahr.getKurzschreibweise() + "_Erst"),
                            wahlkreisJson.getInt("Ungueltige_" + jahr.getKurzschreibweise() + "_Erst"),
                            wahlkreisJson.getInt("Gueltige_" + jahr.getKurzschreibweise() + "_Erst"),
                            wahlkreisJson.getInt("Ungueltige_" + jahr.getKurzschreibweise() + "_Erst")
                    );
                    data.wahlkreise.get(jahr).add(wahlkreis);

                    JsonArray ergebnisse = wahlkreisJson.getJsonArray("ParteiErgebnisse");
                    for (int j = 0; j < ergebnisse.size(); j++) {
                        JsonObject ergebnisJson = ergebnisse.getJsonObject(j);
                        String parteiName = ergebnisJson.getString("Partei");
                        wahlkreis.getErgebnisse().add(new WahlkreisErgebnis(data.getCreatePartei(parteiName, null, jahr),
                                ergebnisJson.getInt("Erststimme_" + jahr.getKurzschreibweise()),
                                ergebnisJson.getInt("Zweitstimme_" + jahr.getKurzschreibweise())));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseBewerber2013(Datamodel data) {
        try (Reader reader = new FileReader(new File("wahlparser/src/res/Wahldaten/wahlbewerber2013_mit_platz.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader());

            for (CSVRecord record : parser) {
                String titel = record.get("Titel");
                String nachname = record.get("Nachname");
                String vorname = record.get("Vorname");
                int jahrgang = getInt(record.get("Jahrgang"));
                String parteikurz = record.get("Partei");
                Integer wahlkreis = getInt(record.get("Wahlkreis"));
                String landKurz = record.get("Bundesland");
                Integer listenEintrag = getInt(record.get("Listenplatz"));

                Partei partei = null;
                if (!parteikurz.isEmpty()) {
                    partei = data.getByKuerzel(parteikurz, data.wahl2013);
                }
                if (landKurz.isEmpty()) {
                    landKurz = null;
                }

                Bewerber bewerber = new Bewerber(titel, nachname, vorname, null, null, null, jahrgang,
                        wahlkreis != null ? data.getWahlkreis(data.wahl2013, wahlkreis) : null,
                        partei);

                if (landKurz != null) {
                    Bundesland land = data.getBundeslandByKuerzel(landKurz);
                    partei.addLandeslisteEintrag(land, bewerber, listenEintrag);
                }

                data.bewerber.get(data.wahl2013).add(bewerber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseBewerber2017(Datamodel data) {
        try (Reader reader = new FileReader(new File("wahlparser/src/res/btw17_kandidaten_utf8-korr2.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader());

            for (CSVRecord record : parser) {
                String titel = record.get("Titel");
                String namenszusatz = record.get("Namenszusatz");
                String nachname = record.get("Name");
                String vorname = record.get("Vorname");
                int jahrgang = getInt(record.get("Geburtsjahr"));
                String parteiBez = record.get("Wahlkreis_ParteiBez");
                String parteiKurz = record.get("Wahlkreis_ParteiKurzBez");
                Integer wahlkreis = getInt(record.get("Wahlkreis_Nr"));
                String landKurz = record.get("Liste_Land");
                Integer listenEintrag = getInt(record.get("Liste_Platz"));
                String geschlecht = record.get("Geschlecht");
                String beruf = record.get("Beruf");
                String parteiListe = record.get("Liste_ParteiBez");

                Partei partei = null;
                if (!parteiBez.isEmpty()) {
                    partei = data.getCreatePartei(parteiBez, parteiKurz, data.wahl2017);
                }

                // Neuen Bewerber anlegen
                Bewerber bewerber = new Bewerber(titel, nachname, vorname, namenszusatz, geschlecht, beruf, jahrgang,
                        wahlkreis != null ? data.getWahlkreis(data.wahl2017, wahlkreis) : null,
                        partei);

                // Dieser Bewerber steht auf einer Landesliste
                if (!parteiListe.isEmpty()) {
                    Partei landesPartei = data.getCreatePartei(parteiListe, record.get("Liste_ParteiKurzBez"), data.wahl2017);
                    landesPartei.addLandeslisteEintrag(data.getBundeslandByKuerzel(landKurz), bewerber, listenEintrag);
                }

                data.bewerber.get(data.wahl2017).add(bewerber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer getInt(String zahl) {
        Integer re = null;
        try {
            re = Integer.parseInt(zahl);
        } catch (NumberFormatException x) {

        }
        return re;
    }
}

