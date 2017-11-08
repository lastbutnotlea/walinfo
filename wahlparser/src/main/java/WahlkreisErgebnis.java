public class WahlkreisErgebnis {
    private Partei parte;
    private int anzahlErststimmen, zweitstimmen;

    public WahlkreisErgebnis(Partei parte, int anzahlErststimmen, int zweitstimmen) {
        this.parte = parte;
        this.anzahlErststimmen = anzahlErststimmen;
        this.zweitstimmen = zweitstimmen;
    }

    public Partei getParte() {
        return parte;
    }

    public void setParte(Partei parte) {
        this.parte = parte;
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
