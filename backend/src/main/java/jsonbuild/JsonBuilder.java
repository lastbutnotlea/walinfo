package jsonbuild;

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


}
