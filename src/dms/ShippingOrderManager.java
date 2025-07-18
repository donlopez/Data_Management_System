package dms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Manager class for handling all operations related to ShippingOrder records.
 * Supports full CRUD operations, loading orders from files, and automatic data validation.
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *     <li>Inserting and updating orders using foreign key relationships</li>
 *     <li>Validating customer and shipper names</li>
 *     <li>Auto-creating customer/shipper records if they don't exist</li>
 *     <li>Calculating shipping cost in Java instead of SQL</li>
 *     <li>Loading data with JOINs for display</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class ShippingOrderManager {

    /** In-memory list of all shipping orders (used for display and search) */
    private final List<ShippingOrder> orders;

    /**
     * Constructor initializes the order list and loads data from the database.
     */
    public ShippingOrderManager() {
        orders = new ArrayList<>();
        loadOrdersFromDatabase();
    }

    /**
     * Adds a new order after validating and inserting related customer and shipper records.
     *
     * @param customerName customer name (validated and stored)
     * @param shipperName shipper name (validated and stored)
     * @param weight shipment weight in pounds
     * @param distance shipping distance in miles
     * @return true if added successfully, false otherwise
     */
    public boolean addOrder(String customerName, String shipperName, double weight, int distance) {
        if (!isValidName(customerName) || !isValidName(shipperName)) return false;
        if (weight <= 0 || weight > 150 || distance <= 0 || distance > 3000) return false;

        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                System.err.println("Add failed: DB connection is closed or null.");
                return false;
            }

            int customerId = getOrInsertCustomerId(conn, customerName);
            int shipperId = getOrInsertShipperId(conn, shipperName);
            double cost = calculateShippingCost(weight, distance);

            String sql = """
                INSERT INTO ShippingOrder (customer_id, shipper_id, weight_in_pounds, distance_in_miles, shipping_cost)
                VALUES (?, ?, ?, ?, ?)
            """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, shipperId);
                stmt.setDouble(3, weight);
                stmt.setInt(4, distance);
                stmt.setDouble(5, cost);
                stmt.executeUpdate();
            }

            loadOrdersFromDatabase();
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing shipping order's weight, distance, and recalculated cost.
     *
     * @param orderId order ID to update
     * @param weight new weight
     * @param distance new distance
     * @return true if updated successfully, false otherwise
     */
    public boolean updateOrder(int orderId, double weight, int distance) {
        if (weight <= 0 || weight > 150 || distance <= 0 || distance > 3000) return false;

        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                System.err.println("Update failed: DB connection is closed or null.");
                return false;
            }

            ShippingOrder order = findOrder(orderId);
            if (order == null) {
                System.err.println("Update failed: order not found.");
                return false;
            }

            double cost = calculateShippingCost(weight, distance);

            String sql = """
                UPDATE ShippingOrder
                SET weight_in_pounds = ?, distance_in_miles = ?, shipping_cost = ?
                WHERE order_id = ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, weight);
                stmt.setInt(2, distance);
                stmt.setDouble(3, cost);
                stmt.setInt(4, orderId);
                stmt.executeUpdate();
            }

            loadOrdersFromDatabase();
            return true;

        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an order from the database by ID.
     *
     * @param id order ID to delete
     * @return true if deleted, false if not found or failed
     */
    public boolean deleteOrder(int id) {
        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                System.err.println("Delete failed: DB connection is closed or null.");
                return false;
            }

            String sql = "DELETE FROM ShippingOrder WHERE order_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    loadOrdersFromDatabase();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
        return false;
    }

    /**
     * Finds an order by ID from the in-memory list.
     *
     * @param id the order ID to search
     * @return ShippingOrder if found, otherwise null
     */
    public ShippingOrder findOrder(int id) {
        for (ShippingOrder order : orders) {
            if (order.getOrderId() == id) return order;
        }
        return null;
    }

    /**
     * Returns the full list of shipping orders currently held in memory.
     *
     * @return a list of all ShippingOrder objects loaded from the database
     */
    public List<ShippingOrder> getAllOrders() {
        return orders;
    }

    /**
     * Loads orders from a file with structured pipe-delimited format.
     * Each line should have 5 fields: ID | Customer | Shipper | Weight | Distance
     *
     * @param filename path to the .txt file
     */
    public void loadOrdersFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String customerName = parts[1].trim();
                    String shipperName = parts[2].trim();
                    double weight = Double.parseDouble(parts[3].trim());
                    int distance = Integer.parseInt(parts[4].trim());
                    addOrder(customerName, shipperName, weight, distance);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading orders from file: " + e.getMessage());
        }
    }

    private int getOrInsertCustomerId(Connection conn, String name) throws SQLException {
        if (!isValidName(name)) throw new SQLException("Invalid customer name.");

        String select = "SELECT customer_id FROM Customer WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("customer_id");
        }

        String insert = "INSERT INTO Customer (name, email, phone) VALUES (?, '', '')";
        try (PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }

        throw new SQLException("Failed to insert or fetch customer.");
    }

    private int getOrInsertShipperId(Connection conn, String name) throws SQLException {
        if (!isValidName(name)) throw new SQLException("Invalid shipper name.");

        String select = "SELECT shipper_id FROM Shipper WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("shipper_id");
        }

        String insert = "INSERT INTO Shipper (name, phone) VALUES (?, '')";
        try (PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }

        throw new SQLException("Failed to insert or fetch shipper.");
    }

    private double calculateShippingCost(double weight, int distance) {
        return Math.round(weight * distance * 0.0015 * 100.0) / 100.0;
    }

    private boolean isValidName(String name) {
        return name != null && name.length() <= 30 && !name.matches(".*\\d.*");
    }

    private void loadOrdersFromDatabase() {
        orders.clear();
        String sql = """
            SELECT 
                o.order_id,
                o.customer_id,
                o.shipper_id,
                o.weight_in_pounds,
                o.distance_in_miles,
                c.name AS customer_name,
                s.name AS shipper_name
            FROM ShippingOrder o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Shipper s ON o.shipper_id = s.shipper_id
        """;

        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                System.err.println("Load failed: DB connection is closed or null.");
                return;
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    double weight = rs.getDouble("weight_in_pounds");
                    int distance = rs.getInt("distance_in_miles");
                    double shippingCost = calculateShippingCost(weight, distance);

                    ShippingOrder order = new ShippingOrder(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("shipper_id"),
                            weight,
                            distance,
                            rs.getString("customer_name"),
                            rs.getString("shipper_name"),
                            shippingCost
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }
}
