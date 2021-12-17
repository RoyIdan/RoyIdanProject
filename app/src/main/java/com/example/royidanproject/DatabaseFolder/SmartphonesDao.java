package com.example.royidanproject.DatabaseFolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SmartphonesDao {

    @Query("SELECT * FROM tblSmartphones WHERE productId = :smartphoneId")
    Smartphone getSmartphoneById(long smartphoneId);

    @Insert
    long insert(Smartphone smartphone);

    @Query("SELECT * FROM tblsmartphones WHERE :query")
    List<Smartphone> getByQuery(String query);

    @Query("SELECT * FROM tblsmartphones")
    List<Smartphone> getAll();

}
