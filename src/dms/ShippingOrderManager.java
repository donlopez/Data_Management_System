package dms;

import java.util.ArrayList;
import java.util.List;

public class ShippingOrderManager {
    private final List<ShippingOrder> orders = new ArrayList<>();
    private int nextId = 1;

    public List<ShippingOrder> getAllOrders() {
        return orders;
    }

    public ShippingOrder findOrder(int orderId) {
        for (ShippingOrder order : orders) {
            if (order.getOrderID() == orderId) {
                return order;
            }
        }
        return null;
    }

    public boolean updateOrder(int orderId, double newWeight, int newDistance) {
        ShippingOrder order = findOrder(orderId);
        if (order != null) {
            order.setWeightInPounds(newWeight);
            order.setDistanceInMiles(newDistance);
            order.calculateShippingCost(); // Explicit recalculate per flowchart
            return true;
        }
        return false;
    }

    public boolean deleteOrder(int orderId) {
        ShippingOrder order = findOrder(orderId);
        if (order != null) {
            orders.remove(order);
            return true;
        }
        return false;
    }

    public boolean addOrder(String customerName, String shipperName, double weightInPounds, int distanceInMiles) {
        ShippingOrder newOrder = new ShippingOrder(nextId++, customerName, shipperName, weightInPounds, distanceInMiles);
        orders.add(newOrder);
        return true;
    }
}
