/**
 * Created by Lea on 08.11.17.
 */
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresConnection {

    public static void main(String[] argv) throws SQLException {

        Connection dbConnection = connectToDatabase();
        if(dbConnection != null) {
            fillTables(dbConnection);
        }
    }

    public static void fillTables(Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        String fill1 = "insert into professoren values" +
                "       (2125, 'Sokrates', 'C4', 226)";
        statement.executeUpdate(fill1);
    }

    public static Connection connectToDatabase() {

        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return null;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5433/walinfo", "postgres",
                    "mysecretpassword");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            return connection;
        } else {
            System.out.println("Failed to make connection!");
            return null;
        }
    }

}