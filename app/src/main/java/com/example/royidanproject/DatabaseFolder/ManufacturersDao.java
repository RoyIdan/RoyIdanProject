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
    public Manufacturer getManufacturerByName(String manufacturerName);

    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT *" +
            "FROM [tblManufacturers]" +
            "WHERE LOWER(manufacturerName) = LOWER(:manufacturerName)" +
            ")" +
            "THEN CAST(1 AS BIT)" +
            "ELSE CAST(0 AS BIT) END")
    boolean hasName_caseInsensitive(String manufacturerName);

    @Insert
    public void insert(Manufacturer manufacturer);

    @Insert
    public void insertAll(List<Manufacturer> manufacturers);

    @Delete
    public void delete(Manufacturer manufacturer);

}
