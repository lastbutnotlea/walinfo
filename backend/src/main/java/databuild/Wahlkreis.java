package databuild;

public class Wahlkreis {

    private final int id;
    private final int nummer;
    private final int corrNummer;
    private final String name;
    private final Bundesland bundesland;

    public Wahlkreis(int id, int nummer, int corrNummer, String name, Bundesland bundesland) {
        this.id = id;
        this.nummer = nummer;
        this.corrNummer = corrNummer;
        this.name = name;
        this.bundesland = bundesland;
    }

    public int getId() {
        return id;
    }

    public int getNummer() {
        return nummer;
    }

    public int getCorrNummer() {
        return corrNummer;
    }

    public String getName() {
        return name;
    }

    public Bundesland getBundesland() {
        return bundesland;
    }
}
