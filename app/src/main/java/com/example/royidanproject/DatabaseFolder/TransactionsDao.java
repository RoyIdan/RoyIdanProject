package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TransactionsDao {
    @Query("SELECT * FROM tblTransactions WHERE userId = :userId")
    List<Transaction> getTransactionsByUserId(long userId);

    @Insert
    void insert(Transaction transaction);

}
