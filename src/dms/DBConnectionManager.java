package dms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the MySQL database connection for the application using the singleton pattern.
 * Ensures only one shared connection is used throughout the app.
 *
 * <p>Responsibilities include:
 * <ul>
 *     <li>Creating a connection with given credentials</li>
 *     <li>Providing access to the shared connection</li>
 *     <li>Closing the connection cleanly</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class DBConnectionManager {

    /** Singleton instance of DBConnectionManager */
    private static DBConnectionManager instance;

    /** Static reference to the shared database connection */
    private static Connection connection;

    /**
     * Private constructor to prevent external instantiation.
     * Used only internally to enforce singleton pattern.
     */
    private DBConnectionManager() {}

    /**
     * Returns the singleton instance of DBConnectionManager.
     * Instantiates the instance if it does not yet exist.
     *
     * @return singleton instance of DBConnectionManager
     */
    public static DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    /**
     * Establishes and returns a new database connection using the provided credentials.
     * Stores the connection for reuse.
     *
     * @param host     the database host (e.g., "localhost")
     * @param port     the database port (e.g., "3306")
     * @param dbName   the name of the database to connect to
     * @param user     the MySQL username
     * @param password the MySQL password
     * @return a valid and active database connection
     * @throws SQLException if the connection fails
     */
    public Connection connect(String host, String port, String dbName, String user, String password) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * Sets the internal shared connection manually (typically after a successful login).
     *
     * @param conn an externally created and validated Connection object
     */
    public static void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Returns the currently stored database connection.
     *
     * @return Connection object or null if no connection has been established
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the stored database connection safely.
     * If no connection exists or it is already closed, this method does nothing.
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
