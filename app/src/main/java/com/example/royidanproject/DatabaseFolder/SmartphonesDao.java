package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

@Dao
public interface SmartphonesDao {

    @Query("SELECT * FROM tblSmartphones")
    List<Smartphone> getAll();

    @Query("SELECT * FROM tblSmartphones ORDER BY productName")
    List<Smartphone> getAll_orderByName();

    @Query("SELECT * FROM tblSmartphones WHERE productId = :smartphoneId")
    Smartphone getSmartphoneById(long smartphoneId);

    @Insert
    long insert(Smartphone smartphone);

    @RawQuery
    List<Smartphone> getByQuery(androidx.sqlite.db.SupportSQLiteQuery query);

}
