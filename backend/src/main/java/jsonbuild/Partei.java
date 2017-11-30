package jsonbuild;

public class Partei {

    private final String kuerzel;
    private final String name;

    public Partei(String kuerzel, String name) {
        this.kuerzel = kuerzel;
        this.name = name;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public String getName() {
        return name;
    }
}
