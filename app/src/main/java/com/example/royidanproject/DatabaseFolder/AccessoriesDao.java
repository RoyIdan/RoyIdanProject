package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface AccessoriesDao extends IProductDao {

    @Query("SELECT * FROM tblAccessories")
    List<Accessory> getAll();

    @Query("SELECT * FROM tblAccessories WHERE productStock > 0")
    List<Accessory> getAll_whereInStock();


    @Query("SELECT * FROM tblAccessories ORDER BY productName")
    List<Accessory> getAll_orderByName();

    @Query("SELECT * FROM tblAccessories WHERE productId = :accessoryId")
    Accessory getAccessoryById(long accessoryId);

    @Query("UPDATE tblAccessories SET productStock = :stock WHERE productId = :id")
    void updateStockById(long id, int stock);

    @Query("SELECT * FROM tblAccessories WHERE manufacturerId = :manufacturerId")
    List<Accessory> getByManufacturerId(long manufacturerId);

    @Query("SELECT * FROM tblAccessories WHERE manufacturerId = :manufacturerId AND productStock > 0")
    List<Accessory> getByManufacturerId_whereInStock(long manufacturerId);

    @Insert
    long insert(Accessory accessory);

    @Delete
    void delete(Accessory accessory);

    @Update
    void update(Accessory accessory);

    @RawQuery
    List<Accessory> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

}
