package com.example.royidanproject.DatabaseFolder;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblPaymentCards", foreignKeys = @ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "userId"))
public class PaymentCard {
    @PrimaryKey
    private long cardId;

    private long userId; //FK
    private String cardNumber;
    private String cvv;
    @androidx.room.TypeConverters(TypeConverters.class)
    private Date cardExpireDate;
}
