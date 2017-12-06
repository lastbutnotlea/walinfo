package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import databuild.*;
import org.springframework.web.bind.annotation.*;
import sqlbuild.BundestagSQL;
import sqlbuild.WahlanalysenSQL;
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

    @RequestMapping("/wahlkreise/erststimmenpropartei")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<AnzahlStimmen> wkErstStimmenProPartei (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        String wkStimmenProParteiQuery = WahlkreiseSQL.getWkErststimmenProParteiQuery(
                jahr,
                wknr,
                modus);

        return wkStimmenProParteiAux(wkStimmenProParteiQuery);
    }

    @RequestMapping("/wahlkreise/zweitstimmenpropartei")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<AnzahlStimmen> wkZweitstimmenProPartei (
            @RequestParam("jahr") int jahr,
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        String wkStimmenProParteiQuery = WahlkreiseSQL.getWkZweitstimmenProParteiQuery(
                jahr,
                wknr,
                modus);

        return wkStimmenProParteiAux(wkStimmenProParteiQuery);
    }

    private ArrayList<AnzahlStimmen> wkStimmenProParteiAux (String query) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(query);

            return DataBuilder.getStimmenProPartei(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise/vergleichvorjahr/erststimmen")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<StimmenVergleich> wkVergleichVorjahrErststimmen (
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        String wkVergleichVorjahrQuery = WahlkreiseSQL.getWkVergleichVorjahrErstQuery(
                wknr,
                modus);

        return wkVergleichVorjahrAux(wkVergleichVorjahrQuery);
    }

    @RequestMapping("/wahlkreise/vergleichvorjahr/zweitstimmen")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<StimmenVergleich> wkVergleichVorjahrZweitstimmen (
            @RequestParam("wknr") int wknr,
            @RequestParam("modus") String modus) {

        String wkVergleichVorjahrQuery = WahlkreiseSQL.getWkVergleichVorjahrZweitQuery(
                wknr,
                modus);

        return wkVergleichVorjahrAux(wkVergleichVorjahrQuery);
    }

    private ArrayList<StimmenVergleich> wkVergleichVorjahrAux (String query) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(query);

            return DataBuilder.getStimmenVergleiche(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

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

    @RequestMapping("/knappstesieger")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<KnappesErgebnis> knappsteSieger (
            @RequestParam("jahr") int jahr,
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String knappsteSiegerQuery = WahlanalysenSQL.getKnappsteSiegerQuery(
                    jahr,
                    modus);
            statement.execute(knappsteSiegerQuery);

            return DataBuilder.getKnappeErgebnisse(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/wahlkreise/frauenquote")
    @CrossOrigin(origins = "http://localhost:4200")
    public Frauenanteil knappsteSieger (
            @RequestParam("wknr") int wknr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String frauenanteilSQL = WahlkreiseSQL.getWkFrauenanteil(
                    wknr);
            statement.execute(frauenanteilSQL);

            return DataBuilder.getFrauenanteil(statement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/frauenbonus")
    @CrossOrigin(origins = "http://localhost:4200")
    public double frauenbonus (
            @RequestParam("modus") String modus) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement statement = conn.createStatement();
            String frauenbonusQuery = WahlanalysenSQL.getFrauenbonus(
                    modus);
            statement.execute(frauenbonusQuery);

            ResultSet result = statement.getResultSet();
            result.next();
            return result.getDouble(1);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}