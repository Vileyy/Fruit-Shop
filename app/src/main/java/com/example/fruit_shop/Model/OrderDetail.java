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
                '}';
    }
}