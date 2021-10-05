package main.java.adminside;

import java.sql.Date;
/**
 * <h2>A CLASS TO SAVE RESERVED ROOMS DETAILS</h2>
 */
@SuppressWarnings("All")
public class ReservedRooms {
    private int RID;
    private String fullName;
    private String phoneNo;
    private Date arrival;
    private Date departure;
    private Float price;

    public ReservedRooms(int RID, String fullName, String phoneNo, Date arrival, Date departure, Float price) {
        this.RID = RID;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.arrival = arrival;
        this.departure = departure;
        this.price = price;
    }

    public int getRID() {
        return RID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public Date getArrival() {
        return arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public Float getPrice() {
        return price;
    }
}
