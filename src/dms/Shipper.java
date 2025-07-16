package dms;

public class Shipper {
    private int shipperId;
    private String name;
    private String phone;

    public Shipper(int shipperId, String name, String phone) {
        this.shipperId = shipperId;
        this.name = name;
        this.phone = phone;
    }

    public int getShipperId() {
        return shipperId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name;
    }
}
