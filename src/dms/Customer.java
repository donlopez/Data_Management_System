package dms;

/**
 * Represents a Customer in the Data Management System.
 * Stores customer ID, name, email, and phone number.
 *
 * <p>This class is used to associate customer information with shipping orders.</p>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class Customer {

    /** Unique identifier for the customer (usually database-generated) */
    private int customerId;

    /** Customer's full name */
    private String name;

    /** Customer's email address */
    private String email;

    /** Customer's phone number */
    private String phone;

    /**
     * Constructs a Customer object with specified ID, name, email, and phone.
     *
     * @param customerId the unique ID for the customer
     * @param name the full name of the customer
     * @param email the customer's email address
     * @param phone the customer's phone number
     */
    public Customer(int customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Returns the customer ID.
     *
     * @return customerId as an integer
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer's full name.
     *
     * @return name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the customer's email address.
     *
     * @return email as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the customer's phone number.
     *
     * @return phone as a String
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns a string representation of the customer (name only).
     *
     * @return customer's name
     */
    @Override
    public String toString() {
        return name;
    }
}
