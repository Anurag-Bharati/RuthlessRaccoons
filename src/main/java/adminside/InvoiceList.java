package main.java.adminside;

import java.sql.Date;

/**
 * <h2>A CLASS TO SAVE ALL THE INVOICE DETAILS</h2>
 */
@SuppressWarnings("All")
public class InvoiceList {
    private int CID;

    private String name;
    private Date date;
    private Float totalAmount;
    private String status;

    public InvoiceList( int BID, String name, Date date, Float totalAmount, String status) {
        this.CID = BID;
        this.name = name;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Integer getCID() {
        return CID;
    }
    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}
