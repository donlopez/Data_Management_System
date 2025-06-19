package dms;

/**
 * Represents a shipping order with customer information, shipping details, and cost calculation.
 * This class holds all the data for a single order and includes methods to calculate shipping cost.
 *
 * @author Julio Lopez
 * @version 1.0
 */
public class ShippingOrder {
    private int orderID;
    private String customerName;
    private String shipperName;
    private double weightInPounds;
    private int distanceInMiles;

    /**
     * Constructor for objects of class ShippingOrder.
     *
     * @param orderID unique identifier for the order
     * @param customerName name of the customer
     * @param shipperName name of the shipping company
     * @param weightInPounds package weight in pounds
     * @param distanceInMiles shipping distance in miles
     */
    public ShippingOrder(int orderID, String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
    }

    /**
     * Returns customer name.
     *
     * @return customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns order ID.
     *
     * @return order ID
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Calculates and returns shipping cost based on weight and distance.
     *
     * @return calculated shipping cost
     */
    public double calculateShippingCost() {
        double costPerPound = 0.05;
        double costPerMile = 0.10;
        double baseCost = 5.00;
        return baseCost + (weightInPounds * costPerPound) + (distanceInMiles * costPerMile);
    }

    /**
     * Returns weight in pounds.
     *
     * @return weight in pounds
     */
    public double getWeightInPounds() {
        return weightInPounds;
    }

    /**
     * Returns distance in miles.
     *
     * @return distance in miles
     */
    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    /**
     * Sets weight in pounds.
     *
     * @param weightInPounds new weight value
     */
    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    /**
     * Sets distance in miles.
     *
     * @param distanceInMiles new distance value
     */
    public void setDistanceInMiles(int distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    /**
     * Returns shipper name.
     *
     * @return shipper name
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * Returns a string representation of the shipping order.
     *
     * @return order details as string
     */
    public String toString() {
        return "Order ID: " + orderID + ", Customer: " + customerName + ", Shipper: " + shipperName +
                ", Weight: " + weightInPounds + " lbs, Distance: " + distanceInMiles + " miles, Cost: $" + calculateShippingCost();
    }
}
