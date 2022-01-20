package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CreditCardDao {
    @Query("SELECT * FROM tblCreditCards WHERE userId = :userId")
    List<CreditCard> getByUserId(long userId);

    @Query("UPDATE tblCreditCards SET cardBalance = :cardBalance WHERE cardId = :cardId")
    void updateBalanceById(long cardId, double cardBalance);
}
