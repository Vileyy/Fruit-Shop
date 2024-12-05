package com.example.fruit_shop.Model;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetail implements Serializable {
    private String userUid;
    private String userName;
    private ArrayList<CartItem> orderItems;
    private String address;
    private String phoneNumber;
    private String totalPrice;
    private Boolean orderAccepted = false;
    private Boolean paymentReceived = false;
    private String itemPushKey;
    private Long currentTime = 0L;
    private Boolean statusPending; // Thêm trường statusPending
    private Boolean successfulDelivery;



    protected OrderDetail(Parcel in) {
        userUid = in.readString();
        userName = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        totalPrice = in.readString();
        byte tmpOrderAccepted = in.readByte();
        orderAccepted = tmpOrderAccepted == 0 ? null : tmpOrderAccepted == 1;
        byte tmpPaymentReceived = in.readByte();
        paymentReceived = tmpPaymentReceived == 0 ? null : tmpPaymentReceived == 1;
        itemPushKey = in.readString();
        if (in.readByte() == 0) {
            currentTime = null;
        } else {
            currentTime = in.readLong();
        }
        // Đọc giá trị của statusPending từ Parcel
        if (in.readByte() == 0) {
            statusPending = null;
        } else {
            statusPending = in.readByte() == 1; // 1 cho true, 0 cho false
        }
        // Đọc giá trị của statusPending từ Parcel
        if (in.readByte() == 0) {
            successfulDelivery = null;
        } else {
            successfulDelivery = in.readByte() == 1; // 1 cho true, 0 cho false
        }
    }


    public OrderDetail() {
    }

    public OrderDetail(String userUid, String userName, ArrayList<CartItem> orderItems, String address, String phoneNumber, String totalPrice, Boolean orderAccepted, Boolean paymentReceived, String itemPushKey, Long currentTime) {
        this.userUid = userUid;
        this.userName = userName;
        this.orderItems = orderItems;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
        this.itemPushKey = itemPushKey;
        this.currentTime = currentTime;
        this.statusPending = false; // Giá trị mặc định là false
        this.successfulDelivery = false;
    }




    public  Boolean getSuccessfulDelivery() {
        return successfulDelivery;
    }
    public void setSuccessfulDelivery(Boolean successfulDelivery) {
        this.successfulDelivery = successfulDelivery;
    }
    // Getter và Setter cho statusPending
    public void setStatusPending(Boolean statusPending) {
        this.statusPending = statusPending;
    }

    public Boolean getStatusPending() {
        return statusPending;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderItems(ArrayList<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderAccepted(Boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public void setPaymentReceived(Boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<CartItem> getOrderItems() {
        return orderItems;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public Boolean getOrderAccepted() {
        return orderAccepted;
    }

    public Boolean getPaymentReceived() {
        return paymentReceived;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "userUid='" + userUid + '\'' +
                ", userName='" + userName + '\'' +
                ", orderItems=" + orderItems +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", orderAccepted=" + orderAccepted +
                ", paymentReceived=" + paymentReceived +
                ", itemPushKey='" + itemPushKey + '\'' +
                ", currentTime=" + currentTime +
                ", statusPending=" + statusPending + // In thêm statusPending
                ", successfulDelivery=" + successfulDelivery + // In thêm successfulDelivery
                '}';
    }

    // Viết lại hàm viết Parcel cho statusPending
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(totalPrice);
        dest.writeByte((byte) (orderAccepted == null ? 0 : orderAccepted ? 1 : 2));
        dest.writeByte((byte) (paymentReceived == null ? 0 : paymentReceived ? 1 : 2));
        dest.writeString(itemPushKey);
        if (currentTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(currentTime);
        }
        if (statusPending == null) {
            dest.writeByte((byte) 0);  // null
        } else {
            dest.writeByte((byte) (statusPending ? 1 : 2));  // true or false
        }
        if (successfulDelivery == null) {
            dest.writeByte((byte) 0);  // null
        } else {
            dest.writeByte((byte) (successfulDelivery ? 1 : 2));  // true or false
        }
    }

}
