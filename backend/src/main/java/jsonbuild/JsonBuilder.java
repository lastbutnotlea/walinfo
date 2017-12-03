package jsonbuild;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JsonBuilder {

    public static ArrayList<Sitze> getSitzverteilungJson(ResultSet result) throws SQLException {
        ArrayList<Sitze> sitzverteilung = new ArrayList<>();

        while(result.next()) {
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

    public static ArrayList<Abgeordneter> getMitgliederJson(ResultSet result) throws SQLException {
        ArrayList<Abgeordneter> mitglieder = new ArrayList<>();

        while(result.next()) {
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

}
