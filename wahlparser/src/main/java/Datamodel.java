import java.util.*;

public class Datamodel {
    public Set<Bundesland> laender = new HashSet<>();

    public Set<Wahlkreis> wahlkreise = new HashSet<>();

    public Map<Wahljahr, Set<Bewerber>> bewerber = new HashMap<>();

    public Set<Partei> parteien = new HashSet<>();

    public Wahljahr wahl2013, wahl2017;

    public Map<String, String> parteienKurzschreibweise = new HashMap<>();

    public Map<Integer, Integer> wahlkreisMapping = new HashMap<>();

    // public Partei uebrigePartei = new Partei("Übrige", "Übrige", wahl2013);

    public Datamodel() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 9, 22);

        wahl2013 = new Wahljahr(calendar.getTime());
        bewerber.put(wahl2013, new HashSet<>());

        calendar.set(2017, 9, 24);
        wahl2017 = new Wahljahr(calendar.getTime());
        bewerber.put(wahl2017, new HashSet<>());

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
        if (name.equalsIgnoreCase("Übrige") || name.isEmpty() || (kurzAngabe != null && kurzAngabe.startsWith("EB: "))) {
            return null;
        }
        if (jahr == null) {
            throw new NullPointerException(name);
        }
        for (Partei partei : parteien) {
            if (partei.getName().equals(name) && partei.getJahr() == jahr) {
                return partei;
            }
        }

        String kurz = parteienKurzschreibweise.get(name);
        if (kurzAngabe != null) {
            kurz = kurzAngabe;
        } else if (kurz == null) {
            System.err.println("Für folgende Partei ist kein Kürzel vorhanden: " + name);
        }
        Partei partei = new Partei(name, kurz, jahr);

        parteien.add(partei);
        return partei;
    }

    public Partei getPartei(String name, String kurzAngabe, Wahljahr jahr) {
        if (name.equals("Übrige") || name.isEmpty() || (kurzAngabe != null && kurzAngabe.startsWith("EB: "))) {
            return null;
        }
        for (Partei partei : parteien) {
            if (partei.getName().equals(name) && partei.getJahr() == jahr) {
                return partei;
            }
        }
        throw new RuntimeException(name);
    }

    public Partei getByKuerzel(String kurz, Wahljahr jahr) {
        if (kurz.startsWith("EB: ")) {
            return null;
        }
        if (kurz.equalsIgnoreCase("VERNUNFT")) {
            kurz = "PARTEI DER VERNUNFT";
        }
        for (Partei partei : parteien) {
            if ((partei.getKurzschreibweise().equalsIgnoreCase(kurz)
                    || partei.getKurzschreibweise().replaceAll(" ", "").equalsIgnoreCase(kurz))
                    && partei.getJahr() == jahr) {
                return partei;
            }
        }
        return null;
    }

    public Partei getCreateByKuerzel(String kurz, Wahljahr jahr) {
        if (kurz.startsWith("EB: ") || kurz.equals("Übrige")) {
            return null;
        }
        Partei partei = getByKuerzel(kurz, jahr);
        if (partei == null) {
            String name = null;
            for (Map.Entry<String, String> entry : parteienKurzschreibweise.entrySet()) {
                if (entry.getValue().equals(kurz)) {
                    name = entry.getKey();
                }
            }
            if (name == null) {
                System.err.println("Konnte nichts zu " + kurz + " finden.");
            } else {
                partei = new Partei(name, kurz, jahr);
                parteien.add(partei);
            }

        }
        return partei;
    }

    public Wahlkreis getWahlkreis(int i, Wahljahr jahr) {
        for (Wahlkreis kreis : wahlkreise) {
            if (kreis.getNummer() == i && kreis.getJahr() == jahr) {
                return kreis;
            }
        }
        throw new RuntimeException();
    }
}
