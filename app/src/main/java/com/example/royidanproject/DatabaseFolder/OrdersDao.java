package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

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

    @Query("SELECT * FROM tblOrders WHERE orderDatePurchased BETWEEN :from AND :until AND customerId = :customerId")
    List<Order> getByDateRangeAndCustomerId(Date from, Date until, long customerId);

    @Query("SELECT * FROM tblorders")
    List<Order> getAll();

    @Query("SELECT * FROM tblOrders WHERE customerId = :customerId LIMIT 1")
    Order hasAny(long customerId);

    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT *" +
            "FROM [tblOrders]" +
            ")" +
            "THEN CAST(1 AS BIT)" +
            "ELSE CAST(0 AS BIT) END")
    boolean hasAny();

    @Query("SELECT * FROM tblOrders WHERE creditCardId = :cardId LIMIT 1")
    Order hasAnyCard(long cardId);

    @Query("SELECT customerId FROM tblOrders WHERE orderId = :orderId")
    long getCustomerIdByOrderId(long orderId);


}
