package dms;

/**
 * Represents a Shipper in the Data Management System.
 * Stores the shipper's ID, name, and contact phone number.
 *
 * <p>This class is used to associate shipping orders with a shipping provider.</p>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class Shipper {

    /** Unique identifier for the shipper (usually database-generated) */
    private int shipperId;

    /** Name of the shipping company or provider */
    private String name;

    /** Phone number for contacting the shipper */
    private String phone;

    /**
     * Constructs a Shipper object with ID, name, and phone number.
     *
     * @param shipperId the unique identifier for the shipper
     * @param name the name of the shipper
     * @param phone the contact phone number for the shipper
     */
    public Shipper(int shipperId, String name, String phone) {
        this.shipperId = shipperId;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Returns the shipper's ID.
     *
     * @return the shipper ID
     */
    public int getShipperId() {
        return shipperId;
    }

    /**
     * Returns the shipper's name.
     *
     * @return the name of the shipper
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the shipper's phone number.
     *
     * @return the shipper's contact phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns a string representation of the shipper (name only).
     *
     * @return the shipper's name
     */
    @Override
    public String toString() {
        return name;
    }
}
