package dms;

import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * Package Shipping System Main Class.
 * Handles user interface, manual input and file loading.
 * Preserves full validation, emojis, and original design.
 * Fully controlled numeric input parsing for professional validation.
 *
 * @author Julio Lopez
 * @version 1.0
 */
public class Main {

    private final ShippingOrderManager manager;
    private final Scanner scanner;

    /**
     * Constructor initializes manager and scanner.
     */
    public Main() {
        manager = new ShippingOrderManager();
        scanner = new Scanner(System.in);
    }

    /**
     * Main program loop displaying the menu and handling user choices.
     */
    public void run() {
        int choice;
        do {
            choice = readMenuChoice();
            switch (choice) {
                case 1 -> addOrderMenu();
                case 2 -> viewOrders();
                case 3 -> updateOrder();
                case 4 -> deleteOrder();
                case 5 -> System.out.println("👋 Exiting... All data cleared from memory.");
                default -> System.out.println("❌ Invalid option. Please choose between 1-5.");
            }
        } while (choice != 5);
    }

    private int readMenuChoice() {
        System.out.println("\n=============================");
        System.out.println("Package Shipping System");
        System.out.println("=============================");
        System.out.println("1. Add Order");
        System.out.println("2. View Orders");
        System.out.println("3. Update Order");
        System.out.println("4. Delete Order");
        System.out.println("5. Exit");
        System.out.print("Choose an option (1-5): ");
        return readValidatedInt(1, 5);
    }

    private void addOrderMenu() {
        System.out.println("\n1. Upload Orders from File");
        System.out.println("2. Enter Order Manually");
        System.out.print("Choose an option (1-2): ");
        int subChoice = readValidatedInt(1, 2);
        if (subChoice == 1) {
            loadOrdersFromFile();
        } else {
            addOrderManually();
        }
    }

    private void loadOrdersFromFile() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine().trim();
        File file = new File(filename);

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 5) {
                    System.out.println("❌ Skipping invalid line: " + line);
                    continue;
                }

                String customer = parts[1].trim();
                String shipper = parts[2].trim();

                if (!isValidName(customer)) {
                    System.out.println("❌ Invalid customer name in line: " + line);
                    continue;
                }
                if (!isValidName(shipper)) {
                    System.out.println("❌ Invalid shipper name in line: " + line);
                    continue;
                }

                double weight;
                int distance;

                try {
                    weight = Double.parseDouble(parts[3].trim());
                    distance = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid numeric data in line: " + line);
                    continue;
                }

                if (manager.addOrder(customer, shipper, weight, distance)) {
                    System.out.println("✅ Order loaded: " + customer);
                } else {
                    System.out.println("❌ Invalid weight or distance in line: " + line);
                }
            }
            System.out.println("✅ File loading complete.");
            viewOrders(); // ✅ show orders after file load
        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found: " + filename);
        }
    }

    private void addOrderManually() {
        String customer = readValidatedName("Customer name");
        String shipper = readValidatedName("Shipper name");
        double weight = readValidatedDouble("Weight (lbs)", 0.1, 150.0);
        int distance = readValidatedInt("Distance (miles)", 1, 3000);

        if (manager.addOrder(customer, shipper, weight, distance)) {
            System.out.println("✅ Order added successfully!");
        } else {
            System.out.println("❌ Failed to add order. Check business rules.");
        }
    }

    private void viewOrders() {
        List<ShippingOrder> orders = manager.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("❌ No orders found.");
        } else {
            System.out.println("\n=== Current Orders ===");
            for (ShippingOrder order : orders) {
                System.out.println(order);
            }
        }
    }

    private void updateOrder() {
        System.out.print("Enter Order ID to update: ");
        int id = readValidatedInt(1, Integer.MAX_VALUE);
        if (manager.findOrder(id) == null) {
            System.out.println("❌ Order Not Found");
            return;
        }
        double weight = readValidatedDouble("New Weight (lbs)", 0.1, 150.0);
        int distance = readValidatedInt("New Distance (miles)", 1, 3000);
        if (manager.updateOrder(id, weight, distance)) {
            System.out.println("✅ Order updated successfully!");
        } else {
            System.out.println("❌ Failed to update order.");
        }
    }

    private void deleteOrder() {
        System.out.print("Enter Order ID to delete: ");
        int id = readValidatedInt(1, Integer.MAX_VALUE);
        if (manager.deleteOrder(id)) {
            System.out.println("✅ Order deleted successfully!");
        } else {
            System.out.println("❌ Order Not Found");
        }
    }

    private String readValidatedName(String fieldName) {
        String name;
        do {
            System.out.print(fieldName + ": ");
            name = scanner.nextLine().trim();
            if (!isValidName(name)) {
                System.out.println("❌ " + fieldName + " must only contain letters and spaces.");
            }
        } while (!isValidName(name));
        return name;
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    private int readValidatedInt(int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (!input.matches("-?\\d+")) {
                System.out.println("❌ Invalid input. Enter a valid integer.");
                continue;
            }

            try {
                BigInteger bigValue = new BigInteger(input);
                if (bigValue.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0
                        || bigValue.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                    continue;
                }

                int value = bigValue.intValue();
                if (value < min || value > max) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Enter a valid integer.");
            }
        }
    }

    private int readValidatedInt(String prompt, int min, int max) {
        System.out.print(prompt + ": ");
        return readValidatedInt(min, max);
    }

    private double readValidatedDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                BigInteger bigIntPart = new BigInteger(input.split("\\.")[0]);
                if (bigIntPart.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                    continue;
                }

                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("❌ Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }
}
