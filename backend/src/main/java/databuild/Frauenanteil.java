package databuild;

public class Frauenanteil {

    private final int anzahlMaenner;
    private final int gesamtAnzahl;

    public Frauenanteil(int anzahlMaenner, int gesamtAnzahl) {
        this.anzahlMaenner = anzahlMaenner;
        this.gesamtAnzahl = gesamtAnzahl;
    }

    public int getAnzahlMaenner() {
        return anzahlMaenner;
    }

    public int getGesamtAnzahl() {
        return gesamtAnzahl;
    }
}
