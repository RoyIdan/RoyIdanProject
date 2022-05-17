package com.example.royidanproject.Utility;

import android.content.Context;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Transaction;

import java.util.Date;

public class TransactionManager {

    public enum TransactionType {
        Deposit, Purchase
    }

    public static void makeTransaction(Context context, TransactionType type, long userId,
                                       long cardId, double amount, String description) {
        AppDatabase db = AppDatabase.getInstance(context);
        if (type == TransactionType.Purchase) {
            if (db.creditCardDao().getCardBalance(cardId) < amount) {
                return;
            }
        }

        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setCreditCardId(cardId);
        t.setTransactionType(type);
        t.setTransactionAmount(amount);
        t.setTransactionDescription(description);
        t.setTransactionDate(new Date());
        db.transactionsDao().insert(t);

        if (type == TransactionType.Purchase) amount = - amount;

        db.creditCardDao().addBalanceById(cardId, amount);
    }

}
