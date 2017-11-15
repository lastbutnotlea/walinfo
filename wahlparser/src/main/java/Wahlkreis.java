import java.util.HashSet;
import java.util.Set;

public class Wahlkreis {
    private int nummer, id;
    private static int counter = 0;

    public int getNummer() {
        return nummer;
    }

    private String name;
    private Bundesland land;
    private Wahljahr jahr;

    public int getWahlberechtigte() {
        return wahlberechtigte;
    }


    private int wahlberechtigte;
    private int ungueltigeStimmenErst;
    private int ungueltigeStimmenZweit;

    public int getUngueltigeStimmenErst() {
        return ungueltigeStimmenErst;
    }

    public int getUngueltigeStimmenZweit() {
        return ungueltigeStimmenZweit;
    }

    public int getId() {
        return id;
    }

    private Set<WahlkreisErgebnis> ergebnisse = new HashSet<>();

    public Set<WahlkreisErgebnis> getErgebnisse() {
        return ergebnisse;
    }


    public Wahlkreis(int nummer, String name, Bundesland land, Wahljahr jahr, int wahlberechtigte, int ungueltigeStimmenErst, int ungueltigeStimmenZweit) {
        this.nummer = nummer;
        this.name = name;
        this.land = land;
        this.jahr = jahr;
        this.wahlberechtigte = wahlberechtigte;
        this.ungueltigeStimmenErst = ungueltigeStimmenErst;
        this.ungueltigeStimmenZweit = ungueltigeStimmenZweit;
        this.id = counter++;
    }

    public String getName() {
        return name;
    }

    public Bundesland getLand() {
        return land;
    }

    public Wahljahr getJahr() {
        return jahr;
    }
}
