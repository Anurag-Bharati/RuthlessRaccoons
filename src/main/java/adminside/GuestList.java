package main.java.adminside;


/**
 * <h2>A CLASS TO SAVE GUEST DETAILS</h2>
 */
@SuppressWarnings("All")
public class GuestList {
    private final int guestID;
    private final String fullName;
    private final String gender;
    private final String phoneNo;
    private final String gmail;

    public GuestList(int guestID, String fullName, String gender, String phoneNo, String gmail) {
        this.guestID = guestID;
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.gmail = gmail;
    }

    public int getGuestID() {
        return guestID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGmail() {
        return gmail;
    }
}
