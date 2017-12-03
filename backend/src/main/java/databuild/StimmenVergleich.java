package databuild;

public class StimmenVergleich {

    private final Partei partei;
    private final int stimmen2013;
    private final int stimmen2017;

    public StimmenVergleich(Partei partei, int stimmen2013, int stimmen2017) {
        this.partei = partei;
        this.stimmen2013 = stimmen2013;
        this.stimmen2017 = stimmen2017;
    }

    public Partei getPartei() {
        return partei;
    }

    public int getStimmen2013() {
        return stimmen2013;
    }

    public int getStimmen2017() {
        return stimmen2017;
    }
}
