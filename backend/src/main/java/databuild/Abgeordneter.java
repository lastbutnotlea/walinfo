package databuild;

public class Abgeordneter {

    private final String titel;
    private final String name;
    private final String vorname;
    private final String namenszusatz;
    private final int geburtsjahr;
    private final Partei partei;

    public Abgeordneter(String titel, String name, String vorname,
                        String namneszusatz, int geburtsjahr, Partei partei) {
        this.name = name;
        this.vorname = vorname;
        this.titel = titel;
        this.namenszusatz = namneszusatz;
        this.geburtsjahr = geburtsjahr;
        this.partei = partei;
    }

    public String getName() {
        return name;
    }

    public String getVorname() {
        return vorname;
    }

    public String getTitel() {
        return titel;
    }

    public String getNamenszusatz() {
        return namenszusatz;
    }

    public int getGeburtsjahr() {
        return geburtsjahr;
    }

    public Partei getPartei() {
        return partei;
    }

}
