package com.example.fruit_shop.Model;

public class Product {
    private String name;
    private int imageResId;
    private double price;
    private String location;

    // Constructor
    public Product(String name, int imageResId, double price, String location) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.location = location;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
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
}
