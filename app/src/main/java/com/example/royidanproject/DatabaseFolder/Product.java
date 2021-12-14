package com.example.royidanproject.DatabaseFolder;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblProducts")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String productName;
    private double productPrice;
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

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }
}
