package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface AccessoriesDao extends IProductDao {

    @Query("SELECT * FROM tblAccessories")
    List<Accessory> getAll();

    @Query("SELECT * FROM tblAccessories ORDER BY productName")
    List<Accessory> getAll_orderByName();

    @Query("SELECT * FROM tblAccessories WHERE productId = :accessoryId")
    Accessory getAccessoryById(long accessoryId);

    @Query("UPDATE tblAccessories SET productStock = :stock WHERE productId = :id")
    void updateStockById(long id, int stock);

    @Insert
    long insert(Accessory accessory);

    @RawQuery
    List<Accessory> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

}
