package main.java.userside;

@SuppressWarnings("All")
public class Room {
    private final String RID;
    private final float price;
    public Room(String RID, float price){
        this.RID = RID;
        this.price = price;
    }

    public String getRID() {
        return RID;
    }

    public float getPrice() {
        return price;
    }
}
