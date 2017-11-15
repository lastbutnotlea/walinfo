import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.*;
import java.util.*;

public class main {

    public static void main(String[]args){
        Datamodel data = new Datamodel();

        // Parse bundeslaender Daten
        parseBundeslaender(data);

        // Parse Parteien Kurzschreibweisen
        parseKurzschreibweisenParteien(data);

        // Für 2017
        parseWahldatenJson(data);

        // Für 2013
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
        for (Partei partei : data.parteien) {
            sqlFileStream.println("INSERT INTO Parteien (id, kuerzel, name, wahljahr) VALUES (" +
                    partei.getNumber() + ", '" + partei.getKurzschreibweise() + "', '" + partei.getName() + "', " + partei.getJahr().getKurzschreibweiseLang() + ");");
        }

        sqlFileStream.println();

        // Wahlkreise
        for (Wahlkreis kreis : data.wahlkreise) {
            sqlFileStream.println("INSERT INTO Wahlkreise (id, nummer, name, bundesland, anzahl_wahlberechtigte, wahljahr) VALUES (" +
                    kreis.getId() + ", " + kreis.getNummer() + ", '" + kreis.getName() + "', '" +
                    kreis.getLand().getKurzschreibweise() + "',"
                    + kreis.getWahlberechtigte() + ", " + kreis.getJahr().getKurzschreibweiseLang() + ");");

            sqlFileStream.println("INSERT INTO Zweitstimmenergebnisse(wahlkreis_id, anzahl) VALUES (" +
                kreis.getId() + "," + kreis.getUngueltigeStimmenZweit() + ");");

            sqlFileStream.println("INSERT INTO Erststimmenergebnisse(wahlkreis_id, anzahl) VALUES (" +
                    kreis.getId() + ", " + kreis.getUngueltigeStimmenErst() + ");");
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
                        (bewe.getWahlkreis() != null ? bewe.getWahlkreis().getId() : null)
                        + ", " + jahr.getKurzschreibweiseLang() + ");");

                for (Partei partei : data.parteien) {
                    for (Landesliste liste : partei.getLandeslisten()) {
                        for (Map.Entry<Integer, Bewerber> entry2 : liste.getEintraege().entrySet()) {
                            if (entry2.getValue() == bewe) {
                                sqlFileStream.println("INSERT INTO Listenplaetze (kandidaten_id, bundesland, listenplatz) VALUES (" +
                                        bewe.getId() + ", '" + liste.getLand().getKurzschreibweise() + "', " + entry2.getKey() + ");");
                            }
                        }
                    }
                }
            }
        }

        sqlFileStream.println();

        // Zweitstimmenergebnisse
        for (Wahlkreis kreis : data.wahlkreise) {
            for (WahlkreisErgebnis ergebnis : kreis.getErgebnisse()) {
                if (ergebnis.getZweitstimmen() == 0) {
                    continue;
                }
                if (ergebnis.getPartei() == null) {
                    if (ergebnis.getZweitstimmen() > 0) {
                        throw new RuntimeException("Anscheinend gibt es Zweitstimmen, die keiner PArtei gehören. Sollte nicht sein");
                    }
                } else {
                    sqlFileStream.println("INSERT INTO Zweitstimmenergebnisse (partei_id, wahlkreis_id, anzahl) VALUES (" +
                            ergebnis.getPartei().getNumber() + ", " + kreis.getId() + ", " + ergebnis.getZweitstimmen() + ");");
                }
            }
        }

        // Erststimmenergebnis
        for (Wahlkreis kreis : data.wahlkreise) {
            for (WahlkreisErgebnis ergebnis : kreis.getErgebnisse()) {
                if (ergebnis.getAnzahlErststimmen() == 0) {
                    continue;
                }
                if (ergebnis.getJahr() == data.wahl2013
                        && kreis.getId() == 521
                        && ergebnis.getPartei() == null) {
                    ergebnis.setPartei(data.getByKuerzel("AfD", data.wahl2013));
                }
                if (ergebnis.getPartei() == null) {
                    List<Bewerber> alleParteilosen = new ArrayList<>();
                    Map<Bewerber, Integer> alleParteilosenStimmen = new HashMap<>();
                    for (Bewerber bewerber : data.bewerber.get(ergebnis.getJahr())) {
                        if (bewerber.getWahlkreis() == kreis && bewerber.getPartei() == null) {
                            alleParteilosen.add(bewerber);
                            alleParteilosenStimmen.put(bewerber, 0);
                        }
                    }
                    if (alleParteilosen.isEmpty()) {
                        if (ergebnis.getJahr() == data.wahl2017) {
                            System.out.println("Sollte nicht sein: " + ergebnis.getAnzahlErststimmen());
                        } else {
                            System.out.println("Konnte folgende Stimmen nicht zuordnen: " + ergebnis.getAnzahlErststimmen());
                        }
                        continue;
                    }
                    int stimmen = ergebnis.getAnzahlErststimmen();
                    while (stimmen > 0) {
                        for (Bewerber bewe : alleParteilosen) {
                            int anzahl = alleParteilosenStimmen.get(bewe);
                            alleParteilosenStimmen.put(bewe, anzahl + 1);
                            stimmen--;
                        }
                    }
                    for (Bewerber bewe : alleParteilosen) {
                        sqlFileStream.println("INSERT INTO Erststimmenergebnisse (kandidaten_id, wahlkreis_id, anzahl) VALUES (" +
                                bewe.getId() + ", " + kreis.getId() + ", " + alleParteilosenStimmen.get(bewe) + ");");
                    }
                } else {
                    boolean found = false;
                    for (Bewerber bewe : data.bewerber.get(ergebnis.getJahr())) {
                        if (bewe.getWahlkreis() == kreis && bewe.getPartei() == ergebnis.getPartei()) {
                            sqlFileStream.println("INSERT INTO Erststimmenergebnisse (kandidaten_id, wahlkreis_id, anzahl) VALUES (" +
                                    bewe.getId() + ", " + kreis.getId() + ", " + ergebnis.getAnzahlErststimmen() + ");");
                            found = true;
                        }
                    }
                    if (!found) {
                        if (ergebnis.getJahr() == data.wahl2017) {
                            System.out.println("Sollte nicht sein: " + ergebnis.getAnzahlErststimmen());
                        } else {
                            System.out.println("Konnte folgende Stimmen nicht zuordnen: " + ergebnis.getAnzahlErststimmen());
                        }
                    }
                }
            }
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
        try (Reader reader = new FileReader(new File("wahlparser/src/res/btw13_kerg.csv"))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.newFormat(';'));

            int row = 0;
            Map<Integer, String> header = new HashMap<>();
            for (CSVRecord record : parser) {
                int aktuelleSpalte = 0;
                if (row == 0) {
                    while (aktuelleSpalte < record.size()) {
                        header.put(aktuelleSpalte, record.get(aktuelleSpalte++));
                    }
                }
                if (row++ < 3) {
                    continue;
                }
                if (record.get(0).isEmpty()) {
                    continue;
                }

                int wahlkreisnummer = getInt(record.get(aktuelleSpalte++));
                if (wahlkreisnummer > 900) {
                    continue;
                }
                String name = record.get(aktuelleSpalte++);
                Bundesland bundesland = data.getBundeslandByKuerzel(record.get(aktuelleSpalte++));
                int wahlberechtigte = getInt(record.get(aktuelleSpalte++));

                aktuelleSpalte += 7;

                int ungueltigeStimmenErst = getInt(record.get(aktuelleSpalte++));
                aktuelleSpalte++;
                int ungueltigeStimmenZweit = getInt(record.get(aktuelleSpalte++));

                aktuelleSpalte+=5;

                Wahlkreis kreis = new Wahlkreis(wahlkreisnummer, name, bundesland, data.wahl2013, wahlberechtigte, ungueltigeStimmenErst, ungueltigeStimmenZweit);

                data.wahlkreise.add(kreis);

                while(aktuelleSpalte < record.size()) {
                    String parteiKuerzel = header.get(aktuelleSpalte);
                    Integer erststimmen = getInt(record.get(aktuelleSpalte++));
                    aktuelleSpalte++; // Vorperiode egal
                    Integer zweitstimmen = getInt(record.get(aktuelleSpalte++));
                    aktuelleSpalte++; // Vorperiode egal

                    if (erststimmen == null && zweitstimmen == null) {
                        continue;
                    }

                    kreis.getErgebnisse().add(new WahlkreisErgebnis(
                        data.getCreateByKuerzel(parteiKuerzel, data.wahl2013),
                        erststimmen == null ? 0 : erststimmen,
                        zweitstimmen == null ? 0 : zweitstimmen,
                        data.wahl2013));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseWahldatenJson(Datamodel data) {
        try (FileReader fileReader = new FileReader(new File("wahlparser/src/res/complete.json"))) {

            JsonObject mainObject = Json.createReader(fileReader).readObject();

            JsonObject wahlkreiseJson = mainObject.getJsonObject("wahlkreise");
            for (int i = 1; i <= 299; i++) {
                JsonObject wahlkreisJson = wahlkreiseJson.getJsonObject(Integer.toString(i));
                Wahlkreis wahlkreis = new Wahlkreis(
                        i,
                        wahlkreisJson.getString("Name"),
                        data.getBundesland(wahlkreisJson.getString("Bundesland")),
                        data.wahl2017,
                        wahlkreisJson.getInt("Wahlberechtigte_17"),
                        wahlkreisJson.getInt("Ungueltige_17_Erst"),
                        wahlkreisJson.getInt("Ungueltige_17_Zweit"));
                data.wahlkreise.add(wahlkreis);

                JsonArray ergebnisse = wahlkreisJson.getJsonArray("ParteiErgebnisse");
                for (int j = 0; j < ergebnisse.size(); j++) {
                    JsonObject ergebnisJson = ergebnisse.getJsonObject(j);
                    String parteiName = ergebnisJson.getString("Partei");
                    wahlkreis.getErgebnisse().add(new WahlkreisErgebnis(data.getCreatePartei(parteiName, null, data.wahl2017),
                            ergebnisJson.getInt("Erststimme_" + data.wahl2017.getKurzschreibweise()),
                            ergebnisJson.getInt("Zweitstimme_" + data.wahl2017.getKurzschreibweise()), data.wahl2017));
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
                        wahlkreis != null ? data.getWahlkreis(wahlkreis, data.wahl2013) : null,
                        partei);

                if (landKurz != null && partei != null) {
                    Bundesland land = data.getBundeslandByKuerzel(landKurz);
                    partei.addLandeslisteEintrag(land, bewerber, listenEintrag, data.wahl2013);
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
                    partei = data.getPartei(parteiBez, parteiKurz, data.wahl2017);
                }

                // Neuen Bewerber anlegen
                Bewerber bewerber = new Bewerber(titel, nachname, vorname, namenszusatz, geschlecht, beruf, jahrgang,
                        wahlkreis != null ? data.getWahlkreis(wahlkreis, data.wahl2017) : null,
                        partei);

                // Dieser Bewerber steht auf einer Landesliste
                if (!parteiListe.isEmpty()) {
                    Partei landesPartei = data.getPartei(parteiListe, record.get("Liste_ParteiKurzBez"), data.wahl2017);
                    landesPartei.addLandeslisteEintrag(data.getBundeslandByKuerzel(landKurz), bewerber, listenEintrag, data.wahl2017);
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

