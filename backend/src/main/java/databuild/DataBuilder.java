package databuild;

import com.sun.corba.se.impl.oa.toa.TOA;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBuilder {

    public static ArrayList<Sitze> getSitzverteilungList(ResultSet result) throws SQLException {
        ArrayList<Sitze> sitzverteilung = new ArrayList<>();

        while (result.next()) {
            Partei partei = new Partei(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3)
            );
            Sitze sitze = new Sitze(
                    partei,
                    result.getInt(4));

            sitzverteilung.add(sitze);
        }

        return sitzverteilung;
    }

    public static ArrayList<Abgeordneter> getMitgliederList(ResultSet result) throws SQLException {
        ArrayList<Abgeordneter> mitglieder = new ArrayList<>();

        while (result.next()) {
            Partei partei = new Partei(
                    result.getString(6),
                    result.getString(7),
                    result.getString(8)
            );
            Abgeordneter abgeordneter = new Abgeordneter(
                    0,
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getInt(5),
                    partei
            );

            mitglieder.add(abgeordneter);
        }

        return mitglieder;
    }

    public static ArrayList<Wahlkreis> getWahlkreisList(ResultSet result) throws SQLException {
        ArrayList<Wahlkreis> wahlkreise = new ArrayList<>();

        while (result.next()) {
            Bundesland bundesland = new Bundesland(
                    result.getString(4),
                    result.getString(5)
            );
            Wahlkreis wahlkreis = new Wahlkreis(
                    result.getInt(1),
                    result.getInt(2),
                    result.getString(3),
                    bundesland
            );

            wahlkreise.add(wahlkreis);
        }

        return wahlkreise;
    }

    public static Wahlbeteiligung getWahlbeteiligung(ResultSet result) throws SQLException {
        result.next();

        return new Wahlbeteiligung(
                result.getInt(1),
                result.getInt(2)
        );
    }

    public static Abgeordneter getDirektmandat(ResultSet result) throws SQLException {
        result.next();

        Partei partei = new Partei(
                result.getString(6),
                result.getString(7),
                result.getString(8)
        );
        return new Abgeordneter(
                0,
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getInt(5),
                partei
        );
    }

    public static ArrayList<AnzahlStimmen> getStimmenProPartei(ResultSet result) throws SQLException {
        ArrayList<AnzahlStimmen> stimmenProPartei = new ArrayList<>();

        while(result.next()) {
            Partei partei = new Partei(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3)
            );
            AnzahlStimmen anzahlStimmen = new AnzahlStimmen(
                    partei,
                    result.getInt(4),
                    result.getFloat(5)
            );

            stimmenProPartei.add(anzahlStimmen);
        }

        return stimmenProPartei;
    }

    public static ArrayList<StimmenVergleich> getStimmenVergleiche(ResultSet result) throws SQLException {
        ArrayList<StimmenVergleich> stimmenVergleiche = new ArrayList<>();

        while(result.next()) {
            Partei partei = new Partei(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3)
            );
            StimmenVergleich stimmenVergleich = new StimmenVergleich(
                    partei,
                    result.getInt(4),
                    result.getInt(5)
            );

            stimmenVergleiche.add(stimmenVergleich);
        }

        return stimmenVergleiche;
    }

    public static Partei getPartei(ResultSet result) throws SQLException {
        result.next();

        return new Partei(
                result.getString(1),
                result.getString(2),
                result.getString(3)
        );
    }

    public static ArrayList<Ueberhangmandate> getUeberhangmandate(ResultSet result) throws SQLException {
        ArrayList<Ueberhangmandate> ueberhangmandate = new ArrayList<>();

        while(result.next()) {
            Bundesland bundesland = new Bundesland(
                    result.getString(1),
                    result.getString(2)
            );
            Partei partei = new Partei(
                    result.getString(3),
                    result.getString(4),
                    result.getString(5)
            );
            Ueberhangmandate ueberhangmandatepartei = new Ueberhangmandate(
                    bundesland,
                    partei,
                    result.getInt(6)
            );

            ueberhangmandate.add(ueberhangmandatepartei);
        }

        return ueberhangmandate;
    }

    public static ArrayList<KnappesErgebnis> getKnappeErgebnisse(ResultSet result) throws SQLException {
        ArrayList<KnappesErgebnis> knappeErgebnisse = new ArrayList<>();

        while(result.next()) {
            Partei partei = new Partei(
                    result.getString(6),
                    result.getString(7),
                    result.getString(8)
            );
            Abgeordneter abgeordneter = new Abgeordneter(
                    0,
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getInt(5),
                    partei
            );
            SiegerOderVerlierer siegerOderVerlierer;
            if(result.getString(9).equals("s"))
                siegerOderVerlierer = SiegerOderVerlierer.SIEGER;
            else siegerOderVerlierer = SiegerOderVerlierer.VERLIERER;

            KnappesErgebnis knappesErgebnis = new KnappesErgebnis(
                    abgeordneter,
                    siegerOderVerlierer,
                    result.getInt(10)
            );

            knappeErgebnisse.add(knappesErgebnis);
        }

        return knappeErgebnisse;
    }

    public static Frauenanteil getFrauenanteil(ResultSet result) throws SQLException {
        result.next();

        return new Frauenanteil(
                result.getInt(1),
                result.getInt(2)
        );
    }

    public static ArrayList<Token> getTokens(ResultSet result, int wknr) throws SQLException {
        ArrayList<Token> tokens = new ArrayList<>();

        while(result.next()) {
            boolean used;
            String gueltig = result.getString(2);
            used = !gueltig.equals("n");

            Token token = new Token(
                    result.getString(1),
                    wknr,
                    used,
                    true);
            tokens.add(token);
        }

        return tokens;
    }

    public static Token getTokenInfo(ResultSet result, String token) throws SQLException {
        if(!result.next()) {
            return new Token(token, 0, false, false);
        }
        else {
            boolean used;
            String gueltig = result.getString(3);
            used = !gueltig.equals("n");

            return new Token(
                    result.getString(1),
                    result.getInt(2),
                    used,
                    true
            );
        }
    }
}
