package com.example.royidanproject.Application;

import static com.example.royidanproject.DatabaseFolder.Watch.WatchColor.שחור;
import static com.example.royidanproject.MainActivity.SP_NAME;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.royidanproject.Custom.Currency;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.Services.MusicService;
import com.example.royidanproject.Utility.CurrencyManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class RoyIdanProject extends Application {
    private AppDatabase db;
    public boolean isFirstRun;
    public Class<? extends AppCompatActivity> firstActivity;
    public boolean isMusicServiceRunning;
    public Song currentSong;

    private static RoyIdanProject instance;
    public static RoyIdanProject getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        isFirstRun = true;
        firstActivity = MainActivity.class;

        isMusicServiceRunning = false;

        db = AppDatabase.getInstance(getApplicationContext());
        if (true) { // use fake products
            if (db.manufacturersDao().getAll().isEmpty()) {
                addSampleManager(); // s32334@nhs.co.il : 123
                addSampleProducts();
            }
        }

        SharedPreferences settingsSp = getSharedPreferences("SP_SETTINGS", 0);
        int currMusicId = settingsSp.getInt("musicId", 0);
        if (currMusicId <= 0) {
            currentSong = Song.elevator;
        } else {
            currentSong = Song.values()[currMusicId];
        }

        startService();

        CurrencyManager.setCurrentCurrency(Currency.getCurrency(settingsSp.getInt("currencyId", 0)));

//        startActivity(new Intent(getApplicationContext(), MainActivity.class).
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void startService() {
        Intent intent = new Intent(RoyIdanProject.this, MusicService.class);
        intent.putExtra("isRunning", true);
        startService(intent);
        isMusicServiceRunning = true;
    }

    public void restartMusicService() {
        Intent intent = new Intent(RoyIdanProject.this, MusicService.class);
        stopService(intent);

        startService();
    }


    public boolean isConnectedViaWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
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
        cc.setCardCompany(CreditCard.CardCompany.לאומי);
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
        iphone11.setProductName("iPhone 11");
        iphone11.setProductDateAdded(new Date(1646488087000L)); // 5 March 2022 15:48:07

        Smartphone galaxyS10 = new Smartphone();
        galaxyS10.setProductPhoto("galaxyS10.jpg");
        galaxyS10.setProductStock(3);
        galaxyS10.setPhoneColor(Smartphone.PhoneColor.לבן);
        galaxyS10.setPhoneRamSize(6);
        galaxyS10.setPhoneScreenSize(7);
        galaxyS10.setPhoneStorageSize(64);
        galaxyS10.setManufacturerId(2); // Samsung
        galaxyS10.setProductPrice(1500);
        galaxyS10.setProductName("Galaxy S10");
        galaxyS10.setProductDateAdded(new Date(1646574487000L)); // 6 March 2022 15:48:07

        Watch appleWatch = new Watch();
        appleWatch.setProductPhoto("AppleWatch44mm.jpg");
        appleWatch.setProductStock(4);
        appleWatch.setManufacturerId(1); // Apple
        appleWatch.setProductPrice(900);
        appleWatch.setProductName("44mm Apple Watch SE GPS");
        appleWatch.setWatchSize(44);
        appleWatch.setWatchColor(שחור);
        appleWatch.setProductDateAdded(new Date(1646660887000L)); // 7 March 2022 15:48:07

        db.smartphonesDao().insert(iphone11);
        db.smartphonesDao().insert(galaxyS10);
        db.watchesDao().insert(appleWatch);
    }

    public enum Song {
        elevator, spring_in_my_step, retail_store
    }

    public int getSongId() {
        switch (currentSong) {
            case elevator:
                return R.raw.music_elevator;
            case spring_in_my_step:
                return R.raw.music_spring_in_my_step;
            case retail_store:
                return R.raw.music_retail_store;
            default:
                return 0;
        }
    }
}
