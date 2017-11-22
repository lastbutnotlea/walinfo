package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    /*private static ComboPooledDataSource cpds;

    static {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        cpds.setJdbcUrl( "jdbc:postgresql://localhost:5433/walinfo" );
        cpds.setUser("postgres");
        cpds.setPassword("mysecretpassword");
    }

    public static Connection acquireConnection() throws SQLException {
        return cpds.getConnection();
    }*/

    public static Connection getConnection() {
        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            throw new RuntimeException(e);
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/walinfo", "postgres",
                    "mysecretpassword");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            throw new RuntimeException(e);
        }
        if (connection == null) {
            System.out.println("Failed to make connection!");
        }

        return connection;
    }
}
