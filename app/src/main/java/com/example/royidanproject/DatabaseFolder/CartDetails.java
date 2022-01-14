package com.example.royidanproject.DatabaseFolder;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblOrdersDetails", primaryKeys = {"userId","productId"}, foreignKeys = {
        @ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "userId"),
        @ForeignKey(entity = Product.class, parentColumns = "productId", childColumns = "productId")})
public class CartDetails {
    private long userId;
    private long productId;
    private int productQuantity;
    private double productOriginalPrice;
}
