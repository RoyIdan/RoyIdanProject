package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Application.RoyIdanProject;
import com.example.royidanproject.BroadcastReceivers.BatteryReceiver;
import com.example.royidanproject.BroadcastReceivers.WifiStatusReceiver;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.Utility.HorizontalMoving;
import com.example.royidanproject.Utility.PermissionManager;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.Dialogs;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String USERS_FOLDER_NAME = "royIdanProject_Users";
    public static final String PRODUCTS_FOLDER_NAME = "royIdanProject_Products";
    public static final String SP_NAME = "USER_INFO";
    public static final String ADMIN_PHONE = "0509254011";
    private static final String PHYSICAL_PHONE = "0509254011";
    private static final String EMULATOR_PHONE = "555-521-5554";
    public static String SMS_PHONE = PHYSICAL_PHONE;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppDatabase db;

    private BatteryReceiver batteryReceiver;
    private IntentFilter intentFilter;

    private PermissionManager permissionManager;
    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

        batteryReceiver = new BatteryReceiver(findViewById(R.id.tvTitle));
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);

        toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(batteryReceiver);

        toolbarManager.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RoyIdanProject app = ((RoyIdanProject)getApplication());
        if (app.isFirstRun) {
            app.isFirstRun = false;
            if (app.firstActivity != MainActivity.class) {
                startActivity(new Intent(MainActivity.this, app.firstActivity));
                finish();
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(MainActivity.this, toolbar);

        permissionManager = new PermissionManager(MainActivity.this);

        View v1 = findViewById(R.id.tvAppName), v2 = findViewById(R.id.tvAppName2);
        HorizontalMoving hm = HorizontalMoving.createHorizontalMoving(MainActivity.this, v1, v2)[0];


        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        db = AppDatabase.getInstance(MainActivity.this);


        ((Button)findViewById(R.id.btnManagerActivity)).setVisibility(View.GONE);

        if (sp.contains("id")) {
            ((LinearLayout)findViewById(R.id.llGuestButtons)).setVisibility(View.GONE);

            if (sp.getBoolean("admin", false)) {
                ((TextView)findViewById(R.id.tvTitle)).setText("שלום [המנהל] " + sp.getString("name", "_nameNotFound"));
                ((Button)findViewById(R.id.btnManagerActivity)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.btnManagerActivity)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                         finish();
                    }
                });
            }
            else {
                ((TextView)findViewById(R.id.tvTitle)).setText("שלום " + sp.getString("name", "_nameNotFound"));
            }

        }

        findViewById(R.id.btnAboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createAboutDialog(MainActivity.this);
            }
        });

        findViewById(R.id.btnContactsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                finish();
            }
        });

        findViewById(R.id.btnSettingsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                finish();
            }
        });

        findViewById(R.id.btnGalleryActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                finish();
            }
        });

        setTitle("רועי סלולר");
    }

}