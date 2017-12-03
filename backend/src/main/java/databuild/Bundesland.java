package databuild;

public class Bundesland {

    private final String kuerzel;
    private final String name;

    public Bundesland(String kuerzel, String name) {
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
