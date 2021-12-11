package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProductsDao {
    @Insert
    public long insert(Product product);

    @Query("SELECT * FROM tblProducts WHERE productId = :id")
    public Product getProductById(long id);
}
