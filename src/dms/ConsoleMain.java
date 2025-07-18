package dms;

import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * Main class for the Package Shipping System console interface.
 * Provides a text-based menu for managing shipping orders, including options
 * to add, view, update, and delete orders manually or from a file.
 *
 * <p>This class is responsible for:
 * <ul>
 *     <li>Displaying the interactive menu</li>
 *     <li>Routing user input to appropriate functions</li>
 *     <li>Handling input validation and file reading</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class ConsoleMain {

    /** Manager object that handles all CRUD operations on shipping orders */
    private final ShippingOrderManager manager;

    /** Scanner used for user input throughout the console application */
    private final Scanner scanner;

    /**
     * Constructor initializes the order manager and scanner for input.
     */
    public ConsoleMain() {
        manager = new ShippingOrderManager();
        scanner = new Scanner(System.in);
    }

    /**
     * Main program loop that displays the menu and handles user input.
     * The loop continues until the user chooses to exit.
     */
    public void run() {
        int choice;
        do {
            choice = readMenuChoice();
            switch (choice) {
                case 1 -> addOrderMenu(); // Option to add orders (manually or from file)
                case 2 -> viewOrders();   // Display all current orders
                case 3 -> updateOrder();  // Update selected order
                case 4 -> deleteOrder();  // Delete selected order
                case 5 -> System.out.println("👋 Exiting... All data cleared from memory.");
                default -> System.out.println("❌ Invalid option. Please choose between 1-5.");
            }
        } while (choice != 5);
    }

    /**
     * Displays the main menu and prompts the user to select a valid option.
     *
     * @return the validated menu option chosen by the user
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
     * Displays a submenu for adding orders, either manually or from a file.
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
     * Loads shipping orders from a structured text file.
     * Skips invalid lines and notifies the user of errors.
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

                String customerName = parts[1].trim();
                String shipperName = parts[2].trim();
                double weight;
                int distance;

                try {
                    weight = Double.parseDouble(parts[3].trim());
                    distance = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid numeric data in line: " + line);
                    continue;
                }

                if (manager.addOrder(customerName, shipperName, weight, distance)) {
                    System.out.println("✅ Order loaded: " + customerName + " -> " + shipperName);
                } else {
                    System.out.println("❌ Invalid order in line: " + line);
                }
            }
            System.out.println("✅ File loading complete.");
            viewOrders();
        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found: " + filename);
        }
    }

    /**
     * Prompts the user to manually input order details.
     * Fields are validated before the order is added.
     */
    private void addOrderManually() {
        System.out.print("Customer Name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Shipper Name: ");
        String shipperName = scanner.nextLine().trim();

        double weight = readValidatedDouble("Weight (lbs)", 0.1, 150.0);
        int distance = readValidatedInt("Distance (miles)", 1, 3000);

        if (manager.addOrder(customerName, shipperName, weight, distance)) {
            System.out.println("✅ Order added successfully!");
        } else {
            System.out.println("❌ Failed to add order. Check business rules.");
        }
    }

    /**
     * Displays all shipping orders currently stored in the system.
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
     * Prompts the user for an order ID and updates the weight and distance.
     * Provides feedback if the order ID is invalid.
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
     * Prompts the user for an order ID and deletes the order if it exists.
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
     * Validates that user input is an integer within a specified range.
     * Uses BigInteger to avoid overflow and ensure safe parsing.
     *
     * @param min minimum accepted value (inclusive)
     * @param max maximum accepted value (inclusive)
     * @return validated integer within range
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
     * Prompts the user with a custom message and reads a validated integer.
     *
     * @param prompt custom message displayed before input
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return validated integer input
     */
    private int readValidatedInt(String prompt, int min, int max) {
        System.out.print(prompt + ": ");
        return readValidatedInt(min, max);
    }

    /**
     * Validates that user input is a double within a specified range.
     * Includes protection against overflows and non-numeric input.
     *
     * @param prompt custom label for the user input
     * @param min minimum accepted value
     * @param max maximum accepted value
     * @return validated double value
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
     * Entry point for launching the console-based shipping system.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ConsoleMain app = new ConsoleMain();
        app.run();
    }
}
