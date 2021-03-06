package com.example.royidanproject.DatabaseFolder;

import java.io.Serializable;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblCreditCards", foreignKeys = @ForeignKey(entity = Users.class, parentColumns = "userId", childColumns = "userId"))
public class CreditCard implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long cardId;
    private long userId; //FK
    private String cardNumber;
    private String cvv;
    private Double cardBalance;
    private Date cardExpireDate;
    private CardCompany cardCompany;

    public enum CardCompany {
        אמריקן, ישראכרט, מאסטרכארד, לאומי
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(Double cardBalance) {
        this.cardBalance = cardBalance;
    }

    public Date getCardExpireDate() {
        return cardExpireDate;
    }

    public void setCardExpireDate(Date cardExpireDate) {
        this.cardExpireDate = cardExpireDate;
    }

    public CardCompany getCardCompany() {
        return cardCompany;
    }

    public void setCardCompany(CardCompany cardCompany) {
        this.cardCompany = cardCompany;
    }

    @Override
    public String toString() {
        return cardCompany + " - " + cardNumber.substring(12);
    }
}
