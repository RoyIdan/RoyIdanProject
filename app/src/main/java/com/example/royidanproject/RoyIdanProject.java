package com.example.royidanproject;

import static com.example.royidanproject.DatabaseFolder.Watch.WatchColor.Black;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.StartupThread;

public class RoyIdanProject extends Application {
    private AppDatabase db;
    public boolean isFirstRun;
    public Class<? extends AppCompatActivity> firstActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        isFirstRun = true;
        firstActivity = StartupActivity.class;

        db = AppDatabase.getInstance(getApplicationContext());
        if (false) { // use fake products
            if (db.manufacturersDao().getAll().isEmpty()) {
                addSampleProducts();
            }
        }


    }

    private void addSampleProducts() {

        Manufacturer apple = new Manufacturer();
        apple.setManufacturerName("Apple");

        Manufacturer samsung = new Manufacturer();
        samsung.setManufacturerName("Samsung");

        db.manufacturersDao().insert(apple);
        db.manufacturersDao().insert(samsung);


        Smartphone iphone11 = new Smartphone();
        iphone11.setProductPhoto("iphone 11.png");
        iphone11.setProductStock(5);
        iphone11.setPhoneColor(Smartphone.PhoneColor.Black);
        iphone11.setPhoneRamSize(8);
        iphone11.setPhoneScreenSize(8);
        iphone11.setPhoneStorageSize(128);
        iphone11.setManufacturerId(1); // Apple
        iphone11.setProductPrice(2000);
        iphone11.setProductRating_count(5);
        iphone11.setProductRating_sum(21);
        iphone11.setProductName("iPhone 11");

        Smartphone galaxyS10 = new Smartphone();
        galaxyS10.setProductPhoto("galaxyS10.jpg");
        galaxyS10.setProductStock(3);
        galaxyS10.setPhoneColor(Smartphone.PhoneColor.White);
        galaxyS10.setPhoneRamSize(6);
        galaxyS10.setPhoneScreenSize(7);
        galaxyS10.setPhoneStorageSize(64);
        galaxyS10.setManufacturerId(2); // Samsung
        galaxyS10.setProductPrice(1500);
        galaxyS10.setProductRating_sum(22);
        galaxyS10.setProductRating_count(5);
        galaxyS10.setProductName("Galaxy S10");

        Watch appleWatch = new Watch();
        appleWatch.setProductPhoto("AppleWatch.png");
        appleWatch.setProductStock(4);
        appleWatch.setManufacturerId(1); // Apple
        appleWatch.setProductPrice(900);
        appleWatch.setProductRating_sum(19);
        appleWatch.setProductRating_count(12);
        appleWatch.setProductRating_count(3);
        appleWatch.setProductName("44mm Apple Watch SE GPS");
        appleWatch.setWatchSize(Watch.WatchSize.M);
        appleWatch.setWatchColor(Black);


        db.smartphonesDao().insert(iphone11);
        db.smartphonesDao().insert(galaxyS10);
        db.watchesDao().insert(appleWatch);
    }
}
