package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import login.LoginController;

public final class Variables {
    private static Integer userID;
    public static Connection connection;
    public static PreparedStatement ps;
    private static String username;

    public static Integer getUserID() {
        return userID;
    }

    public static void setUserName(String username) { Variables.username = username; }
    public static String getUserName() {
    	return username;
    }

    public static void setUserID(Integer userID) {
        Variables.userID = userID;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Variables.connection = connection;
    }

    public static PreparedStatement getPreparedStatement() {
        return ps;
    }

    public static void setPreparedStatement(PreparedStatement ps) {
        Variables.ps = ps;
    }
}
