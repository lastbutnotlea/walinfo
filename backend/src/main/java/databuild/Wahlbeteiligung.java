package databuild;

public class Wahlbeteiligung {

    private final int anzahlWaehler;
    private final int anzahlWahlberechtigte;

    public Wahlbeteiligung(int anzahlWaehler,
                           int anzahlWahlberechtigteZweitstimmen) {
        this.anzahlWaehler = anzahlWaehler;
        this.anzahlWahlberechtigte = anzahlWahlberechtigteZweitstimmen;
    }

    public int getAnzahlWaehler() {
        return anzahlWaehler;
    }

    public int getAnzahlWahlberechtigte() {
        return anzahlWahlberechtigte;
    }
}
