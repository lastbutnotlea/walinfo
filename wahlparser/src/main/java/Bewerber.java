public class Bewerber {
    private String titel, nachname, vorname;
    int jahrgang;
    Wahlkreis wahlkreis;
    Bundesland bundesland;
    private Partei partei;

    public Partei getPartei() {
        return partei;
    }

    public void setPartei(Partei partei) {
        this.partei = partei;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public int getJahrgang() {
        return jahrgang;
    }

    public void setJahrgang(int jahrgang) {
        this.jahrgang = jahrgang;
    }

    public Wahlkreis getWahlkreis() {
        return wahlkreis;
    }

    public void setWahlkreis(Wahlkreis wahlkreis) {
        this.wahlkreis = wahlkreis;
    }

    public Bundesland getBundesland() {
        return bundesland;
    }

    public void setBundesland(Bundesland bundesland) {
        this.bundesland = bundesland;
    }

    public Bewerber(String titel, String nachname, String vorname, int jahrgang, Wahlkreis wahlkreis, Bundesland bundesland, Partei partei) {
        this.partei = partei;
        this.titel = titel;
        this.nachname = nachname;
        this.vorname = vorname;
        this.jahrgang = jahrgang;
        this.wahlkreis = wahlkreis;
        this.bundesland = bundesland;
    }
}
