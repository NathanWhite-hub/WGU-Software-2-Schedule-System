package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authenticator {

    /**
     * This method is called to validate the user. If the input password matches what is found on the database, then it returns true,
     * else, it will return false, and the user will not be validated.
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean isValidUser(String userName, String password) throws SQLException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Password FROM users WHERE User_Name ='" + userName + "'";
        ResultSet findUser = statement.executeQuery(sqlStatement);

        while(findUser.next()){
            if(findUser.getString("password").equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to get the user ID from the database. The input userName is used to find the userID and if found,
     * it will return the ID.
     * @param userName
     * @return
     * @throws SQLException
     */
    public static int getUserID(String userName) throws SQLException{
        int dbUserID = 0;
        Statement statement = JDBC.connection.createStatement();
        PreparedStatement sqlStatement = JDBC.connection.prepareStatement("SELECT * FROM users WHERE User_Name = ?");
        sqlStatement.setString(1, userName);
        ResultSet findUser = sqlStatement.executeQuery();

        while(findUser.next()){
            dbUserID = findUser.getInt("User_ID");
        }
        return dbUserID;
    }

    /**
     * Identical to the method above, however, it will attempt to find a userName associated with the userID. It will then return
     * the found userName.
     * @param userID
     * @return
     * @throws SQLException
     */
    public static String getUserName(int userID) throws SQLException{
        String userName = "";
        Statement statement = JDBC.connection.createStatement();
        PreparedStatement sqlStatement = JDBC.connection.prepareStatement("SELECT * FROM users WHERE User_ID = ?");
        sqlStatement.setInt(1, userID);
        ResultSet findUserName = sqlStatement.executeQuery();

        while(findUserName.next()){
            userName = findUserName.getString("User_Name");
        }
        return userName;
    }
}
