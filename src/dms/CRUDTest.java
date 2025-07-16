package dms;

import java.sql.*;

public class CRUDTest {
    public static void main(String[] args) {
        String host = "localhost";
        String port = "3306";
        String dbName = "ShippingDMS";
        String user = "root";
        String password = "Beautifulday24";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connected to database.");

            // INSERT test
            String insertSql = "INSERT INTO ShippingOrder (customer_id, shipper_id, weight_in_pounds, distance_in_miles, shipping_cost) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, 1); // Assume customer_id 1 exists
                insertStmt.setInt(2, 1); // Assume shipper_id 1 exists
                insertStmt.setDouble(3, 50.0);
                insertStmt.setInt(4, 200);
                insertStmt.setDouble(5, 100.0);
                int inserted = insertStmt.executeUpdate();
                System.out.println("Inserted rows: " + inserted);
            }

            // SELECT test
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM ShippingOrder")) {
                System.out.println("Shipping Orders:");
                while (rs.next()) {
                    System.out.println("Order ID: " + rs.getInt("order_id") +
                            ", Weight: " + rs.getDouble("weight_in_pounds") +
                            ", Distance: " + rs.getInt("distance_in_miles") +
                            ", Cost: $" + rs.getDouble("shipping_cost"));
                }
            }

            // UPDATE test
            String updateSql = "UPDATE ShippingOrder SET weight_in_pounds = ? WHERE order_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDouble(1, 60.0);
                updateStmt.setInt(2, 1); // Update order with ID 1
                int updated = updateStmt.executeUpdate();
                System.out.println("Updated rows: " + updated);
            }

            // DELETE test
            String deleteSql = "DELETE FROM ShippingOrder WHERE order_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, 1); // Delete order with ID 1
                int deleted = deleteStmt.executeUpdate();
                System.out.println("Deleted rows: " + deleted);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
        }
    }
}
