package sqlbuild;

public class Replace {

    public static String getGewaehlteErstkandidatenTable(String modus) {
        if(modus.equals("roh")) {
            return "gewaehlte_erstkandidaten";
        }
        else return "gewaehlte_erstkandidaten_schnell";
    }

    public static String getErststimmenTable(String modus) {
        if(modus.equals("roh")) {
            return "erststimmenergebnisse_view";
        }

        else return "erststimmenergebnisse";
    }

    public static String getZweitstimmenTable(String modus) {
        if(modus.equals("roh")) {
            return "zweitstimmenergebnisse_view";
        }

        else return "zweitstimmenergebnisse";
    }

}
