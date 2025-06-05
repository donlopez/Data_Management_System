package dms;

import java.util.ArrayList;
import java.util.List;

public class ShippingOrderManager {
    private final List<ShippingOrder> orders = new ArrayList<>();
    private int nextId = 1;

    public boolean addOrder(String customer, String shipper, double weight, int distance) {
        ShippingOrder order = new ShippingOrder(nextId++, customer, shipper, weight, distance);
        orders.add(order);
        return true;
    }

    public List<ShippingOrder> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public boolean updateOrder(int orderId, double newWeight, int newDistance) {
        for (ShippingOrder order : orders) {
            if (order.getOrderID() == orderId) {
                order.setWeightInPounds(newWeight);
                order.setDistanceInMiles(newDistance);
                return true;
            }
        }
        return false;
    }

    public boolean deleteOrder(int orderId) {
        return orders.removeIf(order -> order.getOrderID() == orderId);
    }

    public ShippingOrder findOrder(int orderId) {
        for (ShippingOrder order : orders) {
            if (order.getOrderID() == orderId) {
                return order;
            }
        }
        return null;
    }
}
