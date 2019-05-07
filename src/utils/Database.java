package utils;
import config.Keys;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(Keys.databaseURL, Keys.databaseUser, Keys.databasePassword);
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        catch (SQLException ex) {
            System.out.println("Failed to create the database connection.");
        }
        return con;
    }
}
