package main.java.db;

import java.sql.*;
import java.util.Properties;

/**
 * <h1>DATABASE CONNECTION PROVIDER</h1>
 * <p>This class provides connection of the database</p>
 */

public class DatabaseManager {
    private Properties properties;
    private static final String MAX_POOL = "250";
    Connection connection = null;


    public Connection connect(){
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Raccoons", getProperties());

            } catch (SQLException throwable) {
                throwable.printStackTrace();
                System.out.println("[DATABASE] NO CONNECTION!");
            }
        }
        return connection;
    }
    // Properties for a connection
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", MyData.getUsername());
            properties.setProperty("password", MyData.getPass());
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // Disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
