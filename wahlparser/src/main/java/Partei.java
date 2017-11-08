import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Partei {

    private static int counter = 0;

    private Map<Bundesland, Landesliste> landeslisten = new HashMap<>();

    public Partei(String name, String kurzschreibweise) {
        this.name = name;
        this.kurzschreibweise = kurzschreibweise;
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

    public Map<Bundesland, Landesliste> getLandeslisten() {
        return landeslisten;
    }

    public void addLandeslisteEintrag(Bundesland land, Bewerber bewerber, int listenPlatz) {
        if (landeslisten.get(land) == null) {
            landeslisten.put(land, new Landesliste());
        }
        landeslisten.get(land).getEintraege().put(listenPlatz, bewerber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
