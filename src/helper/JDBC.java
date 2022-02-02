package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public abstract class JDBC {

    private static ResourceBundle databaseEnv = ResourceBundle.getBundle("locale/database");

    /**
     * Variables for connecting to the database. The username and password variable were stored in the database.properties file
     * as a security measure. This prevents the username and password from being exposed in the code.
     */
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = databaseEnv.getString("userName"); // Username
    private static String password = databaseEnv.getString("password"); // Password
    public static Connection connection;  // Connection Interface

    /**
     * This method is called to establish and open the connection to the database.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * This method is called to close the connection to the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
