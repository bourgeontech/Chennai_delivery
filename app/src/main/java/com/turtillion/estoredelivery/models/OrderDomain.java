package com.turtillion.estoredelivery.models;

public class OrderDomain {
    String dBoyId;
    String order_id;
    String from;
    String to;
    String km;
    String order_date;
    String order_time;

    public OrderDomain(String dBoyId, String order_id, String from, String to, String km, String order_date, String order_time) {
        this.dBoyId = dBoyId;
        this.order_id = order_id;
        this.from = from;
        this.to = to;
        this.km = km;
        this.order_date = order_date;
        this.order_time = order_time;
    }

    public String getdBoyId() { return dBoyId; }

    public String getOrder_id() {
        return order_id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getKm() {
        return km;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

}
