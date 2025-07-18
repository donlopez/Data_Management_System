package dms;

/**
 * Represents a shipping order with references to customer and shipper IDs,
 * shipment details (weight, distance), and calculated shipping cost.
 *
 * <p>This class supports integration with a normalized MySQL database,
 * allowing orders to be displayed using either foreign key IDs or
 * joined customer/shipper names, depending on the use case.</p>
 *
 * Author: Julio Lopez
 * Version: 2.1
 */
public class ShippingOrder {

    // --- Fields ---

    /** Unique identifier for the order (primary key in the database) */
    private int orderId;

    /** Foreign key to the Customer table */
    private int customerId;

    /** Foreign key to the Shipper table */
    private int shipperId;

    /** Weight of the shipment in pounds */
    private double weightInPounds;

    /** Distance to be shipped in miles */
    private int distanceInMiles;

    /** Cached value of the shipping cost based on weight and distance */
    private double shippingCost;

    /** Customer name (used for displaying JOINed results) */
    private String customerName;

    /** Shipper name (used for displaying JOINed results) */
    private String shipperName;

    // --- Constructors ---

    /**
     * Constructor for raw database values without names.
     *
     * @param orderId         the order ID
     * @param customerId      the customer ID (foreign key)
     * @param shipperId       the shipper ID (foreign key)
     * @param weightInPounds  the shipment weight in pounds
     * @param distanceInMiles the distance to be shipped in miles
     */
    public ShippingOrder(int orderId, int customerId, int shipperId,
                         double weightInPounds, int distanceInMiles) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost();
    }

    /**
     * Constructor used for displaying data with pre-computed cost and names.
     *
     * @param orderId         the order ID
     * @param customerName    the customer name
     * @param shipperName     the shipper name
     * @param weightInPounds  the shipment weight in pounds
     * @param distanceInMiles the distance to be shipped in miles
     * @param shippingCost    the pre-computed shipping cost
     */
    public ShippingOrder(int orderId, String customerName, String shipperName,
                         double weightInPounds, int distanceInMiles, double shippingCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = shippingCost;
    }

    /**
     * Constructor for JOINed results including IDs and names.
     *
     * @param orderId         the order ID
     * @param customerId      the customer ID (foreign key)
     * @param shipperId       the shipper ID (foreign key)
     * @param weightInPounds  the shipment weight in pounds
     * @param distanceInMiles the distance to be shipped in miles
     * @param customerName    the customer name
     * @param shipperName     the shipper name
     */
    public ShippingOrder(int orderId, int customerId, int shipperId,
                         double weightInPounds, int distanceInMiles,
                         String customerName, String shipperName) {
        this(orderId, customerId, shipperId, weightInPounds, distanceInMiles);
        this.customerName = customerName;
        this.shipperName = shipperName;
    }

    /**
     * Constructor for JOINed results including IDs, names, and cost.
     *
     * @param orderId         the order ID
     * @param customerId      the customer ID (foreign key)
     * @param shipperId       the shipper ID (foreign key)
     * @param weightInPounds  the shipment weight in pounds
     * @param distanceInMiles the distance to be shipped in miles
     * @param customerName    the customer name
     * @param shipperName     the shipper name
     * @param shippingCost    the pre-computed shipping cost
     */
    public ShippingOrder(int orderId, int customerId, int shipperId,
                         double weightInPounds, int distanceInMiles,
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

    /**
     * Gets the order ID.
     *
     * @return the order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Gets the customer ID.
     *
     * @return the customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId the new customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the shipper ID.
     *
     * @return the shipper ID
     */
    public int getShipperId() {
        return shipperId;
    }

    /**
     * Sets the shipper ID.
     *
     * @param shipperId the new shipper ID
     */
    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    /**
     * Gets the shipment weight in pounds.
     *
     * @return the shipment weight
     */
    public double getWeightInPounds() {
        return weightInPounds;
    }

    /**
     * Sets the shipment weight in pounds and updates cost.
     *
     * @param weightInPounds the new weight in pounds
     */
    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
        this.shippingCost = calculateShippingCost();
    }

    /**
     * Gets the shipping distance in miles.
     *
     * @return the distance in miles
     */
    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    /**
     * Sets the shipping distance in miles and updates cost.
     *
     * @param distanceInMiles the new distance in miles
     */
    public void setDistanceInMiles(int distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost();
    }

    /**
     * Gets the calculated or cached shipping cost.
     *
     * @return the shipping cost
     */
    public double getShippingCost() {
        return shippingCost;
    }

    /**
     * Gets the customer name (for display).
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     *
     * @param customerName the new customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the shipper name (for display).
     *
     * @return the shipper name
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * Sets the shipper name.
     *
     * @param shipperName the new shipper name
     */
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    // --- Business Logic ---

    /**
     * Calculates the shipping cost using basic pricing rules:
     * Base fee: $5.00 + $0.05/lb + $0.10/mile
     *
     * @return the calculated shipping cost
     */
    public double calculateShippingCost() {
        double base = 5.00;
        double perPound = 0.05;
        double perMile = 0.10;
        return base + (weightInPounds * perPound) + (distanceInMiles * perMile);
    }

    // --- Utility ---

    /**
     * Returns a formatted string of order data for logging or debugging.
     *
     * @return the string representation of the order
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
