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
        order = new ShippingOrder(1, "Julio Lopez", "UPS", 20.0, 150);
    }

    /**
     * Test getter methods to ensure correct data retrieval.
     */
    @Test
    @DisplayName("Test getter methods")
    public void testGetters() {
        assertEquals(1, order.getOrderID(), "Order ID should be 1");
        assertEquals("Julio Lopez", order.getCustomerName(), "Customer name should match");
        assertEquals("UPS", order.getShipperName(), "Shipper name should match");
        assertEquals(20.0, order.getWeightInPounds(), 0.0001, "Weight should be 20.0");
        assertEquals(150, order.getDistanceInMiles(), "Distance should be 150");
    }

    /**
     * Test calculation of shipping cost with the initial values.
     */
    @Test
    @DisplayName("Test shipping cost calculation")
    public void testCalculateShippingCost() {
        // Base cost: 5.00 + (weight * 0.05) + (distance * 0.10)
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

        // New cost based on updated values
        double expectedCost = 5.00 + (50.0 * 0.05) + (300 * 0.10);
        assertEquals(expectedCost, order.calculateShippingCost(), 0.0001, "Shipping cost should be recalculated correctly after update");
    }
}
