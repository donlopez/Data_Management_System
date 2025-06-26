package dms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShippingOrderManager class.
 */
public class ShippingOrderManagerTest {

    private ShippingOrderManager manager;

    @BeforeEach
    public void setUp() {
        manager = new ShippingOrderManager();
    }

    @Test
    @DisplayName("Test adding a new order")
    public void testAddOrder() {
        boolean added = manager.addOrder("Alice", "Bob", 10.5, 500);
        assertTrue(added, "Order should be added successfully");
        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(1, orders.size(), "There should be 1 order");
    }

    @Test
    @DisplayName("Test deleting an order")
    public void testDeleteOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);
        manager.addOrder("Charlie", "Dana", 7.0, 700);

        boolean deleted = manager.deleteOrder(1);
        assertTrue(deleted, "Order should be deleted successfully");
        assertEquals(1, manager.getAllOrders().size(), "Only one order should remain");

        boolean deleteNonExistent = manager.deleteOrder(999);
        assertFalse(deleteNonExistent, "Deleting non-existent ID should fail");
    }

    @Test
    @DisplayName("Test updating an existing order")
    public void testUpdateOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);
        boolean updated = manager.updateOrder(1, 20.0, 800);
        assertTrue(updated, "Order should be updated successfully");

        ShippingOrder order = manager.findOrder(1);
        assertNotNull(order, "Order should exist after update");
        assertTrue(order.toString().contains("20.0"), "Updated weight should be reflected");
        assertTrue(order.toString().contains("800"), "Updated distance should be reflected");

        boolean updateInvalid = manager.updateOrder(999, 15.0, 400);
        assertFalse(updateInvalid, "Updating non-existent ID should fail");
    }

    @Test
    @DisplayName("Test finding an order by ID")
    public void testFindOrder() {
        manager.addOrder("Alice", "Bob", 10.5, 500);
        ShippingOrder foundOrder = manager.findOrder(1);
        assertNotNull(foundOrder, "Order with ID 1 should be found");

        ShippingOrder nonExistentOrder = manager.findOrder(999);
        assertNull(nonExistentOrder, "Non-existent ID should return null");
    }

    @Test
    @DisplayName("Test retrieving all orders")
    public void testGetAllOrders() {
        assertTrue(manager.getAllOrders().isEmpty(), "Initially order list should be empty");
        manager.addOrder("Alice", "Bob", 10.5, 500);
        manager.addOrder("Charlie", "Dana", 7.0, 700);
        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(2, orders.size(), "There should be 2 orders after adding");
    }

    @Test
    @DisplayName("Test loading orders from file")
    public void testLoadOrdersFromFile() {
        File file = new File("test/resources/shipping_orders_sample.txt");
        assertTrue(file.exists(), "Test resource file should exist: " + file.getAbsolutePath());

        manager.loadOrdersFromFile(file.getPath());

        List<ShippingOrder> orders = manager.getAllOrders();
        assertEquals(20, orders.size(), "Should load 20 orders from file");

        ShippingOrder first = manager.findOrder(1);
        assertNotNull(first, "First order should be found");
        assertEquals("John Smith", first.getCustomerName());
    }
}
