package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblSmartphones")
public class Smartphone extends Product {
    private PhoneColor phoneColor;
    private PhoneManufacturer phoneManufacturer;
    private String phoneModel;
    private int phoneScreenSize;
    private int phoneStorageSize;
    private int phoneRamSize;

    public PhoneColor getPhoneColor() {
        return phoneColor;
    }

    public void setPhoneColor(PhoneColor phoneColor) {
        this.phoneColor = phoneColor;
    }

    public PhoneManufacturer getPhoneManufacturer() {
        return phoneManufacturer;
    }

    public void setPhoneManufacturer(PhoneManufacturer phoneManufacturer) {
        this.phoneManufacturer = phoneManufacturer;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public int getPhoneScreenSize() {
        return phoneScreenSize;
    }

    public void setPhoneScreenSize(int phoneScreenSize) {
        this.phoneScreenSize = phoneScreenSize;
    }

    public int getPhoneStorageSize() {
        return phoneStorageSize;
    }

    public void setPhoneStorageSize(int phoneStorageSize) {
        this.phoneStorageSize = phoneStorageSize;
    }

    public int getPhoneRamSize() {
        return phoneRamSize;
    }

    public void setPhoneRamSize(int phoneRamSize) {
        this.phoneRamSize = phoneRamSize;
    }

    public enum PhoneColor {
        Black, White, Green,
    }

    public enum PhoneManufacturer {
        Samsung, Apple, Xiaomi
    }

}


