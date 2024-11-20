package com.example.fruit_shop.Model;

public class Product {

    private String name;
    private double price;
    private String location;
    private int imageResId;  // Thay imageURL thành imageResId để lưu ID tài nguyên drawable

    // Constructor
    public Product(String name, double price, String location, int imageResId) {
        this.name = name;
        this.price = price;
        this.location = location;
        this.imageResId = imageResId;  // Khởi tạo imageResId
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImageResId() {  // Getter cho imageResId
        return imageResId;
    }

    public void setImageResId(int imageResId) {  // Setter cho imageResId
        this.imageResId = imageResId;
    }
}
