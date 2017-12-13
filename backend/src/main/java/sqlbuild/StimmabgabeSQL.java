package sqlbuild;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StimmabgabeSQL {

    public static String getGenerateTokenQuery() {
        return "SELECT * " +
                "FROM words " +
                "ORDER BY random() " +
                "limit 10;"
                ;
    }

    public static String getInsertTokenQuery(ResultSet result) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstLoop = true;
        while(result.next()) {
            if(firstLoop) {
                stringBuilder.append(result.getString(2));
                firstLoop = false;
            }
            else {
                stringBuilder.append("-");
                stringBuilder.append(result.getString(2));
            }
        }
        System.out.println(stringBuilder.toString());
        return "INSERT INTO tokens (token, wahlkreis_nr, verwendet) VALUES " +
                "  ('" + stringBuilder.toString() + "', ?, 'n');";
    }

    public static String getAllTokensQuery() {
        return "SELECT token, verwendet " +
                "FROM tokens " +
                "WHERE wahlkreis_nr = ?;"
                ;
    }

    public static String getVerifyTokenQuery() {
        return "SELECT * " +
                "FROM tokens " +
                "WHERE token = ?;"
                ;
    }
}
