package main.java.userside;

public class Room extends Amenities{
    protected String type, name, tag;
    protected int floor;
    protected double price;
    protected boolean available;

    public Room(boolean WIFI, boolean AC, boolean FAN, boolean ATTACHED, boolean SINGLE, boolean DOUBLE,
                boolean TRIPLE, String type, String name, String tag, int floor, double price, boolean available) {
        super(WIFI, AC, FAN, ATTACHED, SINGLE, DOUBLE, TRIPLE);
        this.type = type;
        this.name = name;
        this.tag = tag;
        this.floor = floor;
        this.price = price;
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
