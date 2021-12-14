package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;

@Entity(tableName = "tblOrdersDetails", primaryKeys = {"orderId","productId"})
public class OrderDetails {
    private long orderId;
    private long productId;
    private int productQuantity;
    private double productOriginalPrice;
}
