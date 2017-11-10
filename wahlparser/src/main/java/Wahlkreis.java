import java.util.HashSet;
import java.util.Set;

public class Wahlkreis {
    private int nummer;

    public int getNummer() {
        return nummer;
    }

    private String name;
    private Bundesland land;

    public int getWahlberechtigte17() {
        return wahlberechtigte17;
    }

    public int getWahlberechtigte13() {
        return wahlberechtigte13;
    }

    private int wahlberechtigte17,wahlberechtigte13;
//            waehler,
//            gueltigeStimmenErst,
//            ungueltigeStimmenErst,
//            gueltigeStimmenZweit,
//            ungueltigeStimmenZweit;

    private Set<WahlkreisErgebnis> ergebnisse = new HashSet<>();

    public Set<WahlkreisErgebnis> getErgebnisse() {
        return ergebnisse;
    }

    public void setErgebnisse(Set<WahlkreisErgebnis> ergebnisse) {
        this.ergebnisse = ergebnisse;
    }


    public Wahlkreis(int nummer, String name, Bundesland land, int wahlberechtigte17, int wahlberechtigte13) {
        this.nummer = nummer;
        this.name = name;
        this.land = land;
        this.wahlberechtigte17 = wahlberechtigte17;
        this.wahlberechtigte13 = wahlberechtigte13;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bundesland getLand() {
        return land;
    }

    public void setLand(Bundesland land) {
        this.land = land;
    }


}
