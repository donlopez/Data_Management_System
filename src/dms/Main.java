package dms;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<ShippingOrder> orders = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Shipping Management System ===");
            System.out.println("1. Add Shipping Order");
            System.out.println("2. View All Orders");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addOrderLoop();
                case 2 -> viewOrders();
                case 3 -> {
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please select 1-3.");
            }
        }
    }

    private static void addOrderLoop() {
        boolean addMore = true;

        while (addMore) {
            addOrder();
            System.out.print("\nEnter 1 to add another order or 0 to return to the main menu: ");
            int again = readInt();
            if (again != 1) {
                addMore = false;
            }
        }
    }

    private static void addOrder() {
        String customer;
        String shipper;
        double weight = 0;
        int distance = 0;

        System.out.print("Customer Name: ");
        customer = scanner.nextLine().trim();

        System.out.print("Shipper Name: ");
        shipper = scanner.nextLine().trim();

        while (weight <= 0 || weight > 10) {
            System.out.print("Enter weight in pounds (max 10 lbs): ");
            weight = readDouble();

            if (weight <= 0) {
                System.out.println("Error: Weight must be greater than zero.");
            } else if (weight > 10) {
                System.out.println("Error: We don't ship packages over 10 pounds.");
            }
        }

        while (distance <= 0) {
            System.out.print("Enter distance in miles: ");
            distance = readInt();

            if (distance <= 0) {
                System.out.println("Error: Distance must be greater than zero.");
            }
        }

        try {
            ShippingOrder order = new ShippingOrder(nextId++, customer, shipper, weight, distance);
            orders.add(order);
            System.out.println("Order added: " + order);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        for (ShippingOrder order : orders) {
            System.out.println(order);
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid integer: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
