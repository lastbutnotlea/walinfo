import java.util.HashSet;
import java.util.Set;

public class Partei {

    private static int counter = 0;

    private Set<Landesliste> landeslisten = new HashSet<>();

    public Partei(String name, String kurzschreibweise, Wahljahr jahr) {
        this.name = name;
        this.kurzschreibweise = kurzschreibweise;
        this.jahr = jahr;
        this.number = counter++;
    }

    public String getKurzschreibweise() {
        return kurzschreibweise;
    }

    public void setKurzschreibweise(String kurzschreibweise) {
        this.kurzschreibweise = kurzschreibweise;
    }

    private String name, kurzschreibweise;
    private int number;
    private Wahljahr jahr;

    public Wahljahr getJahr() {
        return jahr;
    }

    public Set<Landesliste> getLandeslisten() {
        return landeslisten;
    }

    public void addLandeslisteEintrag(Bundesland land, Bewerber bewerber, int listenPlatz, Wahljahr jahr) {
        if (land == null) {
            throw new NullPointerException();
        }
        Landesliste found = null;
        for (Landesliste liste : landeslisten) {
            if (liste.getLand() == land && liste.getJahr() == jahr) {
                found = liste;
            }
        }
        if (found == null) {
            found = new Landesliste(jahr, land);
            landeslisten.add(found);
        }
        if (found.getEintraege().containsKey(listenPlatz)) {
            throw new RuntimeException(listenPlatz + " schon vorhanden");
        }
        found.getEintraege().put(listenPlatz, bewerber);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }
}
