package dms;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<ShippingOrder> orders = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Shipping Management System ---");
            System.out.println("1. Add Order");
            System.out.println("2. View Orders");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addOrder();
                case 2 -> viewOrders();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addOrder() {
        System.out.print("Customer Name: ");
        String customer = scanner.nextLine();

        System.out.print("Shipper Name: ");
        String shipper = scanner.nextLine();

        System.out.print("Weight in pounds: ");
        double weight = readDouble();

        System.out.print("Distance in miles: ");
        int distance = readInt();

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
