import java.util.HashMap;
import java.util.Map;

public class Landesliste {

    private Wahljahr jahr;
    private Bundesland land;

    public Bundesland getLand() {
        return land;
    }

    private Map<Integer, Bewerber> eintraege = new HashMap<>();

    public Wahljahr getJahr() {
        return jahr;
    }

    public Landesliste(Wahljahr jahr, Bundesland land) {
        this.jahr = jahr;

        this.land = land;
    }

    public Map<Integer, Bewerber> getEintraege() {
        return eintraege;
    }
}
