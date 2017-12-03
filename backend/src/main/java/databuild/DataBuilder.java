package databuild;

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

}
