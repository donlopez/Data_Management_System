package dms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to encapsulate all database operations for the DMS (Data Management System).
 * Provides helper methods to retrieve records from the normalized MySQL schema, including:
 * <ul>
 *     <li>Customer table</li>
 *     <li>Shipper table</li>
 *     <li>ShippingOrder table (with JOINs for customer and shipper names)</li>
 * </ul>
 *
 * <p>Note: shipping_cost is not stored in the database—it is calculated in Java.</p>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class DatabaseHelper {

    /** Active database connection for running prepared SQL queries */
    private final Connection conn;

    /**
     * Constructs a DatabaseHelper object using the provided JDBC connection.
     *
     * @param conn the active JDBC connection to use for all database operations
     */
    public DatabaseHelper(Connection conn) {
        this.conn = conn;
    }

    /**
     * Fetches all customer records from the Customer table.
     *
     * @return a list of Customer objects loaded from the database
     * @throws SQLException if a database access error occurs
     */
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        }

        return list;
    }

    /**
     * Fetches all shipper records from the Shipper table.
     *
     * @return a list of Shipper objects loaded from the database
     * @throws SQLException if a database access error occurs
     */
    public List<Shipper> getAllShippers() throws SQLException {
        List<Shipper> list = new ArrayList<>();
        String sql = "SELECT * FROM Shipper";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Shipper(
                        rs.getInt("shipper_id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        }

        return list;
    }

    /**
     * Retrieves all shipping orders from the ShippingOrder table,
     * including customer and shipper names using JOIN operations.
     *
     * @return a list of ShippingOrder objects, each enriched with joined customer/shipper names
     * @throws SQLException if a database access error occurs
     */
    public List<ShippingOrder> getAllShippingOrders() throws SQLException {
        List<ShippingOrder> list = new ArrayList<>();

        // SQL query with JOINs to bring in customer and shipper names for display
        String sql = """
            SELECT so.order_id, so.customer_id, so.shipper_id,
                   so.weight_in_pounds, so.distance_in_miles,
                   c.name AS customer_name, s.name AS shipper_name
            FROM ShippingOrder so
            JOIN Customer c ON so.customer_id = c.customer_id
            JOIN Shipper s ON so.shipper_id = s.shipper_id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new ShippingOrder(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("shipper_id"),
                        rs.getDouble("weight_in_pounds"),
                        rs.getInt("distance_in_miles"),
                        rs.getString("customer_name"),
                        rs.getString("shipper_name")
                ));
            }
        }

        return list;
    }
}
