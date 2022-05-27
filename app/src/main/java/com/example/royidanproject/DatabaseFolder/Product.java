package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tblProducts", foreignKeys =
    @ForeignKey(entity=Manufacturer.class, parentColumns = "manufacturerId", childColumns = "manufacturerId"))
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private String productDescription;
    private long manufacturerId;
    private String productPhoto;
    private Date productDateAdded;

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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public Date getProductDateAdded() {
        return productDateAdded;
    }

    public void setProductDateAdded(Date productDateAdded) {
        this.productDateAdded = productDateAdded;
    }
}
