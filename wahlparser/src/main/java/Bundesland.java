public class Bundesland {
    private String name, kurzschreibweise;
    private int id;

    private static int idCounter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKurzschreibweise() {
        return kurzschreibweise;
    }

    public void setKurzschreibweise(String kurzschreibweise) {
        this.kurzschreibweise = kurzschreibweise;
    }

    public Bundesland(String kurz, String name) {
        this.id = idCounter++;
        this.kurzschreibweise = kurz;

        this.name = name;
    }
}
