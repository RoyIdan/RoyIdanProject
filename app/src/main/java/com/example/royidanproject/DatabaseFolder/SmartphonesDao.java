package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface SmartphonesDao extends IProductDao {

    @Query("SELECT * FROM tblSmartphones")
    List<Smartphone> getAll();

    @Query("SELECT * FROM tblSmartphones WHERE productStock > 0")
    List<Smartphone> getAll_whereInStock();

    @Query("SELECT * FROM tblSmartphones ORDER BY productName")
    List<Smartphone> getAll_orderByName();

    @Query("SELECT * FROM tblSmartphones WHERE productId = :smartphoneId")
    Smartphone getSmartphoneById(long smartphoneId);

    @Query("UPDATE tblSmartphones SET productStock = :stock WHERE productId = :id")
    void updateStockById(long id, int stock);

    @Query("SELECT * FROM tblSmartphones WHERE manufacturerId = :manufacturerId")
    List<Smartphone> getByManufacturerId(long manufacturerId);

    @Query("SELECT * FROM tblSmartphones WHERE manufacturerId = :manufacturerId AND productStock > 0")
    List<Smartphone> getByManufacturerId_whereInStock(long manufacturerId);

    @Insert
    long insert(Smartphone smartphone);

    @Delete
    void delete(Smartphone smartphone);

    @Update
    void update(Smartphone smartphone);

    @RawQuery
    List<Smartphone> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

}
