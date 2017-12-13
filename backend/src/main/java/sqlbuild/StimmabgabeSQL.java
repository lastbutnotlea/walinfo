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
                " AND k.wahljahr = 2017 " +
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
                "AND p.wahljahr = 2017 " +
                "AND wk.nummer = ?;"
                ;
    }

    public static String getTokenOkayQuery() {
        return "SELECT wk.id " +
                "FROM tokens, wahlkreise wk " +
                "WHERE verwendet = 'n' " +
                "AND tokens.wahlkreis_nr = wk.nummer " +
                "AND wk.wahljahr = 2017 " +
                "AND token = ?;"
                ;
    }

    public static String getKandidateOkayQuery() {
        return "SELECT * " +
                "FROM kandidaten k " +
                "WHERE k.wahlkreis_id = ? " +
                "AND k.id = ?;"
                ;
    }

    public static String getWaehleKandidatQuery() {
        return "INSERT INTO erststimmen VALUES (?, ?);"
                ;
    }

    public static String getParteiOkayQuery() {
        return "SELECT * " +
                "FROM parteien p, zweitstimmenergebnisse z " +
                "WHERE z.partei_id = p.id " +
                "AND z.wahlkreis_id = ? " +
                "AND p.id = ?;"
                ;
    }

    public static String getWaehleParteiQuery() {
        return "INSERT INTO zweitstimmen VALUES(?, ?);"
                ;
    }

    public static String getTokenVerwendetQuery() {
        return "UPDATE tokens SET " +
                "  verwendet = 'j' WHERE token = ?;"
                ;
    }
}
