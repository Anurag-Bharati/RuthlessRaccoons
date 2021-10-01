package main.java.userside;

import java.sql.Timestamp;

@SuppressWarnings("All")
public class MyBooking {

    private final int booking_id;
    private final String arrival, departure;
    private final float room_price, total_price;
    private final String room_name;
    private final Timestamp timestamp;
    private final String status;

    public MyBooking(int booking_id, String arrival, String departure, float room_price,
                     float total_price, String room_name, Timestamp timestamp, String status) {
        this.booking_id = booking_id;
        this.arrival = arrival;
        this.departure = departure;
        this.room_price = room_price;
        this.total_price = total_price;
        this.room_name = room_name;
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

    public float getRoom_price() {
        return room_price;
    }

    public float getTotal_price() {
        return total_price;
    }

    public String getRoom_name() {
        return room_name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getStatus() {
        return status;
    }

}