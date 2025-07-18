package dms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShippingOrder} class.
 * <p>
 * These tests verify:
 * <ul>
 *     <li>Correct retrieval of properties through getters</li>
 *     <li>Accurate calculation of shipping costs</li>
 *     <li>Recalculation logic when weight or distance is modified</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class ShippingOrderTest {

    private ShippingOrder order;

    /**
     * Default constructor for ShippingOrderTest.
     * Prepares the test class for execution.
     */
    public ShippingOrderTest() {
        // No custom setup needed here; JUnit handles test lifecycle
    }

    /**
     * Creates a default ShippingOrder instance before each test using base constructor.
     */
    @BeforeEach
    public void setUp() {
        order = new ShippingOrder(1, 101, 201, 20.0, 150);
    }

    /**
     * Verifies that getter methods return the correct values for all properties.
     */
    @Test
    @DisplayName("Getters return correct values")
    public void testGetters() {
        assertEquals(1, order.getOrderId(), "Order ID should be 1");
        assertEquals(101, order.getCustomerId(), "Customer ID should be 101");
        assertEquals(201, order.getShipperId(), "Shipper ID should be 201");
        assertEquals(20.0, order.getWeightInPounds(), 0.0001, "Weight should be 20.0 pounds");
        assertEquals(150, order.getDistanceInMiles(), "Distance should be 150 miles");
    }

    /**
     * Validates the initial cost calculation based on weight and distance.
     */
    @Test
    @DisplayName("Initial shipping cost is calculated correctly")
    public void testCalculateShippingCost() {
        double expectedCost = 5.00 + (20.0 * 0.05) + (150 * 0.10);
        assertEquals(expectedCost, order.calculateShippingCost(), 0.0001, "Shipping cost formula should match expected result");
    }

    /**
     * Tests that updating weight and distance results in recalculated shipping cost.
     */
    @Test
    @DisplayName("Shipping cost updates after modifying weight and distance")
    public void testSettersAndRecalculation() {
        order.setWeightInPounds(50.0);
        order.setDistanceInMiles(300);

        double expectedCost = 5.00 + (50.0 * 0.05) + (300 * 0.10);
        assertEquals(expectedCost, order.calculateShippingCost(), 0.0001, "Updated cost should reflect new weight and distance");
    }
}
