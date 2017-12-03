package databuild;

public class AnzahlStimmen {

    private final Partei partei;
    private final int anzahlAbsolut;
    private final float anzahlRelativ;

    public AnzahlStimmen(Partei partei, int anzahlAbsolut, float anzahlRelativ) {
        this.partei = partei;
        this.anzahlAbsolut = anzahlAbsolut;
        this.anzahlRelativ = anzahlRelativ;
    }

    public Partei getPartei() {
        return partei;
    }

    public int getAnzahlAbsolut() {
        return anzahlAbsolut;
    }

    public float getAnzahlRelativ() {
        return anzahlRelativ;
    }
}
