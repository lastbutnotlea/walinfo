package databuild;

public class AnzahlStimmenAbgeordneter {

    private final Abgeordneter abgeordneter;
    private final int anzahlAbsolut;

    public AnzahlStimmenAbgeordneter(Abgeordneter abgeordneter, int anzahlAbsolut, float anzahlRelativ) {
        this.abgeordneter = abgeordneter;
        this.anzahlAbsolut = anzahlAbsolut;
    }

    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    public int getAnzahlAbsolut() {
        return anzahlAbsolut;
    }
}
