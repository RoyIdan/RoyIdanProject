package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Application.RoyIdanProject;
import com.example.royidanproject.BroadcastReceivers.BatteryReceiver;
import com.example.royidanproject.BroadcastReceivers.WifiStatusReceiver;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.Services.MusicService;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.HorizontalMoving;
import com.example.royidanproject.Utility.PermissionManager;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;
import com.example.royidanproject.Utility.Dialogs;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    public static final String USERS_FOLDER_NAME = "royIdanProject_Users";
    public static final String PRODUCTS_FOLDER_NAME = "royIdanProject_Products";
    public static final String SP_NAME = "USER_INFO";
    public static final String ADMIN_PHONE = "0509254011";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppDatabase db;

    private BatteryReceiver batteryReceiver;
    private IntentFilter intentFilter;

    private WifiStatusReceiver wifiStatusReceiver;
    private IntentFilter intentFilter2;

    private PermissionManager permissionManager;
    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

//        batteryReceiver = new BatteryReceiver(findViewById(R.id.tvTitle));
//        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(batteryReceiver, intentFilter);

        WifiStatusReceiver wsr = new WifiStatusReceiver(findViewById(R.id.tvTitle));
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wsr, filter);

        //toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregisterReceiver(batteryReceiver);
        unregisterReceiver(wifiStatusReceiver);
        //toolbarManager.onDestroy();
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


//        addSampleProducts();
//        if (1 > 0) return;

        //startActivity(new Intent(MainActivity.this, GalleryActivity.class));

        ((Button)findViewById(R.id.btnManagerActivity)).setVisibility(View.GONE);

        if (sp.contains("id")) {
            ((LinearLayout)findViewById(R.id.llGuestButtons)).setVisibility(View.GONE);
            //((ImageView)findViewById(R.id.ivUser)).setImageURI(UserImages.getImage(sp.getString("image", ""), MainActivity.this));

            if (sp.getBoolean("admin", false)) {
                ((TextView)findViewById(R.id.tvTitle)).setText("שלום [המנהל] " + sp.getString("name", "_nameNotFound"));
                ((Button)findViewById(R.id.btnUsersActivity)).setText("מסך משתמשים");
                ((Button)findViewById(R.id.btnManagerActivity)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.btnManagerActivity)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                    }
                });
            }
            else {
                ((TextView)findViewById(R.id.tvTitle)).setText("שלום " + sp.getString("name", "_nameNotFound"));
            }

        }
        else {
            ((LinearLayout)findViewById(R.id.llUserButtons)).setVisibility(View.GONE);
            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialogs.createLoginDialog(MainActivity.this);
                }
            });
        }

        findViewById(R.id.btnAboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createAboutDialog(MainActivity.this);
            }
        });

        findViewById(R.id.btnGalleryActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

        findViewById(R.id.btnUsersActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UsersActivity.class));
            }
        });
        findViewById(R.id.btnLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().commit();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        findViewById(R.id.btnRegisterActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        findViewById(R.id.btnCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.cartDetailsDao().hasAny(sp.getLong("id", 0)) != null) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "הסל הזמני שלך ריק", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btnCreditCards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreditCardsActivity.class));
            }
        });
        findViewById(R.id.btnOrderHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.ordersDao().hasAny(sp.getLong("id", 0)) != null || sp.getBoolean("admin", false)) {
                    startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "היסטורית ההזמנות שלך ריקה", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTitle("רועי סלולר");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem miLogin = menu.findItem(R.id.menu_login);
        MenuItem miLogout = menu.findItem(R.id.menu_logout);
        MenuItem miRegister = menu.findItem(R.id.menu_register);

        if (sp.contains("name")) {
            miLogout.setVisible(true);
            miLogin.setVisible(false);
            miRegister.setVisible(false);
        } else {
            miLogout.setVisible(false);
            miLogin.setVisible(true);
            miRegister.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.menu_login:
                Dialogs.createLoginDialog(MainActivity.this);
                break;
            case R.id.menu_logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            default:
                int a = 1/0;
        }

        return true;
    }

    private void updateSample() {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        i.putExtra("id", 2L);
        i.putExtra("email","s3234@nhs.co.il");
        i.putExtra("phone","0569254011");
        i.putExtra("name","Roy");
        i.putExtra("surname","Idan");
        i.putExtra("gender","male");
        i.putExtra("birthdate","10/08/2004");
        i.putExtra("address","zzz");
        i.putExtra("city","Ashdod");
        i.putExtra("password","123");
        i.putExtra("photo", "211107-13:25:10.jpg");

        startActivity(i);
    }
}