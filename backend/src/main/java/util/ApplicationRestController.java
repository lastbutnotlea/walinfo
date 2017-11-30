package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import hello.Greeting;
import jsonbuild.JsonBuilder;
import jsonbuild.Sitze;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sqlbuild.Bundestag;

@RestController
public class ApplicationRestController {

    @RequestMapping("/test")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Greeting> greeting() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();
        Statement statement = conn.createStatement();

        statement.execute("WITH stimmen_gesamt AS (\n" +
                "    SELECT\n" +
                "      sum(zweit.anzahl) + sum(erst.anzahl) AS gesamtstimmen,\n" +
                "      erst.wahlkreis_id,\n" +
                "      k.wahljahr\n" +
                "    FROM erststimmenergebnisse erst, zweitstimmenergebnisse zweit, parteien p, kandidaten k\n" +
                "    WHERE zweit.partei_id = p.id\n" +
                "          AND erst.wahlkreis_id = zweit.wahlkreis_id\n" +
                "          AND k.id = erst.kandidaten_id\n" +
                "          AND k.partei_id = p.id\n" +
                "          AND zweit.partei_id IS NOT NULL\n" +
                "          AND erst.kandidaten_id IS NOT NULL\n" +
                "    GROUP BY erst.wahlkreis_id, k.wahljahr\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "  erst.wahlkreis_id,\n" +
                "  p.kuerzel,\n" +
                "  erst.anzahl + zweit.anzahl AS anzahl_absolut,\n" +
                "  CAST(erst.anzahl + zweit.anzahl AS NUMERIC) /\n" +
                "  (CAST((\n" +
                "          SELECT gesamtstimmen\n" +
                "          FROM stimmen_gesamt sg\n" +
                "          WHERE sg.wahljahr = p.wahljahr\n" +
                "                AND sg.wahlkreis_id = erst.wahlkreis_id\n" +
                "        ) AS NUMERIC))       AS anzahl_relativ,\n" +
                "\n" +
                "  k.wahljahr\n" +
                "FROM erststimmenergebnisse erst, zweitstimmenergebnisse zweit,\n" +
                "  parteien p, kandidaten k\n" +
                "WHERE erst.wahlkreis_id = zweit.wahlkreis_id\n" +
                "      AND erst.kandidaten_id = k.id\n" +
                "      AND zweit.partei_id = p.id\n" +
                "      AND k.partei_id = p.id\n" +
                "      AND zweit.partei_id IS NOT NULL\n" +
                "      AND erst.kandidaten_id IS NOT NULL\n" +
                "ORDER BY k.wahljahr, k.wahlkreis_id;");

       // statement.execute("select count(*) from kandidaten");

        ResultSet result = statement.getResultSet();

        ArrayList<Greeting> tupel = new ArrayList<>();
        while (result.next()) {
            Greeting greeting = new Greeting(
                    result.getInt(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getInt(5)
            );
            tupel.add(greeting);
        }

        conn.close();

        return tupel;
        //return new Greeting(tupel);
    }

    @RequestMapping("/bundestag/sitzverteilung")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Sitze> sitzverteilung() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();
        Statement statement = conn.createStatement();

        String sitzverteilungQuery = Bundestag.getSitzverteilungQuery(2013, "aggr");

        statement.execute(sitzverteilungQuery);

        ArrayList<Sitze> sitzverteilung = JsonBuilder.getSitzverteilungJson(statement.getResultSet());

        conn.close();

        return sitzverteilung;
    }
}