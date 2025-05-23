package dms;

public class ShippingOrder {
    private int orderID;
    private String customerName;
    private String shipperName;
    private double weightInPounds;
    private int distanceInMiles;
    private double shippingCost;

    // Constructor
    public ShippingOrder(int orderID, String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        if (weightInPounds <= 0 || weightInPounds > 10) {
            throw new IllegalArgumentException("Weight must be between 0 and 10 pounds.");
        }
        if (distanceInMiles <= 0) {
            throw new IllegalArgumentException("Distance must be a positive number.");
        }

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
        if (weightInPounds <= 0 || weightInPounds > 10) {
            throw new IllegalArgumentException("Weight must be between 0 and 10 pounds.");
        }
        this.weightInPounds = weightInPounds;
        this.shippingCost = calculateShippingCost();
    }

    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(int distanceInMiles) {
        if (distanceInMiles <= 0) {
            throw new IllegalArgumentException("Distance must be a positive number.");
        }
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
        } else {
            return 5.25 * segments;
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
                ", shippingCost=$" + String.format("%.2f", shippingCost) +
                '}';
    }
}
