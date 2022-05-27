package com.example.royidanproject.DatabaseFolder;

import java.io.Serializable;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblOrders", foreignKeys = {@ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "customerId"),
                                                @ForeignKey(entity = CreditCard.class, parentColumns = "cardId", childColumns = "creditCardId")})
public class Order implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long orderId;
    private long customerId; // FK
    private Date orderDatePurchased;
    private long creditCardId; // FK

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDatePurchased() {
        return orderDatePurchased;
    }

    public void setOrderDatePurchased(Date orderDatePurchased) {
        this.orderDatePurchased = orderDatePurchased;
    }

    public long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(long creditCardId) {
        this.creditCardId = creditCardId;
    }
}
