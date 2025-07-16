package dms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the MySQL database connection for the application.
 * Uses a singleton pattern to ensure a single shared connection.
 */
public class DBConnectionManager {
    private static DBConnectionManager instance;
    private static Connection connection;

    // Private constructor to prevent external instantiation
    private DBConnectionManager() {}

    /**
     * Returns the singleton instance of DBConnectionManager.
     *
     * @return singleton instance
     */
    public static DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    /**
     * Establishes and stores a connection to the database.
     *
     * @param host     database host (e.g., "localhost")
     * @param port     port number (e.g., "3306")
     * @param dbName   database name (e.g., "ShippingDMS")
     * @param user     username
     * @param password password
     * @return active connection
     * @throws SQLException if connection fails
     */
    public Connection connect(String host, String port, String dbName, String user, String password) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * Sets the connection manually (used after login).
     *
     * @param conn externally established connection
     */
    public static void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Returns the currently stored connection.
     *
     * @return Connection object or null if not connected
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the stored database connection, if open.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
