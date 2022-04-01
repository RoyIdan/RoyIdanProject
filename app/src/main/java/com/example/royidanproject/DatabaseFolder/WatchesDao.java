package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface WatchesDao extends IProductDao {
    @Query("SELECT * FROM tblWatches WHERE productId = :watchId")
    Watch getWatchById(long watchId);

    @Query("UPDATE tblWatches SET productStock = :stock WHERE productId = :id")
    void updateStockById(long id, int stock);

    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT *" +
            "FROM [tblWatches]" +
            "WHERE manufacturerId = :manufacturerId" +
            ")" +
            "THEN CAST(1 AS BIT)" +
            "ELSE CAST(0 AS BIT) END")
    boolean hasManufacturer(long manufacturerId);

    @Insert
    long insert(Watch watch);

    @Delete
    void delete(Watch watch);

    @Update
    void update(Watch watch);

    @Query("SELECT * FROM tblWatches WHERE manufacturerId = :manufacturerId")
    List<Watch> getByManufacturerId(long manufacturerId);

    @Query("SELECT * FROM tblWatches WHERE manufacturerId = :manufacturerId AND productStock > 0")
    List<Watch> getByManufacturerId_whereInStock(long manufacturerId);

    @RawQuery
    List<Watch> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

    @Query("SELECT * FROM tblWatches")
    List<Watch> getAll();

    @Query("SELECT * FROM tblWatches WHERE productStock > 0")
    List<Watch> getAll_whereInStock();

    @Query("SELECT * FROM tblWatches ORDER BY productName")
    List<Watch> getAll_orderByName();
}
