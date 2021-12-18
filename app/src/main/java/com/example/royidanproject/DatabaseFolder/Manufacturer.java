package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblManufacturers")
public class Manufacturer {
    @PrimaryKey(autoGenerate = true)
    private long manufacturerId;
    private String manufacturerName;

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
}
