package databuild;

public class Partei {

    private final String kuerzel;
    private final String name;
    private final String farbe;

    public Partei(String kuerzel, String name, String farbe) {
        this.kuerzel = kuerzel;
        this.name = name;
        this.farbe = farbe;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public String getName() {
        return name;
    }

    public String getFarbe() {
        return farbe;
    }
}
