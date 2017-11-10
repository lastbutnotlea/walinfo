import java.util.HashSet;
import java.util.Set;

public class Wahlkreis {
    private int nummer;

    public int getNummer() {
        return nummer;
    }

    private String name;
    private Bundesland land;

    private int wahlberechtigte,
            waehler,
            gueltigeStimmenErst,
            ungueltigeStimmenErst,
            gueltigeStimmenZweit,
            ungueltigeStimmenZweit;

    private Set<WahlkreisErgebnis> ergebnisse = new HashSet<>();

    public Set<WahlkreisErgebnis> getErgebnisse() {
        return ergebnisse;
    }

    public void setErgebnisse(Set<WahlkreisErgebnis> ergebnisse) {
        this.ergebnisse = ergebnisse;
    }

    public int getGueltigeStimmenErst() {
        return gueltigeStimmenErst;
    }

    public void setGueltigeStimmenErst(int gueltigeStimmenErst) {
        this.gueltigeStimmenErst = gueltigeStimmenErst;
    }

    public int getUngueltigeStimmenErst() {
        return ungueltigeStimmenErst;
    }

    public void setUngueltigeStimmenErst(int ungueltigeStimmenErst) {
        this.ungueltigeStimmenErst = ungueltigeStimmenErst;
    }

    public int getGueltigeStimmenZweit() {
        return gueltigeStimmenZweit;
    }

    public void setGueltigeStimmenZweit(int gueltigeStimmenZweit) {
        this.gueltigeStimmenZweit = gueltigeStimmenZweit;
    }

    public int getUngueltigeStimmenZweit() {
        return ungueltigeStimmenZweit;
    }

    public void setUngueltigeStimmenZweit(int ungueltigeStimmenZweit) {
        this.ungueltigeStimmenZweit = ungueltigeStimmenZweit;
    }

    public Wahlkreis(int nummer, String name, Bundesland land, int wahlberechtigte, int waehler, int gueltigeStimmenErst, int ungueltigeStimmenErst, int gueltigeStimmenZweit, int ungueltigeStimmenZweit) {
        this.nummer = nummer;
        this.name = name;
        this.land = land;
        this.wahlberechtigte = wahlberechtigte;
        this.waehler = waehler;
        this.gueltigeStimmenErst = gueltigeStimmenErst;
        this.ungueltigeStimmenErst = ungueltigeStimmenErst;
        this.gueltigeStimmenZweit = gueltigeStimmenZweit;
        this.ungueltigeStimmenZweit = ungueltigeStimmenZweit;
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

    public int getWahlberechtigte() {
        return wahlberechtigte;
    }

    public void setWahlberechtigte(int wahlberechtigte) {
        this.wahlberechtigte = wahlberechtigte;
    }

    public int getWaehler() {
        return waehler;
    }

    public void setWaehler(int waehler) {
        this.waehler = waehler;
    }
}
