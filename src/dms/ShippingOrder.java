package dms;

/**
 * Represents a shipping order with customer and shipper IDs, shipping details, and cost calculation.
 * This class holds all the data for a single order and includes methods to calculate shipping cost.
 *
 * Updated to support normalized database design with JOINed display (customer/shipper names).
 *
 * Author: Julio Lopez
 * Version: 2.1
 */
public class ShippingOrder {
    private int orderId;             // Unique identifier for the order (order_id in DB)
    private int customerId;          // Foreign key to Customer table
    private int shipperId;           // Foreign key to Shipper table
    private double weightInPounds;   // Weight of the shipment
    private int distanceInMiles;     // Shipping distance
    private double shippingCost;     // Calculated and cached cost

    private String customerName;     // Display name of customer (optional)
    private String shipperName;      // Display name of shipper (optional)

    /**
     * Constructor for core database values (without names).
     */
    public ShippingOrder(int orderId, int customerId, int shipperId, double weightInPounds, int distanceInMiles) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost();
    }

    /**
     * Extended constructor for display data with names and cost (used in display tables).
     */
    public ShippingOrder(int orderId, String customerName, String shipperName, double weightInPounds, int distanceInMiles, double shippingCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = shippingCost;
    }

    /**
     * Extended constructor for JOINed queries (includes IDs + names).
     */
    public ShippingOrder(int orderId, int customerId, int shipperId, double weightInPounds, int distanceInMiles,
                         String customerName, String shipperName) {
        this(orderId, customerId, shipperId, weightInPounds, distanceInMiles);
        this.customerName = customerName;
        this.shipperName = shipperName;
    }

    /**
     * Fully extended constructor for JOINed queries (includes IDs + names + cost).
     */
    public ShippingOrder(int orderId, int customerId, int shipperId, double weightInPounds, int distanceInMiles,
                         String customerName, String shipperName, double shippingCost) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.shippingCost = shippingCost;
    }

    // --- Getters and Setters ---

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public double getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
        this.shippingCost = calculateShippingCost(); // Recalculate cost
    }

    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(int distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost(); // Recalculate cost
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     * Calculates shipping cost using base + weight + distance multipliers.
     */
    public double calculateShippingCost() {
        double base = 5.00;
        double perPound = 0.05;
        double perMile = 0.10;
        return base + (weightInPounds * perPound) + (distanceInMiles * perMile);
    }

    /**
     * For debug/logging purposes.
     */
    @Override
    public String toString() {
        return "Order ID: " + orderId +
                ", Customer: " + (customerName != null ? customerName : customerId) +
                ", Shipper: " + (shipperName != null ? shipperName : shipperId) +
                ", Weight: " + weightInPounds + " lb" +
                ", Distance: " + distanceInMiles + " mi" +
                ", Cost: $" + String.format("%.2f", shippingCost);
    }
}
