package com.turtillion.estoredelivery.models;

public class NewOrderDetailsDomain {
    String dBoyId;
    String customer_Order_Id;
    String order_id;
    String shopName;
    String shopLocation;
    String shop_id;
    String phone;
    String landmark;
    String latitude;
    String longitude;
    String pickStatus;

    public NewOrderDetailsDomain(String dBoyId,String customer_Order_Id, String order_id, String shopName, String shopLocation, String shop_id, String phone, String landmark, String latitude, String longitude, String pickStatus)
    {
        this.dBoyId = dBoyId;
        this.customer_Order_Id = customer_Order_Id;
        this.order_id = order_id;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.shop_id = shop_id;
        this.phone = phone;
        this.landmark = landmark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pickStatus = pickStatus;
    }

    public String getdBoyId() {return dBoyId; }
    public String getCustomer_Order_Id() {return customer_Order_Id; }
    public String getOrder_id() { return order_id; }
    public String getShopName() { return shopName; }
    public String getShopLocation() { return shopLocation; }

    public String getShop_id() { return shop_id; }
    public String getPhone() { return phone; }
    public String getLandmark() { return landmark; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
    public String getPickStatus() { return pickStatus; }
}
