package dms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShippingOrderManager} class.
 * <p>
 * This test suite validates the following features:
 * <ul>
 *     <li>Adding new orders</li>
 *     <li>Deleting existing and non-existent orders</li>
 *     <li>Updating orders with valid and invalid data</li>
 *     <li>Retrieving orders by ID</li>
 *     <li>Retrieving all orders</li>
 *     <li>Loading orders from a structured text file</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class ShippingOrderManagerTest {

    private ShippingOrderManager manager;

    /**
     * Default constructor.
     * Initializes the test class.
     */
    public ShippingOrderManagerTest() {
        // Constructor left empty intentionally; initialization is handled by setUp()
    }

    /**
     * Initializes a new instance of the shipping order manager before each test.
     */
    @BeforeEach
    public void setUp() {
        manager = new ShippingOrderManager();
    }

    /**
     * Tests that a new valid order is successfully added.
     */
    @Test
    @DisplayName("Add a new order successfully")
    public void testAddOrder() {
        boolean added = manager.addOrder("Alice", "Bob", 10.5, 500);
        assertTrue(added, "Order should be added successfully");

        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(1, orders.size(), "There should be 1 order in the list");
    }

    /**
     * Tests deleting an existing order and verifies deletion by ID.
     * Also checks that deletion fails for a non-existent order.
     */
    @Test
    @DisplayName("Delete an order by ID")
    public void testDeleteOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);
        manager.addOrder("Charlie", "Dana", 7.0, 700);

        boolean deleted = manager.deleteOrder(1);
        assertTrue(deleted, "Order ID 1 should be deleted");

        assertEquals(1, manager.getAllOrders().size(), "Only one order should remain");

        boolean deleteNonExistent = manager.deleteOrder(999);
        assertFalse(deleteNonExistent, "Deleting a non-existent order should return false");
    }

    /**
     * Tests updating the weight and distance of an existing order.
     * Validates changes are reflected. Also tests updating a non-existent order.
     */
    @Test
    @DisplayName("Update an existing order")
    public void testUpdateOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);

        boolean updated = manager.updateOrder(1, 20.0, 800);
        assertTrue(updated, "Order should be updated successfully");

        ShippingOrder order = manager.findOrder(1);
        assertNotNull(order, "Updated order should still exist");
        assertTrue(order.toString().contains("20.0"), "Weight should reflect updated value");
        assertTrue(order.toString().contains("800"), "Distance should reflect updated value");

        boolean updateInvalid = manager.updateOrder(999, 15.0, 400);
        assertFalse(updateInvalid, "Updating non-existent order should return false");
    }

    /**
     * Tests locating an order by ID using {@code findOrder()}.
     */
    @Test
    @DisplayName("Find order by ID")
    public void testFindOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);

        ShippingOrder found = manager.findOrder(1);
        assertNotNull(found, "Order with ID 1 should be found");

        ShippingOrder notFound = manager.findOrder(999);
        assertNull(notFound, "Non-existent ID should return null");
    }

    /**
     * Tests retrieval of all orders and validates the total count.
     */
    @Test
    @DisplayName("Get all orders")
    public void testGetAllOrders() {
        assertTrue(manager.getAllOrders().isEmpty(), "Order list should be empty at start");

        manager.addOrder("Alice", "Bob", 10.5, 500);
        manager.addOrder("Charlie", "Dana", 7.0, 700);

        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(2, orders.size(), "There should be 2 orders in the list");
    }

    /**
     * Tests the file-based loading of orders and validates that data is correctly loaded.
     */
    @Test
    @DisplayName("Load orders from sample file")
    public void testLoadOrdersFromFile() {
        File file = new File("test/resources/shipping_orders_sample.txt");
        assertTrue(file.exists(), "Sample file should exist: " + file.getAbsolutePath());

        manager.loadOrdersFromFile(file.getPath());

        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(20, orders.size(), "Should load 20 orders from file");

        ShippingOrder first = manager.findOrder(1);
        assertNotNull(first, "First order should be found after file load");
        assertEquals("John Smith", first.getCustomerName(), "Customer name should match expected");
    }
}
