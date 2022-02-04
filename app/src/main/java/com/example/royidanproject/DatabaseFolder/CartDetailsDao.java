package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartDetailsDao {
    @Query("SELECT * FROM tblCartDetails WHERE userId = :userId")
    List<CartDetails> getCartDetailsByUserId(long userId);

    @Query("SELECT * FROM tblCartDetails WHERE userId = :userId and productId = :productId and tableId = :tableId")
    CartDetails getCartDetailsByKeys(long userId, long productId, long tableId);

    @Query("SELECT productId FROM tblCartDetails WHERE productId = :productId AND tableId = :tableId")
    List<Long> isProductExist(long productId, long tableId);

    @Query("SELECT * FROM tblCartDetails WHERE userId = :userId LIMIT 1")
    CartDetails hasAny(long userId);

    @Insert
    long addCartItem(CartDetails cartDetails);

    @Update
    void updateCartDetails(CartDetails cartDetails);

    @Delete
    void deleteCartDetailsByReference(CartDetails cartDetails);

    @Query("DELETE FROM tblCartDetails WHERE userId = :userId")
    void deleteCartDetailsByUserId(long userId);
}
