package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingsDao {
    @Query("SELECT * FROM tblRatings WHERE userId = :userId AND productId = :productId AND tableId = :tableId")
    Rating getByParams(long userId, long productId, long tableId);

    @Query("SELECT AVG(rating) from tblRatings WHERE productId = :productId AND tableId = :tableId")
    float getAverageByProduct(long productId, long tableId);

    @Insert
    long insert(Rating rating);

    @Delete
    void delete(Rating rating);
}
