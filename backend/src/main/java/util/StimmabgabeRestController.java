package util;

import java.sql.*;
import java.util.ArrayList;
import databuild.DataBuilder;
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
/*
    @RequestMapping("/waehlen/verify")
    @CrossOrigin(origins = "http://localhost:4200")
    public Token verifyToken(
            @RequestParam(value="token") String token) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            String verifyTokenQuery = StimmabgabeSQL.getVerifyTokenQuery;
            PreparedStatement preparedStatement = conn.prepareStatement(verifyTokenQuery);
            preparedStatement.setString(1, token);
            preparedStatement.execute();

            return DataBuilder.getTokenInfo(preparedStatement.getResultSet());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    */

}