package com.example.royidanproject.DatabaseFolder;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "tblOrders", foreignKeys = {@ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "customerId"),
                                                @ForeignKey(entity = PaymentCard.class, parentColumns = "cardId", childColumns = "paymentCardId")})
public class Order {
    @PrimaryKey(autoGenerate = true)
    private long orderId;
    private long customerId; // FK
    @androidx.room.TypeConverters(TypeConverters.class)
    private Date orderDatePurchased;
    private long paymentCardId; // FK

}
