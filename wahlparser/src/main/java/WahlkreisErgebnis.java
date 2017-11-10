public class WahlkreisErgebnis {
    private Partei partei;
    private int anzahlErststimmen, zweitstimmen;
    private Wahljahr jahr;

    public Wahljahr getJahr() {
        return jahr;
    }

    public WahlkreisErgebnis(Partei partei, int anzahlErststimmen, int zweitstimmen, Wahljahr jahr) {
        this.partei = partei;
        this.anzahlErststimmen = anzahlErststimmen;
        this.zweitstimmen = zweitstimmen;
        this.jahr = jahr;
    }

    public Partei getPartei() {
        return partei;
    }

    public void setPartei(Partei partei) {
        this.partei = partei;
    }

    public int getAnzahlErststimmen() {
        return anzahlErststimmen;
    }

    public void setAnzahlErststimmen(int anzahlErststimmen) {
        this.anzahlErststimmen = anzahlErststimmen;
    }

    public int getZweitstimmen() {
        return zweitstimmen;
    }

    public void setZweitstimmen(int zweitstimmen) {
        this.zweitstimmen = zweitstimmen;
    }
}
