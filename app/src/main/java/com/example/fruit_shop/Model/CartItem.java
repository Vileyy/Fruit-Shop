package com.example.fruit_shop.Model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String productCode;      // Mã sản phẩm (dựa trên id sản phẩm Firebase)
    private String productName;      // Tên sản phẩm
    private String category;         // Danh mục
    private Integer stockQuantity;    // Số lượng tồn kho
    private double price;            // Giá gốc
    private double discountPrice;    // Giá sau giảm giá
    private String discountCode;     // Mã giảm giá
    private String dateIn;           // Ngày nhập
    private String imageUrl;         // URL hình ảnh sản phẩm
    private String edtDescription;   // Mô tả sản phẩm
    private String rating;           // Số sao (đánh giá)

    public CartItem() {
    }

    public CartItem(String productCode, String productName, String category, Integer stockQuantity, double price, double discountPrice, String discountCode, String dateIn, String imageUrl, String edtDescription, String rating) {
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.discountPrice = discountPrice;
        this.discountCode = discountCode;
        this.dateIn = dateIn;
        this.imageUrl = imageUrl;
        this.edtDescription = edtDescription;
        this.rating = rating;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEdtDescription() {
        return edtDescription;
    }

    public void setEdtDescription(String edtDescription) {
        this.edtDescription = edtDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", stockQuantity='" + stockQuantity + '\'' +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", discountCode='" + discountCode + '\'' +
                ", dateIn='" + dateIn + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", edtDescription='" + edtDescription + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}