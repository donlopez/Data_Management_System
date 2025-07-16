package dms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to encapsulate all database operations (SELECT, INSERT, etc.).
 * Provides helper methods to fetch data from the Customer, Shipper, and ShippingOrder tables.
 *
 * This version matches the normalized MySQL schema:
 * - customer_id and shipper_id are foreign keys in ShippingOrder
 * - shipping_cost is calculated in Java, not stored
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class DatabaseHelper {

    private final Connection conn;

    /**
     * Constructor with active database connection.
     * @param conn the active JDBC connection to use for queries
     */
    public DatabaseHelper(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all customer records from the database.
     *
     * @return list of Customer objects
     * @throws SQLException if any database error occurs
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
     * Retrieves all shipper records from the database.
     *
     * @return list of Shipper objects
     * @throws SQLException if any database error occurs
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
     * Retrieves all shipping orders and joins with customer and shipper names for display.
     *
     * @return list of ShippingOrder objects
     * @throws SQLException if any database error occurs
     */
    public List<ShippingOrder> getAllShippingOrders() throws SQLException {
        List<ShippingOrder> list = new ArrayList<>();

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
