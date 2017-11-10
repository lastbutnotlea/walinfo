import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Partei {

    private static int counter = 0;

    private Map<Bundesland, Landesliste> landeslisten = new HashMap<>();
    Wahljahr jahr;

    public Wahljahr getJahr() {
        return jahr;
    }

    public Partei(String name, String kurzschreibweise, Wahljahr jahr) {
        this.name = name;
        this.kurzschreibweise = kurzschreibweise;
        this.number = counter++;
        this.jahr = jahr;
    }

    public String getKurzschreibweise() {
        return kurzschreibweise;
    }

    public void setKurzschreibweise(String kurzschreibweise) {
        this.kurzschreibweise = kurzschreibweise;
    }

    private String name, kurzschreibweise;
    private int number;

    public Map<Bundesland, Landesliste> getLandeslisten() {
        return landeslisten;
    }

    public void addLandeslisteEintrag(Bundesland land, Bewerber bewerber, int listenPlatz) {
        if (land == null) {
            throw new NullPointerException();
        }
        if (landeslisten.get(land) == null) {
            landeslisten.put(land, new Landesliste());
        }
        if (landeslisten.get(land).getEintraege().containsKey(listenPlatz)) {
            throw new RuntimeException(listenPlatz + " schon vorhanden");
        }
        landeslisten.get(land).getEintraege().put(listenPlatz, bewerber);
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
