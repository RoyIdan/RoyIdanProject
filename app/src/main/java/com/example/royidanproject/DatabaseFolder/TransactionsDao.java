package com.example.royidanproject.DatabaseFolder;

import com.example.royidanproject.Utility.TransactionManager;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TransactionsDao {
    @Query("SELECT * FROM tblTransactions WHERE userId = :userId")
    List<Transaction> getTransactionsByUserId(long userId);

    @Query("SELECT * FROM tblTransactions WHERE userId = :userId AND transactionDate BETWEEN :from AND :until")
    List<Transaction> getTransactionsByDateRange(long userId, Date from, Date until);

    @Query("SELECT * FROM tblTransactions WHERE userId = :userId AND transactionType = 'Purchase'")
    List<Transaction> getPurchasesByUserId(long userId);

    @Query("SELECT * FROM tblTransactions WHERE userId = :userId AND transactionType = 'Purchase' AND transactionDate BETWEEN :from AND :until")
    List<Transaction> getPurchasesByDateRange(long userId, Date from, Date until);

    @Query("SELECT * FROM tblTransactions WHERE userId = :userId AND transactionType = 'Deposit'")
    List<Transaction> getDepositsByUserId(long userId);

    @Query("SELECT * FROM tblTransactions WHERE userId = :userId AND transactionType = 'Deposit' AND transactionDate BETWEEN :from AND :until")
    List<Transaction> getDepositsByDateRange(long userId, Date from, Date until);


    @Insert
    void insert(Transaction transaction);

}
