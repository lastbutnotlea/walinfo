package databuild;

public class Wahlbeteiligung {

    private final Wahlkreis wahlkreis;
    private final int anzahlWaehler;
    private final int anzahlWahlberechtigte;

    public Wahlbeteiligung(Wahlkreis wahlkreis,
                           int anzahlWaehler,
                           int anzahlWahlberechtigteZweitstimmen) {
        this.wahlkreis = wahlkreis;
        this.anzahlWaehler = anzahlWaehler;
        this.anzahlWahlberechtigte = anzahlWahlberechtigteZweitstimmen;
    }

    public Wahlkreis getWahlkreis() {
        return wahlkreis;
    }

    public int getAnzahlWaehler() {
        return anzahlWaehler;
    }

    public int getAnzahlWahlberechtigte() {
        return anzahlWahlberechtigte;
    }
}
