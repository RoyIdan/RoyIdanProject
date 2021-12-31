package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WatchesDao {
    @Query("SELECT * FROM tblWatches WHERE productId = :watchId")
    Watch getWatchById(long watchId);

    @Insert
    long insert(Watch watch);

    @Query("SELECT * FROM tblWatches WHERE :query")
    List<Watch> getByQuery(String query);

    @Query("SELECT * FROM tblWatches")
    List<Watch> getAll();
}
