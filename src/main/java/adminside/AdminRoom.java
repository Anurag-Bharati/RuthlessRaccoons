package main.java.adminside;

@SuppressWarnings("All")
public class AdminRoom {
    private final String RID;
    private final String Type;
    private final String Classlist;
    private final String Amenity;
    private final float Price;
    private final int Floor;
    private final String Status;


    public AdminRoom(String RID, String type, String aClass, String amenity, float price, int floor, String status) {
        this.RID = RID;
        Type = type;
        Classlist = aClass;
        Amenity = amenity;
        Price = price;
        Floor = floor;
        Status = status;
    }

    public String getRID() {
        return RID;
    }

    public String getType() {
        return Type;
    }

    public String getClasslist() {
        return Classlist;
    }

    public String getAmenity() {
        return Amenity;
    }

    public float getPrice() {
        return Price;
    }

    public int getFloor() {
        return Floor;
    }

    public String getStatus() {
        return Status;
    }
}
