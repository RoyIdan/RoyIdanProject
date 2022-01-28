package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface OrdersDao {

    @Insert
    long insert(Order order);

    @Query("SELECT * FROM tblOrders WHERE customerId = :customerId")
    List<Order> getByCustomerId(long customerId);

    @Query("SELECT * FROM tblOrders WHERE orderDatePurchased BETWEEN :from AND :until")
    List<Order> getByDateRange(Date from, Date until);

    @Query("SELECT * FROM tblorders")
    List<Order> getAll();

}
