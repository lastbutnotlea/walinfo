package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import databuild.*;
import org.springframework.web.bind.annotation.*;
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
        return wahlkreise(jahr, null);
    }

    @RequestMapping("/wahlkreise/{nummer}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Wahlkreis> wahlkreise(
            @RequestParam("jahr") int jahr,
            @PathVariable(value="nummer") Integer wknr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wahlkreisQuery = WahlkreiseSQL.getWahlkreisQuery(jahr, wknr);
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

    /*
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
    */

    @RequestMapping("/wahlkreise/erststimmenpropartei")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<AnzahlStimmen> wkErstStimmenProPartei (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkStimmenProParteiQuery = WahlkreiseSQL.getWkErststimmenProParteiQuery(
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

    @RequestMapping("/wahlkreise/zweitstimmenpropartei")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<AnzahlStimmen> wkZweitstimmenProPartei (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkStimmenProParteiQuery = WahlkreiseSQL.getWkZweitstimmenProParteiQuery(
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

    @RequestMapping("/wahlkreise/vergleichvorjahr")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<StimmenVergleich> wkVergleichVorjahr (
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkVergleichVorjahrQuery = WahlkreiseSQL.getWkVergleichVorjahrQuery(
                    wknr,
                    modus);
            statement.execute(wkVergleichVorjahrQuery);

            return DataBuilder.getStimmenVergleiche(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    @RequestMapping("/wahlkreise/siegererststimmen")
    @CrossOrigin(origins = "http://localhost:4200")
    public Partei wkSiegerErststimmen (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkSiegerErststimmen = WahlkreiseSQL.getWkSiegerErststimmenQuery(
                    jahr,
                    wknr,
                    modus);
            statement.execute(wkSiegerErststimmen);

            return DataBuilder.getPartei(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    */

    @RequestMapping("/wahlkreise/siegerzweitstimmen")
    @CrossOrigin(origins = "http://localhost:4200")
    public Partei wkSiegerZweitstimmen (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String wkSiegerZweitstimmen = WahlkreiseSQL.getWkSiegerZweitstimmenQuery(
                    jahr,
                    wknr,
                    modus);
            statement.execute(wkSiegerZweitstimmen);

            return DataBuilder.getPartei(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/bundestag/ueberhangmandate")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Ueberhangmandate> ueberhangmandate (
            @RequestParam("jahr") int jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String ueberhangmandateQuery = BundestagSQL.getUeberhangmandateQuery(
                    jahr,
                    modus);
            statement.execute(ueberhangmandateQuery);

            return DataBuilder.getUeberhangmandate(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}