package dms;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Manages shipping orders by handling all CRUD operations.
 * Stores orders in an in-memory list and applies business validation.
 *
 * @author Julio Lopez
 * @version 1.0
 */
public class ShippingOrderManager {
    private List<ShippingOrder> orders;
    private int nextId;

    /**
     * Constructor initializes the list of orders and ID counter.
     */
    public ShippingOrderManager() {
        orders = new ArrayList<>();
        nextId = 1;
    }

    /**
     * Adds a new shipping order.
     *
     * @param customerName customer name
     * @param shipperName shipping company name
     * @param weight weight in pounds
     * @param distance distance in miles
     * @return true if order added successfully, false if validation fails
     */
    public boolean addOrder(String customerName, String shipperName, double weight, int distance) {
        if (weight <= 0 || weight > 150) {
            return false;
        }
        if (distance <= 0 || distance > 3000) {
            return false;
        }
        ShippingOrder newOrder = new ShippingOrder(nextId++, customerName, shipperName, weight, distance);
        orders.add(newOrder);
        return true;
    }

    /**
     * Finds and returns an order by ID.
     *
     * @param id order ID
     * @return ShippingOrder if found, otherwise null
     */
    public ShippingOrder findOrder(int id) {
        for (ShippingOrder order : orders) {
            if (order.getOrderID() == id) {
                return order;
            }
        }
        return null;
    }

    /**
     * Returns list of all orders.
     *
     * @return list of ShippingOrder objects
     */
    public List<ShippingOrder> getAllOrders() {
        return orders;
    }

    /**
     * Alternate getter for orders for JavaFX compatibility.
     *
     * @return list of ShippingOrder objects
     */
    public List<ShippingOrder> getOrders() {
        return orders;
    }

    /**
     * Deletes an order by ID.
     *
     * @param id order ID to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteOrder(int id) {
        ShippingOrder order = findOrder(id);
        if (order != null) {
            orders.remove(order);
            return true;
        }
        return false;
    }

    /**
     * Updates weight and distance for an existing order.
     *
     * @param id order ID
     * @param weight new weight
     * @param distance new distance
     * @return true if updated successfully, false if validation fails or order not found
     */
    public boolean updateOrder(int id, double weight, int distance) {
        ShippingOrder order = findOrder(id);
        if (order != null) {
            if (weight <= 0 || weight > 150 || distance <= 0 || distance > 3000) {
                return false;
            }
            order.setWeightInPounds(weight);
            order.setDistanceInMiles(distance);
            return true;
        }
        return false;
    }

    /**
     * Loads shipping orders from a file. Each row should be:
     * customerName,shipperName,weight,distance
     *
     * @param filename path to the file
     */
    public void loadOrdersFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String customerName = parts[1].trim();
                    String shipperName = parts[2].trim();
                    double weight = Double.parseDouble(parts[3].trim());
                    int distance = Integer.parseInt(parts[4].trim());
                    addOrder(customerName, shipperName, weight, distance);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading orders from file: " + e.getMessage());
        }
    }
}
