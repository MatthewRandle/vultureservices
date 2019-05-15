package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import jobcard.JobController;
import userAccount.UserAccountController;

public final class Variables {
    private static Integer userID;
    public static Connection connection;
    public static PreparedStatement ps;
    private static String username;
    private static String userType;
    private static UserAccountController userAccountController;
    private static JobController jobController;

    public static Integer getUserID() {
        return userID;
    }
    public static void setUserID(Integer userID) {
        Variables.userID = userID;
    }

    public static void setUserName(String username) { Variables.username = username; }
    public static String getUserName() { return username; }

    public static void setUserType(String userType) { Variables.userType = userType; }
    public static String getUserType() {
        return userType;
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

    public static UserAccountController getUserAccountController() { return userAccountController; }
    public static void setUserAccountController(UserAccountController userAccountController) { Variables.userAccountController = userAccountController; }
    
    public static JobController getJobController() { return jobController; }
    public static void setJobController(JobController jobController) { Variables.jobController = jobController; }
}
