package databuild;

public class KnappesErgebnis {

    private final Abgeordneter abgeordneter;
    private final SiegerOderVerlierer siegerOderVerlierer;
    private final int unterschied;

    public KnappesErgebnis(Abgeordneter abgeordneter, SiegerOderVerlierer siegerOderVerlierer, int unterschied) {
        this.abgeordneter = abgeordneter;
        this.siegerOderVerlierer = siegerOderVerlierer;
        this.unterschied = unterschied;
    }

    public Abgeordneter getAbgeordneter() {
        return abgeordneter;
    }

    public SiegerOderVerlierer getSiegerOderVerlierer() {
        return siegerOderVerlierer;
    }

    public int getUnterschied() {
        return unterschied;
    }
}
