package dms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Manager class for all ShippingOrder operations.
 * Calculates shipping cost in Java (not in the database).
 * Fully supports CRUD operations and loading data with JOINs.
 * Removes any existing cost before recalculating it during insert or update.
 * Validates shipper and customer names to reject numeric values or names longer than 30 characters.
 */
public class ShippingOrderManager {
    private final List<ShippingOrder> orders;

    public ShippingOrderManager() {
        orders = new ArrayList<>();
        loadOrdersFromDatabase();
    }

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
            double cost = calculateShippingCost(weight, distance); // Java-side calculation

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

            double cost = calculateShippingCost(weight, distance); // Recalculate cost

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

    public ShippingOrder findOrder(int id) {
        for (ShippingOrder order : orders) {
            if (order.getOrderId() == id) return order;
        }
        return null;
    }

    public List<ShippingOrder> getAllOrders() {
        return orders;
    }

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
        if (!isValidName(name)) throw new SQLException("Invalid customer name: must not contain numbers and max 30 characters.");

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
        if (!isValidName(name)) throw new SQLException("Invalid shipper name: must not contain numbers and max 30 characters.");

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

    /**
     * Shipping cost formula: weight * distance * 0.0015, rounded to 2 decimal places.
     */
    private double calculateShippingCost(double weight, int distance) {
        return Math.round(weight * distance * 0.0015 * 100.0) / 100.0;
    }

    /**
     * Validates name: not null, <= 30 characters, no digits allowed.
     */
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
