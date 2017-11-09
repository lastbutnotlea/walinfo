import java.util.*;

public class Datamodel {
    public Set<Bundesland> laender = new HashSet<>();

    public Map<Wahljahr, Set<Wahlkreis>> wahlkreise = new HashMap<>();

    public Map<Wahljahr, Set<Bewerber>> bewerber = new HashMap<>();

    public Map<Wahljahr, Set<Partei>> parteien = new HashMap<>();

    public Wahljahr wahl2013, wahl2017;

    public Map<String, String> parteienKurzschreibweise = new HashMap<>();

    public Datamodel() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 9, 22);

        wahl2013 = new Wahljahr(calendar.getTime());
        wahlkreise.put(wahl2013, new HashSet<>());
        bewerber.put(wahl2013, new HashSet<>());
        parteien.put(wahl2013, new HashSet<>());

        calendar.set(2017, 9, 24);
        wahl2017 = new Wahljahr(calendar.getTime());
        wahlkreise.put(wahl2017, new HashSet<>());
        bewerber.put(wahl2017, new HashSet<>());
        parteien.put(wahl2017, new HashSet<>());
    }

    public Bundesland   getBundesland(String name) {
        for (Bundesland land : laender) {
            if (land.getName().equals(name)) {
                return land;
            }
        }
        throw new RuntimeException(name);
    }

    public Bundesland getBundeslandByKuerzel(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (Bundesland land : laender) {
            if (land.getKurzschreibweise().equals(name)) {
                return land;
            }
        }
        throw new RuntimeException(name);
    }

    public Partei getCreatePartei(String name, String kurzAngabe, Wahljahr jahr) {
        if (name.equals("Übrige") || name.isEmpty() || (kurzAngabe != null && kurzAngabe.startsWith("EB: "))) {
            return null;
        }
        for (Partei partei : parteien.get(jahr)) {
            if (partei.getName().equals(name)) {
                return partei;
            }
        }

        String kurz = parteienKurzschreibweise.get(name);
        if (kurzAngabe != null) {
            kurz = kurzAngabe;
        } else if (kurz == null) {
            System.err.println(name);
        }
        Partei partei = new Partei(name, kurz);

        parteien.get(jahr).add(partei);
        return partei;
    }

    public Partei getByKuerzel(String kurz, Wahljahr jahr) {
        if (kurz.startsWith("EB: ")) {
            return null;
        }
        for (Partei partei : parteien.get(jahr)) {
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
            parteien.get(jahr).add(neu);
            System.out.println("Zusätzlich angelegte Partei: " + name);
            return neu;
        }
        throw new RuntimeException(kurz);
    }

    public Wahlkreis getWahlkreis(Wahljahr jahr, int i) {
        for (Wahlkreis kreis : wahlkreise.get(jahr)) {
            if (kreis.getNummer() == i) {
                return kreis;
            }
        }
        throw new RuntimeException(jahr.getJahr().toString());
    }
}
