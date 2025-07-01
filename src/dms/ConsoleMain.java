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
 * Author: Julio Lopez
 * Version: 1.0
 */
public class ConsoleMain {

    // Manager object that handles CRUD operations on shipping orders
    private final ShippingOrderManager manager;

    // Scanner used for user input throughout the application
    private final Scanner scanner;

    /**
     * Constructor initializes the manager and input scanner.
     */
    public ConsoleMain() {
        manager = new ShippingOrderManager();
        scanner = new Scanner(System.in);
    }

    /**
     * Main program loop displaying the menu and routing user input to appropriate features.
     */
    public void run() {
        int choice;
        do {
            choice = readMenuChoice();
            switch (choice) {
                case 1 -> addOrderMenu();         // Option to add orders (manual or from file)
                case 2 -> viewOrders();           // View all current orders
                case 3 -> updateOrder();          // Update an existing order
                case 4 -> deleteOrder();          // Delete an order
                case 5 -> System.out.println("👋 Exiting... All data cleared from memory."); // Exit
                default -> System.out.println("❌ Invalid option. Please choose between 1-5.");
            }
        } while (choice != 5);
    }

    /**
     * Displays the main menu and prompts the user for a valid selection.
     */
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

    /**
     * Offers user two options: upload from file or enter manually.
     */
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

    /**
     * Loads orders from a structured text file and adds valid entries to the system.
     * Invalid lines or data are skipped with warnings.
     */
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
            viewOrders(); // Display loaded orders immediately
        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found: " + filename);
        }
    }

    /**
     * Prompts user to manually enter order details with full validation.
     */
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

    /**
     * Displays all orders currently stored in the system.
     */
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

    /**
     * Prompts user for an order ID and allows updating weight and distance if the order exists.
     */
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

    /**
     * Prompts user for an order ID and deletes the order if found.
     */
    private void deleteOrder() {
        System.out.print("Enter Order ID to delete: ");
        int id = readValidatedInt(1, Integer.MAX_VALUE);
        if (manager.deleteOrder(id)) {
            System.out.println("✅ Order deleted successfully!");
        } else {
            System.out.println("❌ Order Not Found");
        }
    }

    /**
     * Validates that a user-entered name contains only letters and spaces.
     */
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

    /**
     * Returns true if the name contains only letters and spaces.
     */
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    /**
     * Reads and validates integer input within specified range.
     * Rejects non-integer input and overflows using BigInteger.
     */
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

    /**
     * Prompts with label and delegates to standard integer validation.
     */
    private int readValidatedInt(String prompt, int min, int max) {
        System.out.print(prompt + ": ");
        return readValidatedInt(min, max);
    }

    /**
     * Reads and validates double input with full overflow and format protection.
     */
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

    /**
     * Application entry point that creates the Main instance and starts the program.
     */
    public static void main(String[] args) {
        ConsoleMain app = new ConsoleMain();
        app.run();
    }
}
