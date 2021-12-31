package com.example.royidanproject.DatabaseFolder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ManufacturersDao {
    @Query("select * from tblManufacturers")
    public List<Manufacturer> getAll();

    @Query("select * from tblManufacturers where manufacturerId = :manufacturerId")
    public Manufacturer getManufacturerById(long manufacturerId);

    @Query("select * from tblManufacturers where manufacturerName = :manufacturerName")
    public Manufacturer getManufacturerByName(long manufacturerName);

    @Insert
    public void insert();

    @Delete
    public void deleteById(long id);

}
