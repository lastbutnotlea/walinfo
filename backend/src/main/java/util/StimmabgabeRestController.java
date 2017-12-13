package util;

import java.sql.*;
import java.util.ArrayList;

import databuild.Abgeordneter;
import databuild.DataBuilder;
import databuild.Partei;
import databuild.Token;
import org.springframework.web.bind.annotation.*;
import sqlbuild.StimmabgabeSQL;


@RestController
public class StimmabgabeRestController {

    @RequestMapping("/tokens/generate")
    @CrossOrigin(origins = "http://localhost:4200")
    public void createTokens(
            @RequestParam("wknr") int wknr,
            @RequestParam("anzahl") int anzahl) {

        int counter = anzahl;

        try (Connection conn = DatabaseConnection.getConnection()) {

            while(counter > 0) {
                String tokenQuery = StimmabgabeSQL.getGenerateTokenQuery();
                PreparedStatement preparedStatement = conn.prepareStatement(tokenQuery);
                preparedStatement.execute();

                String insertTokeyQuery = StimmabgabeSQL.getInsertTokenQuery(
                        preparedStatement.getResultSet());

                PreparedStatement preparedStatement1 = conn.prepareStatement(insertTokeyQuery);
                preparedStatement1.setInt(1, wknr);
                preparedStatement1.execute();

                counter--;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

    @RequestMapping("/tokens/show")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Token> getAllTokens(
            @RequestParam(value="wknr") int wknr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String getAllTokensQuery = StimmabgabeSQL.getAllTokensQuery();
            PreparedStatement preparedStatement = conn.prepareStatement(getAllTokensQuery);
            preparedStatement.setInt(1, wknr);
            preparedStatement.execute();

            return DataBuilder.getTokens(preparedStatement.getResultSet(), wknr);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/waehlen/verify")
    @CrossOrigin(origins = "http://localhost:4200")
    public Token verifyToken(
            @RequestParam(value="token") String token) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String verifyTokenQuery = StimmabgabeSQL.getVerifyTokenQuery();
            PreparedStatement preparedStatement = conn.prepareStatement(verifyTokenQuery);
            preparedStatement.setString(1, token);
            preparedStatement.execute();

            System.out.println(preparedStatement.getResultSet());
            return DataBuilder.getTokenInfo(preparedStatement.getResultSet(), token);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/waehlen/kandidaten")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Abgeordneter> waehlbareKandidaten(
            @RequestParam(value="wknr") int wknr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String waehlbareKandidatenQuery = StimmabgabeSQL.getWaehlbareKandidatenQuery();
            PreparedStatement preparedStatement = conn.prepareStatement(waehlbareKandidatenQuery);
            preparedStatement.setInt(1, wknr);
            preparedStatement.execute();

            System.out.println(preparedStatement.getResultSet());
            return DataBuilder.getWaehlbareKandidaten(preparedStatement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/waehlen/parteien")
    @CrossOrigin(origins = "http://localhost:4200")
    public ArrayList<Partei> waehlbareParteien(
            @RequestParam(value="wknr") int wknr) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String waehlbareParteienQuery = StimmabgabeSQL.getWaehlbareParteienQuery();
            PreparedStatement preparedStatement = conn.prepareStatement(waehlbareParteienQuery);
            preparedStatement.setInt(1, wknr);
            preparedStatement.execute();

            return DataBuilder.getWaehlbareParteien(preparedStatement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/waehlen/stimmabgabe")
    @CrossOrigin(origins = "http://localhost:4200")
    public boolean stimmabgabe(
            @RequestParam(value="token") String token,
            @RequestParam(value="kandidatenid") int kandidatenId,
            @RequestParam(value="parteiid") int parteiId) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String tokenOkayQuery = StimmabgabeSQL.getTokenOkayQuery();
            PreparedStatement p1 = conn.prepareStatement(tokenOkayQuery);
            p1.setString(1, token);
            p1.execute();

            if(p1.getResultSet().next()) {
                int wkid = p1.getResultSet().getInt(1);

                if(kandidatenId >= 0) {
                    String kandidatOkayQuery = StimmabgabeSQL.getKandidateOkayQuery();
                    PreparedStatement p2 = conn.prepareStatement(kandidatOkayQuery);
                    p2.setInt(1, wkid);
                    p2.setInt(2, kandidatenId);
                    p2.execute();

                    if(p2.getResultSet().next()) {
                        String waehleKandidatQuery = StimmabgabeSQL.getWaehleKandidatQuery();
                        PreparedStatement p3 = conn.prepareStatement(waehleKandidatQuery);
                        p3.setInt(1, kandidatenId);
                        p3.setInt(2, wkid);
                        p3.execute();
                    }

                    else return false;
                }
                else {
                    String waehleKandidatQuery = StimmabgabeSQL.getWaehleKandidatQuery();
                    PreparedStatement p4 = conn.prepareStatement(waehleKandidatQuery);
                    p4.setString(1, null);
                    p4.setInt(2, wkid);
                    p4.execute();
                }

                if(parteiId >= 0) {
                    String parteiOkayQuery = StimmabgabeSQL.getParteiOkayQuery();
                    PreparedStatement p5 = conn.prepareStatement(parteiOkayQuery);
                    p5.setInt(1, wkid);
                    p5.setInt(2, parteiId);
                    p5.execute();

                    if(p5.getResultSet().next()) {
                        String waehleParteiQuery = StimmabgabeSQL.getWaehleParteiQuery();
                        PreparedStatement p6 = conn.prepareStatement(waehleParteiQuery);
                        p6.setInt(1, parteiId);
                        p6.setInt(2, wkid);
                        p6.execute();
                    }

                    else return false;
                }
                else {
                    String waehleParteiQuery = StimmabgabeSQL.getWaehleParteiQuery();
                    PreparedStatement p7 = conn.prepareStatement(waehleParteiQuery);
                    p7.setString(1, null);
                    p7.setInt(2, wkid);
                    p7.execute();
                }

                // wenn Code bis hier ausgeführt wurde, wurde gewählt und Token verwendet
                String tokenVerwendetQuery = StimmabgabeSQL.getTokenVerwendetQuery();
                PreparedStatement p8 = conn.prepareStatement(tokenVerwendetQuery);
                p8.setString(1, token);
                p8.execute();

                return true;

            }
            else return false;
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}