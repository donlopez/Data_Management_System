public class ShippingOrder {
    private int orderID;
    private String customerName;
    private String shipperName;
    private double weightInPounds;
    private int distanceInMiles;
    private double shippingCost;

    // Constructor
    public ShippingOrder(int orderID, String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost();
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
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

    public double getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
        this.shippingCost = calculateShippingCost();
    }

    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(int distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
        this.shippingCost = calculateShippingCost();
    }

    public double getShippingCost() {
        return shippingCost;
    }

    // Method to calculate shipping cost
    private double calculateShippingCost() {
        int segments = (distanceInMiles / 500) + (distanceInMiles % 500 > 0 ? 1 : 0);

        if (weightInPounds <= 2) {
            return 1.50 * segments;
        } else if (weightInPounds <= 6) {
            return 3.70 * segments;
        } else if (weightInPounds <= 10) {
            return 5.25 * segments;
        } else {
            throw new IllegalArgumentException("Weight exceeds 10 pounds limit.");
        }
    }

    @Override
    public String toString() {
        return "ShippingOrder{" +
                "orderID=" + orderID +
                ", customerName='" + customerName + '\'' +
                ", shipperName='" + shipperName + '\'' +
                ", weightInPounds=" + weightInPounds +
                ", distanceInMiles=" + distanceInMiles +
                ", shippingCost=" + shippingCost +
                '}';
    }
}
