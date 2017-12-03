package databuild;

public class Ueberhangmandate {

    private final Bundesland bundesland;
    private final Partei partei;
    private final int ueberhangmandate;

    public Ueberhangmandate(Bundesland bundesland, Partei partei, int ueberhangmandate) {
        this.bundesland = bundesland;
        this.partei = partei;
        this.ueberhangmandate = ueberhangmandate;
    }

    public Bundesland getBundesland() {
        return bundesland;
    }

    public Partei getPartei() {
        return partei;
    }

    public int getUeberhangmandate() {
        return ueberhangmandate;
    }
}
