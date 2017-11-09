import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.util.*;

public class StimmGenerator {
    Datamodel datamodel;
    Map<Partei, Set<Bewerber>> landeslisteFuer = new HashMap<>();

    public StimmGenerator(Datamodel datamodel) {
        this.datamodel = datamodel;

        for (Bewerber bewerber : datamodel.bewerber.get(datamodel.wahl2017)) {
            if (!landeslisteFuer.containsKey(bewerber.getPartei())) {
                    landeslisteFuer.put(bewerber.getPartei(), new HashSet<>());
            }
            if ("CDU".equals(bewerber.getPartei().getKurzschreibweise())) {
                break;
            }
            landeslisteFuer.get(bewerber.getPartei()).add(bewerber);
        }
    }

    public void writeInsertStatements(File file) throws IOException {
        PrintStream out = new PrintStream(new FileOutputStream(file));

        for (Wahlkreis kreis : datamodel.wahlkreise.get(datamodel.wahl2017)) {
            System.out.println("Generating Wahlkreis:" + kreis.getNummer());

            Iterator<WahlkreisErgebnis> erstStimmen = kreis.getErgebnisse().iterator();
            WahlkreisErgebnis erstStimme = erstStimmen.next();
            Iterator<WahlkreisErgebnis> zweitStimmen = kreis.getErgebnisse().iterator();
            WahlkreisErgebnis zweitStimme = zweitStimmen.next();

            boolean erstFertig = false;
            boolean zweitFertig = false;

            while (!erstFertig || !zweitFertig) {

                if (erstStimme.getAnzahlErststimmen() <= 0 ) {
                    if (erstStimmen.hasNext()) {
                        erstStimme = erstStimmen.next();
                    } else {
                        erstFertig = true;
                    }
                }

                if (zweitStimme.getZweitstimmen() <= 0 ) {
                    if (zweitStimmen.hasNext()) {
                        zweitStimme = zweitStimmen.next();
                    } else {
                        zweitFertig = true;
                    }
                }

                Partei partei = null;
                Bewerber bewerber = null;
                if (!zweitFertig) {
                    partei = zweitStimme.getPartei();
                    zweitStimme.setZweitstimmen(zweitStimme.getZweitstimmen() - 1);
                }
                if (!erstFertig) {
                    Set<Bewerber> bewerberFuerPartei = landeslisteFuer.get(erstStimme.getPartei());
                    if (bewerberFuerPartei == null) {
                        System.out.println("help");
                    }
                    for (Bewerber kandidat : bewerberFuerPartei) {
                        if (kandidat.getWahlkreis() == kreis) {
                            bewerber = kandidat;
                        }
                    }
                    erstStimme.setAnzahlErststimmen(erstStimme.getAnzahlErststimmen() - 1);
                }
                if (partei == null && bewerber == null) {
                    System.out.println("help");
                }
                if (!zweitFertig && !erstFertig) {
                    out.print(partei != null ? partei.getNumber() : "null");
                    out.print("|");
                    out.print(bewerber != null ? bewerber.getId() : "null");
                    out.print("|");
                    out.println(kreis.getId());
                }
            }
        }
    }
}
