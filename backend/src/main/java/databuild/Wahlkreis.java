package databuild;

public class Wahlkreis {

    private final int id;
    private final int nummer;
    private final String name;
    private final String bundesland;

    public Wahlkreis(int id, int nummer, String name, String bundesland) {
        this.id = id;
        this.nummer = nummer;
        this.name = name;
        this.bundesland = bundesland;
    }

    public int getId() {
        return id;
    }

    public int getNummer() {
        return nummer;
    }

    public String getName() {
        return name;
    }

    public String getBundesland() {
        return bundesland;
    }
}
