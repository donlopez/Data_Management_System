package dms;

import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final ShippingOrderManager manager = new ShippingOrderManager();

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("Package Shipping System");
            System.out.println("==============================");
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

        double weight;
        do {
            weight = readDouble("Weight (lbs): ");
            if (weight < 0.1 || weight > 150) {
                System.out.println("❌ Weight must be between 0.1 and 150 lbs.");
                weight = -1;
            }
        } while (weight < 0.1);

        int distance;
        do {
            distance = readInt("Distance (whole miles): ");
            if (distance < 1 || distance > 3000) {
                System.out.println("❌ Distance must be between 1 and 3000 miles.");
                distance = -1;
            }
        } while (distance < 1);

        boolean success = manager.addOrder(customer, shipper, weight, distance);
        System.out.println(success ? "✅ Order added successfully." : "❌ Failed to add order.");
    }

    private void viewOrders() {
        var orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("⚠️ No orders found.");
            return;
        }

        for (ShippingOrder order : orders) {
            System.out.println(order);
        }
    }

    private void updateOrderUI() {
        int id = readInt("Enter order ID to update: ");
        ShippingOrder order = manager.findOrder(id);
        if (order == null) {
            System.out.println("❌ No order with ID " + id + " exists.");
            return;
        }

        System.out.println("➡️ Current Order: " + order);

        double newWeight;
        do {
            newWeight = readDouble("New weight (lbs): ");
            if (newWeight < 0.1 || newWeight > 150) {
                System.out.println("❌ Weight must be between 0.1 and 150 lbs.");
                newWeight = -1;
            }
        } while (newWeight < 0.1);

        int newDistance;
        do {
            newDistance = readInt("New distance (whole miles): ");
            if (newDistance < 1 || newDistance > 500) {
                System.out.println("❌ Distance must be between 1 and 500 miles.");
                newDistance = -1;
            }
        } while (newDistance < 1);

        boolean success = manager.updateOrder(id, newWeight, newDistance);
        System.out.println(success ? "✅ Order #" + id + " updated successfully." : "❌ Update failed.");
    }

    private void deleteOrderUI() {
        int id = readInt("Enter order ID to delete: ");
        ShippingOrder order = manager.findOrder(id);
        if (order == null) {
            System.out.println("❌ No order with ID " + id + " exists.");
            return;
        }

        System.out.println("🗑️ Deleting: " + order);
        boolean success = manager.deleteOrder(id);
        System.out.println(success ? "✅ Order #" + id + " deleted." : "❌ Deletion failed.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                scanner.nextLine(); // flush newline
                return val;
            } else {
                System.out.println("❌ Invalid input. Please enter a whole number.");
                scanner.next(); // discard bad input
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double val = scanner.nextDouble();
                scanner.nextLine(); // flush newline
                return val;
            } else {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                scanner.next(); // discard bad input
            }
        }
    }

    private int readMenuChoice() {
        int val;
        do {
            val = readInt("Enter choice (1-5): ");
            if (val < 1 || val > 5) {
                System.out.println("❌ Please choose a valid option between 1 and 5.");
            }
        } while (val < 1 || val > 5);
        return val;
    }
}
