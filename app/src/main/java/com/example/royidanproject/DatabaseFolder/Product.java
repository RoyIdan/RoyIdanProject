package com.example.royidanproject.DatabaseFolder;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblProducts")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String productName;
    private String productManufacturer;
    private double productPrice;
    private double productRating;
    private int productRating_count;
    private int productStock;
    private String productPhoto;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(String productManufacturer) {
        this.productManufacturer = productManufacturer;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductRating() {
        return productRating;
    }

    public void setProductRating(double productRating) {
        this.productRating = productRating;
    }

    public int getProductRating_count() {
        return productRating_count;
    }

    public void setProductRating_count(int productRating_count) {
        this.productRating_count = productRating_count;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }
}
