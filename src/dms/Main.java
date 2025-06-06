package dms;

import java.util.Scanner;

public class Main {
    private final ShippingOrderManager manager = new ShippingOrderManager();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=============================");
            System.out.println("Package Shipping System");
            System.out.println("=============================");
            System.out.println("1. Add Order");
            System.out.println("2. View Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Delete Order");
            System.out.println("5. Exit");
            choice = readMenuChoice();

            switch (choice) {
                case 1 -> addOrderUI();
                case 2 -> viewOrders();
                case 3 -> updateOrderUI();
                case 4 -> deleteOrderUI();
                case 5 -> System.out.println("👋 Exiting... All data cleared from memory.");
            }
        } while (choice != 5);
    }

    private void addOrderUI() {
        String customer;
        do {
            System.out.print("Customer name: ");
            customer = scanner.nextLine().trim();
            if (customer.isEmpty()) {
                System.out.println("❌ Customer name is required.");
            } else if (!customer.matches("[a-zA-Z ]+")) {
                System.out.println("❌ Customer name must only contain letters and spaces.");
                customer = "";
            }
        } while (customer.isEmpty());

        String shipper;
        do {
            System.out.print("Shipper name: ");
            shipper = scanner.nextLine().trim();
            if (shipper.isEmpty()) {
                System.out.println("❌ Shipper name is required.");
            } else if (!shipper.matches("[a-zA-Z ]+")) {
                System.out.println("❌ Shipper name must only contain letters and spaces.");
                shipper = "";
            }
        } while (shipper.isEmpty());

        double weight = readDouble("Weight (lbs)", 0.1, 150);
        int distance = readInt("Distance (miles)", 1, 3000);

        manager.addOrder(customer, shipper, weight, distance);
        System.out.println("✅ Order added successfully!");
    }

    private void viewOrders() {
        if (manager.getAllOrders().isEmpty()) {
            System.out.println("❌ No Orders Found");
        } else {
            System.out.println("\n=== Current Orders ===");
            for (ShippingOrder order : manager.getAllOrders()) {
                System.out.println(order);
            }
        }
    }

    private void updateOrderUI() {
        int orderId = readInt("Enter Order ID to update", 1, Integer.MAX_VALUE);
        ShippingOrder order = manager.findOrder(orderId);
        if (order != null) {
            double newWeight = readDouble("New Weight (lbs)", 0.1, 150);
            int newDistance = readInt("New Distance (miles)", 1, 3000);
            manager.updateOrder(orderId, newWeight, newDistance);
            System.out.println("✅ Order updated successfully!");
        } else {
            System.out.println("❌ Order Not Found");
        }
    }

    private void deleteOrderUI() {
        int orderId = readInt("Enter Order ID to delete", 1, Integer.MAX_VALUE);
        ShippingOrder order = manager.findOrder(orderId);
        if (order != null) {
            System.out.print("Are you sure you want to delete this order? (y/n): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                manager.deleteOrder(orderId);
                System.out.println("✅ Order deleted successfully!");
            } else {
                System.out.println("❌ Deletion canceled.");
            }
        } else {
            System.out.println("❌ Order Not Found");
        }
    }

    private int readMenuChoice() {
        return readInt("Choose an option (1-5)", 1, 5);
    }

    private int readInt(String fieldName, int min, int max) {
        int value;
        do {
            System.out.print(fieldName + ": ");
            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                    value = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter an integer.");
                value = -1;
            }
        } while (value == -1);
        return value;
    }

    private double readDouble(String fieldName, double min, double max) {
        double value;
        do {
            System.out.print(fieldName + ": ");
            String input = scanner.nextLine().trim();
            try {
                value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                    value = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter a number.");
                value = -1;
            }
        } while (value == -1);
        return value;
    }
}
