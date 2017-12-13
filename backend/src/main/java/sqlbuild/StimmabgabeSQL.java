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

    public static String getWaehlbareKandidatenQuery() {
        return "SELECT " +
                "  k.id, " +
                "  k.titel, " +
                "  k.name, " +
                "  k.vorname, " +
                "  k.namenszusatz, " +
                "  k.geburtsjahr, " +
                "  p.id, " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe " +
                "FROM kandidaten k, wahlkreise wk, parteien p " +
                "WHERE k.wahlkreis_id = wk.id " +
                "  AND k.partei_id = p.id " +
                "AND wk.nummer = ?;"
                ;
    }

    public static String getWaehlbareParteienQuery() {
        return "SELECT DISTINCT  " +
                "  p.id, " +
                "  p.kuerzel, " +
                "  p.name, " +
                "  p.farbe " +
                "  FROM parteien p, wahlkreise wk, zweitstimmenergebnisse z " +
                "WHERE z.wahlkreis_id = wk.id " +
                "AND z.partei_id = p.id " +
                "AND wk.nummer = ?;"
                ;
    }
}
