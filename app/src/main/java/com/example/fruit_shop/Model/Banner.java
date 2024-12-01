package com.example.fruit_shop.Model;

public class Banner {
    private String id;
    private String imageUrl;

    // Constructor mặc định cần thiết cho Firebase
    public Banner() {
    }

    // Constructor
    public Banner(String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
