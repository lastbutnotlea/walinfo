package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import databuild.Abgeordneter;
import databuild.DataBuilder;
import databuild.Sitze;
import databuild.Wahlkreis;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sqlbuild.BundestagSQL;
import sqlbuild.WahlkreiseSQL;

@RestController
public class ApplicationRestController {

    @RequestMapping("/bundestag/sitzverteilung")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Sitze> sitzverteilung(
            @RequestParam("jahr") String jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String sitzverteilungQuery = BundestagSQL.getSitzverteilungQuery(Integer.parseInt(jahr), modus);
            statement.execute(sitzverteilungQuery);

            return DataBuilder.getSitzverteilungList(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/bundestag/mitglieder")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Abgeordneter> mitgliederBundestag(
            @RequestParam("jahr") String jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String mitgliederQuery = BundestagSQL.getBundestagQuery(Integer.parseInt(jahr), modus);
            statement.execute(mitgliederQuery);

            return DataBuilder.getMitgliederList(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Wahlkreis> wahlkreise(
            @RequestParam("jahr") String jahr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wahlkreisQuery = WahlkreiseSQL.getWahlkreisQuery(Integer.parseInt(jahr));
            statement.execute(wahlkreisQuery);

            return DataBuilder.getWahlkreisList(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}