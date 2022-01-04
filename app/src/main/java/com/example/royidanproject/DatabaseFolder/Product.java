package com.example.royidanproject.DatabaseFolder;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity(tableName = "tblProducts")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private long manufacturerId;
    private int productRating_sum;
    private int productRating_count;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public int getProductRating_sum() {
        return productRating_sum;
    }

    public void setProductRating_sum(int productRating_sum) {
        this.productRating_sum = productRating_sum;
    }

    public int getProductRating_count() {
        return productRating_count;
    }

    public void setProductRating_count(int productRating_count) {
        this.productRating_count = productRating_count;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public double getProductRating() {
        return (double) productRating_count / productRating_sum;
    }

    public void addProductRating(int rating) {
        productRating_sum += rating;
        productRating_count++;
    }
}
