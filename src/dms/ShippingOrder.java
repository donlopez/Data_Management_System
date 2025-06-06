package dms;

public class ShippingOrder {
    private String customerName;
    private String shipperName;
    private int orderID;
    private double weightInPounds;
    private int distanceInMiles;

    public ShippingOrder(int orderID, String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public int getOrderID() {
        return orderID;
    }

    public double getWeightInPounds() {
        return weightInPounds;
    }

    public int getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public void setDistanceInMiles(int distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public double calculateShippingCost() {
        double costPerMile = 0.5;
        double costPerPound = 0.25;
        return (distanceInMiles * costPerMile) + (weightInPounds * costPerPound);
    }

    @Override
    public String toString() {
        return String.format(
                "Order ID: %d | Customer: %s | Shipper: %s | Weight: %.2f lbs | Distance: %d miles | Cost: $%.2f",
                orderID, customerName, shipperName, weightInPounds, distanceInMiles, calculateShippingCost()
        );
    }
}
