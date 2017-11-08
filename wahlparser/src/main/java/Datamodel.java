import java.util.*;

public class Datamodel {
    public Set<Bundesland> laender = new HashSet<>();

    public Map<Wahljahr, Set<Wahlkreis>> wahlkreise = new HashMap<>();

    public Map<Wahljahr, Set<Bewerber>> bewerber = new HashMap<>();

    public Set<Partei> parteien = new HashSet<>();

    public Wahljahr wahl2013, wahl2017;

    public Map<String, String> parteienKurzschreibweise = new HashMap<>();

    public Datamodel() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 9, 22);

        wahl2013 = new Wahljahr(calendar.getTime());
        wahlkreise.put(wahl2013, new HashSet<>());
        bewerber.put(wahl2013, new HashSet<>());

        calendar.set(2017, 9, 24);
        wahl2017 = new Wahljahr(calendar.getTime());
        wahlkreise.put(wahl2017, new HashSet<>());
        bewerber.put(wahl2017, new HashSet<>());
    }

    public Bundesland getBundesland(String name) {
        for (Bundesland land : laender) {
            if (land.getName().equals(name)) {
                return land;
            }
        }
        throw new RuntimeException(name);
    }

    public Bundesland getBundeslandByKuerzel(String name) {
        if (name == null) {
            return null;
        }
        for (Bundesland land : laender) {
            if (land.getKurzschreibweise().equals(name)) {
                return land;
            }
        }
        throw new RuntimeException(name);
    }

    public Partei getCreatePartei(String name) {
        if (name.equals("Übrige")) {
            return null;
        }
        for (Partei partei : parteien) {
            if (partei.getName().equals(name)) {
                return partei;
            }
        }

        String kurz = parteienKurzschreibweise.get(name);
        if (kurz == null) {
            throw new RuntimeException(name);
        }
        Partei partei = new Partei(name, kurz);

        parteien.add(partei);
        return partei;
    }

    public Partei getByKuerzel(String kurz) {
        for (Partei partei : parteien) {
            if (partei.getKurzschreibweise().equals(kurz)) {
                return partei;
            }
        }
        String name = null;
        int anzahl = 0;
        for (Map.Entry<String, String> entry : parteienKurzschreibweise.entrySet()) {
            String parteiName = entry.getKey();
            if (entry.getValue().equals(kurz)) {
                name = parteiName;
                anzahl++;
            }
        }
        if (anzahl == 1) {
            Partei neu = new Partei(name, kurz);
            parteien.add(neu);
            System.out.println("Zusätzlich angelegte Partei: " + name);
            return neu;
        }
        throw new RuntimeException(kurz);
    }

    public Wahlkreis getWahlkreis(Wahljahr jahr, int i) {
        for (Wahlkreis kreis : wahlkreise.get(jahr)) {
            if (kreis.getId() == i) {
                return kreis;
            }
        }
        throw new RuntimeException(jahr.getJahr().toString());
    }
}
