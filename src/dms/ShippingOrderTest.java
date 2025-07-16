package dms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ShippingOrder class.
 */
public class ShippingOrderTest {

    private ShippingOrder order;

    /**
     * Initialize a ShippingOrder instance before each test.
     */
    @BeforeEach
    public void setUp() {
        // Match the constructor from your updated class
        order = new ShippingOrder(1, 101, 201, 20.0, 150);
    }

    /**
     * Test getter methods to ensure correct data retrieval.
     */
    @Test
    @DisplayName("Test getter methods")
    public void testGetters() {
        assertEquals(1, order.getOrderId(), "Order ID should be 1");
        assertEquals(101, order.getCustomerId(), "Customer ID should be 101");
        assertEquals(201, order.getShipperId(), "Shipper ID should be 201");
        assertEquals(20.0, order.getWeightInPounds(), 0.0001, "Weight should be 20.0");
        assertEquals(150, order.getDistanceInMiles(), "Distance should be 150");
    }

    /**
     * Test calculation of shipping cost with the initial values.
     */
    @Test
    @DisplayName("Test shipping cost calculation")
    public void testCalculateShippingCost() {
        double expectedCost = 5.00 + (20.0 * 0.05) + (150 * 0.10);
        assertEquals(expectedCost, order.calculateShippingCost(), 0.0001, "Shipping cost should be calculated correctly");
    }

    /**
     * Test updating weight and distance and verify new shipping cost.
     */
    @Test
    @DisplayName("Test modifying weight and distance")
    public void testSettersAndRecalculation() {
        order.setWeightInPounds(50.0);
        order.setDistanceInMiles(300);

        double expectedCost = 5.00 + (50.0 * 0.05) + (300 * 0.10);
        assertEquals(expectedCost, order.calculateShippingCost(), 0.0001, "Shipping cost should be recalculated correctly after update");
    }
}
