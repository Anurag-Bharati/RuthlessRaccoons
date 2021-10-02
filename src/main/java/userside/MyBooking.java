package main.java.userside;

import java.sql.Timestamp;

@SuppressWarnings("All")
public class MyBooking {

    private final int booking_id;
    private final String arrival, departure;
    private final float price, total_price;
    private final String room_id;
    private final Timestamp timestamp;
    private final String status;

    public MyBooking(int booking_id, String arrival, String departure, float price,
                     float total_price, String room_id, Timestamp timestamp, String status) {
        this.booking_id = booking_id;
        this.arrival = arrival;
        this.departure = departure;
        this.price = price;
        this.total_price = total_price;
        this.room_id = room_id;
        this.timestamp = timestamp;
        this.status = status;
    }

    public int getBooking_id() {
        return booking_id;
    }


    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public float getPrice() {
        return price;
    }

    public float getTotal_price() {
        return total_price;
    }

    public String getRoom_id() {
        return room_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getStatus() {
        return status;
    }

}