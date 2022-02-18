package com.example.royidanproject;

import static com.example.royidanproject.DatabaseFolder.Watch.WatchColor.שחור;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.StartupThread;

import java.util.Calendar;
import java.util.Date;

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
        if (true) { // use fake products
            if (db.manufacturersDao().getAll().isEmpty()) {
                addSampleManager(); // s32334@nhs.co.il : 123
                addSampleProducts();
            }
        }

//        startActivity(new Intent(getApplicationContext(), MainActivity.class).
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void addSampleManager() {

        Calendar cal = Calendar.getInstance();
        cal.set(2004, 8, 10);
        Date date = cal.getTime();

        Users manager = new Users();
        manager.setUserName("רועי");
        manager.setUserSurname("עידן");
        manager.setUserGender("זכר");
        manager.setUserBirthdate(date);
        manager.setUserEmail("s32334@nhs.co.il");
        manager.setUserAddress("רחוב סומסום 123");
        manager.setUserCity("בן שמן");
        manager.setUserPassword("123");
        manager.setUserPhone("0509254011");
        manager.setUserPhoto("220116-14:44:07.jpg");

        long managerId = db.usersDao().insert(manager);

        Calendar cardCal = Calendar.getInstance();
        cardCal.set(2022, 7, 0);
        Date cardDate = cardCal.getTime();

        CreditCard cc = new CreditCard();
        cc.setCardBalance(100000D);
        cc.setCardCompany(CreditCard.CardCompany.VISA);
        cc.setCardNumber("1234567890123456");
        cc.setCardExpireDate(cardDate);
        cc.setCvv("123");
        cc.setUserId(managerId);

        db.creditCardDao().insert(cc);

    }

    private void addSampleProducts() {

        Manufacturer apple = new Manufacturer();
        apple.setManufacturerName("Apple");

        Manufacturer samsung = new Manufacturer();
        samsung.setManufacturerName("Samsung");

        db.manufacturersDao().insert(apple);
        db.manufacturersDao().insert(samsung);


        Smartphone iphone11 = new Smartphone();
        iphone11.setProductPhoto("iphone 11.jpg");
        iphone11.setProductStock(5);
        iphone11.setPhoneColor(Smartphone.PhoneColor.שחור);
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
        galaxyS10.setPhoneColor(Smartphone.PhoneColor.לבן);
        galaxyS10.setPhoneRamSize(6);
        galaxyS10.setPhoneScreenSize(7);
        galaxyS10.setPhoneStorageSize(64);
        galaxyS10.setManufacturerId(2); // Samsung
        galaxyS10.setProductPrice(1500);
        galaxyS10.setProductRating_sum(22);
        galaxyS10.setProductRating_count(5);
        galaxyS10.setProductName("Galaxy S10");

        Watch appleWatch = new Watch();
        appleWatch.setProductPhoto("AppleWatch44mm.jpg");
        appleWatch.setProductStock(4);
        appleWatch.setManufacturerId(1); // Apple
        appleWatch.setProductPrice(900);
        appleWatch.setProductRating_sum(19);
        appleWatch.setProductRating_count(12);
        appleWatch.setProductRating_count(3);
        appleWatch.setProductName("44mm Apple Watch SE GPS");
        appleWatch.setWatchSize(44);
        appleWatch.setWatchColor(שחור);

        db.smartphonesDao().insert(iphone11);
        db.smartphonesDao().insert(galaxyS10);
        db.watchesDao().insert(appleWatch);
    }
}
