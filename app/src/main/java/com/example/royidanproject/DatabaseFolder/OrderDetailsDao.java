package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDetailsDao {

    @Insert
    long insert(OrderDetails orderDetails);

    @Query("SELECT * FROM tblOrderDetails WHERE orderId = :orderId")
    List<OrderDetails> getByOrderId(long orderId);

    @Insert
    void insertAll(List<OrderDetails> detailsList);

}
