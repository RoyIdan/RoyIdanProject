package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

@Dao
public interface WatchesDao extends IProductDao {
    @Query("SELECT * FROM tblWatches WHERE productId = :watchId")
    Watch getWatchById(long watchId);

    @Query("UPDATE tblWatches SET productStock = :stock WHERE productId = :id")
    void updateStockById(long id, int stock);

    @Insert
    long insert(Watch watch);

    @RawQuery
    List<Watch> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

    @Query("SELECT * FROM tblWatches")
    List<Watch> getAll();

    @Query("SELECT * FROM tblWatches ORDER BY productName")
    List<Watch> getAll_orderByName();
}
