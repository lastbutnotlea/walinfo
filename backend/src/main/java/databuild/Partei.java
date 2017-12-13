package databuild;

public class Partei {

    private final int id;
    private final String kuerzel;
    private final String name;
    private final String farbe;

    public Partei(int id, String kuerzel, String name, String farbe) {
        this.id = id;
        this.kuerzel = kuerzel;
        this.name = name;
        this.farbe = farbe;
    }

    public int getId() {
        return id;
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
