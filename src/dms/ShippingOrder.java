package dms;

public class ShippingOrder {
    private String customerName;
    private String shipperName;
    private double weightInPounds;
    private int distanceInMiles;
    private int orderID;

    public ShippingOrder(int orderID, String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.shipperName = shipperName;
        this.weightInPounds = weightInPounds;
        this.distanceInMiles = distanceInMiles;
    }

    public double calculateShippingCost() {
        return weightInPounds * distanceInMiles * 0.01;
    }

    public int getOrderID() { return orderID; }
    public String getCustomerName() { return customerName; }
    public String getShipperName() { return shipperName; }
    public double getWeightInPounds() { return weightInPounds; }
    public int getDistanceInMiles() { return distanceInMiles; }

    public void setOrderID(int orderID) { this.orderID = orderID; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setShipperName(String shipperName) { this.shipperName = shipperName; }
    public void setWeightInPounds(double weightInPounds) { this.weightInPounds = weightInPounds; }
    public void setDistanceInMiles(int distanceInMiles) { this.distanceInMiles = distanceInMiles; }

    @Override
    public String toString() {
        return "Order #" + orderID + ": " + customerName + " via " + shipperName +
                ", " + weightInPounds + " lbs, " + distanceInMiles + " miles, $" + calculateShippingCost();
    }
}
