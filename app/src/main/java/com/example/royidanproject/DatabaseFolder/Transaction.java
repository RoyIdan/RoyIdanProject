package com.example.royidanproject.DatabaseFolder;

import com.example.royidanproject.Utility.TransactionManager;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblTransactions", foreignKeys = {
    @ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "userId"),
    @ForeignKey(entity = CreditCard.class, parentColumns = "cardId", childColumns = "creditCardId")})
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private long transactionId;
    private long userId; // fk
    private long creditCardId; // fk

    private TransactionManager.TransactionType transactionType;
    private double transactionAmount;
    private String transactionDescription;
    private Date transactionDate;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(long creditCardId) {
        this.creditCardId = creditCardId;
    }

    public TransactionManager.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionManager.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
