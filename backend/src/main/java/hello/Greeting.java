package hello;

import java.util.ArrayList;


public class Greeting {

    private final int id;
    private final String parteiname;
    private final int anzahl;
    private final int jahr;

   // private final long id;
   // private final String content;

    public Greeting(int id, String parteiname, int anzahl, int jahr) {
        this.id = id;
        this.parteiname = parteiname;
        this.anzahl = anzahl;
        this.jahr = jahr;
    }

    /*
    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    */

    public int getId() {
        return id;
    }

    public String getParteiname() {
        return parteiname;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public int getJahr() {
        return jahr;
    }
}
