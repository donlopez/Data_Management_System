package dms;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * A simple test class to perform basic CRUD operations on the ShippingOrder table.
 * This is intended for development validation only — not for production use.
 *
 * ⚙️ Assumes:
 *  - A MySQL database named 'ShippingDMS' is running locally
 *  - The 'ShippingOrder' table exists with valid foreign key references
 *  - Customer ID 1 and Shipper ID 1 exist in the database
 */
public class CRUDTest {

    /**
     * Default constructor for CRUDTest.
     * Used when instantiating the class without parameters.
     */
    public CRUDTest() {
        // No initialization required
    }

    /**
     * Entry point of the test class. Performs basic CRUD operations on the ShippingOrder table:
     * insert, select, update, and delete — using JDBC.
     *
     * @param args command-line arguments (not used in this test)
     */
    public static void main(String[] args) {
        // 📦 Load .env file
        Dotenv dotenv = Dotenv.load();

        // 🛠 Database connection parameters from .env
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connected to database.");

            // ➕ INSERT operation
            String insertSql = """
                INSERT INTO ShippingOrder 
                (customer_id, shipper_id, weight_in_pounds, distance_in_miles, shipping_cost) 
                VALUES (?, ?, ?, ?, ?)
                """;

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, 1);  // Existing customer_id
                insertStmt.setInt(2, 1);  // Existing shipper_id
                insertStmt.setDouble(3, 50.0);
                insertStmt.setInt(4, 200);
                insertStmt.setDouble(5, 100.0);
                int inserted = insertStmt.executeUpdate();
                System.out.println("🟢 Inserted rows: " + inserted);
            }

            // 📋 SELECT operation
            System.out.println("\n📦 Current Shipping Orders:");
            String selectSql = "SELECT * FROM ShippingOrder";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSql)) {

                while (rs.next()) {
                    System.out.printf(" - Order ID: %d | Weight: %.2f lbs | Distance: %d mi | Cost: $%.2f%n",
                            rs.getInt("order_id"),
                            rs.getDouble("weight_in_pounds"),
                            rs.getInt("distance_in_miles"),
                            rs.getDouble("shipping_cost"));
                }
            }

            // ✏️ UPDATE operation
            String updateSql = "UPDATE ShippingOrder SET weight_in_pounds = ? WHERE order_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDouble(1, 60.0);
                updateStmt.setInt(2, 1);  // Order ID to update
                int updated = updateStmt.executeUpdate();
                System.out.println("\n🟡 Updated rows: " + updated);
            }

            // 🗑 DELETE operation
            String deleteSql = "DELETE FROM ShippingOrder WHERE order_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, 1);  // Order ID to delete
                int deleted = deleteStmt.executeUpdate();
                System.out.println("🔴 Deleted rows: " + deleted);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
        }
    }
}
