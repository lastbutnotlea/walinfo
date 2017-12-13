package util;

import java.sql.*;
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

                preparedStatement.getResultSet().next();

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



}