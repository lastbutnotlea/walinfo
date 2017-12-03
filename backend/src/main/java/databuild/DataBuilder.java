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
                    result.getString(2));
            Sitze sitze = new Sitze(
                    partei,
                    result.getInt(3));

            sitzverteilung.add(sitze);
        }

        return sitzverteilung;
    }

    public static ArrayList<Abgeordneter> getMitgliederList(ResultSet result) throws SQLException {
        ArrayList<Abgeordneter> mitglieder = new ArrayList<>();

        while (result.next()) {
            Partei partei = new Partei(
                    result.getString(6),
                    result.getString(7));
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
            Wahlkreis wahlkreis = new Wahlkreis(
                    result.getInt(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getString(4)
            );

            wahlkreise.add(wahlkreis);
        }

        return wahlkreise;
    }

    public static Wahlbeteiligung getWahlbeteiligung(ResultSet result) throws SQLException {
        result.next();

        Wahlkreis wahlkreis = new Wahlkreis(
                result.getInt(1),
                result.getInt(2),
                result.getString(3),
                result.getString(4)
        );
        return new Wahlbeteiligung(
                wahlkreis,
                result.getInt(5),
                result.getInt(6)
        );
    }

    public static Abgeordneter getDirektmandat(ResultSet result) throws SQLException {
        result.next();

        Partei partei = new Partei(
                result.getString(6),
                result.getString(7)
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

}