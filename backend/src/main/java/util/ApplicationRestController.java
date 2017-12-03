package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import databuild.*;
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
            @RequestParam("jahr") int jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String sitzverteilungQuery = BundestagSQL.getSitzverteilungQuery(jahr, modus);
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
            @RequestParam("jahr") int jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String mitgliederQuery = BundestagSQL.getBundestagQuery(jahr, modus);
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
            @RequestParam("jahr") int jahr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wahlkreisQuery = WahlkreiseSQL.getWahlkreisQuery(jahr);
            statement.execute(wahlkreisQuery);

            return DataBuilder.getWahlkreisList(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise/wahlbeteiligung")
    @CrossOrigin(origins = "http://localhost:4200")
    public Wahlbeteiligung wkWahlbeteiligung(
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkWahlbeteiligungQuery = WahlkreiseSQL.getWkWahlbeteiligungQuery(
                    jahr,
                    wknr,
                    modus);
            statement.execute(wkWahlbeteiligungQuery);

            return DataBuilder.getWahlbeteiligung(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise/direktmandat")
    @CrossOrigin(origins = "http://localhost:4200")
    public Abgeordneter wkDirektmandat(
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkDirektmandatQuery = WahlkreiseSQL.getWkDirektmandatQuery(
                    jahr,
                    wknr,
                    modus);
            statement.execute(wkDirektmandatQuery);

            return DataBuilder.getDirektmandat(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise/stimmenpropartei")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<AnzahlStimmen> wkStimmenProPartei (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkStimmenProParteiQuery = WahlkreiseSQL.getWkStimmenProParteiQuery(
                    jahr,
                    wknr,
                    modus);
            statement.execute(wkStimmenProParteiQuery);

            return DataBuilder.getStimmenProPartei(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}