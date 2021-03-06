package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CreditCardDao {
    @Query("SELECT * FROM tblCreditCards WHERE userId = :userId")
    List<CreditCard> getByUserId(long userId);

    @Query("SELECT * FROM tblCreditCards WHERE cardId = :cardId")
    CreditCard getById(long cardId);

    @Query("UPDATE tblCreditCards SET cardBalance = :cardBalance WHERE cardId = :cardId")
    void updateBalanceById(long cardId, double cardBalance);

    @Query("SELECT cardBalance FROM tblCreditCards WHERE cardId = :cardId")
    double getCardBalance(long cardId);

    @Query("UPDATE tblCreditCards SET cardBalance = cardBalance + :balanceToAdd WHERE cardId = :cardId")
    void addBalanceById(long cardId, double balanceToAdd);

    @Update
    void update(CreditCard creditCard);

    @Insert
    long insert(CreditCard creditCard);

    @Delete
    void delete(CreditCard creditCard);
}
