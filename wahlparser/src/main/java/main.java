import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class main {

    public static void main(String[]args){
        Datamodel data = new Datamodel();

        // Parse bundeslaender Daten
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

        // Parse Parteien Kurzschreibweisen
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

        try (FileReader fileReader = new FileReader(new File("wahlparser/src/res/complete.json"))) {

            JsonObject mainObject = Json.createReader(fileReader).readObject();

            for (Object o : mainObject.keySet()) {
                System.out.println(o.toString());

            }

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
                        wahlkreis.getErgebnisse().add(new WahlkreisErgebnis(data.getCreatePartei(parteiName),
                                ergebnisJson.getInt("Erststimme_" + jahr.getKurzschreibweise()),
                                ergebnisJson.getInt("Zweitstimme_" + jahr.getKurzschreibweise())));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse Bewerber 2013
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
                    partei = data.getByKuerzel(parteikurz);
                }
                if (landKurz.isEmpty()) {
                    landKurz = null;
                }

                Bewerber bewerber = new Bewerber(titel, nachname, vorname, jahrgang,
                        wahlkreis != null ? data.getWahlkreis(data.wahl2013, wahlkreis) : null,
                        data.getBundeslandByKuerzel(landKurz),
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
        System.out.println("");
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

