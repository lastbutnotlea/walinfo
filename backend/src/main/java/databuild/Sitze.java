package databuild;

public class Sitze {

    private final Partei partei;
    private final int anzahlMandate;

    public Sitze(Partei partei, int anzahlMandate) {
        this.partei = partei;
        this.anzahlMandate = anzahlMandate;
    }

    public Partei getPartei() {
        return partei;
    }

    public int getAnzahlMandate() {
        return anzahlMandate;
    }
}
