public class Bewerber {
    private String titel, nachname, vorname, namenszusatz, geschlecht, beruf;
    int jahrgang;
    Wahlkreis wahlkreis;
    private Partei partei;

    private static int counter;
    private int id;

    public int getId() {
        return id;
    }

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

    public String getNamenszusatz() {
        return namenszusatz;
    }

    public void setNamenszusatz(String namenszusatz) {
        this.namenszusatz = namenszusatz;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getBeruf() {
        return beruf;
    }

    public void setBeruf(String beruf) {
        this.beruf = beruf;
    }

    public Bewerber(String titel, String nachname, String vorname, String namenszusatz, String geschlecht, String beruf, int jahrgang, Wahlkreis wahlkreis, Partei partei) {
        this.namenszusatz = namenszusatz;
        this.geschlecht = geschlecht;
        this.beruf = beruf;
        this.partei = partei;
        this.titel = titel;
        this.nachname = nachname;
        this.vorname = vorname;
        this.jahrgang = jahrgang;
        this.wahlkreis = wahlkreis;
        this.id = counter++;
    }
}
