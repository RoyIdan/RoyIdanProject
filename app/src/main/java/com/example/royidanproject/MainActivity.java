package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Smartphone.PhoneManufacturer;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.Dialogs;

public class MainActivity extends AppCompatActivity {

    public static final String FOLDER_NAME = "photosEmulatorDirectory";
    public static final String SP_NAME = "USER_INFO";
    public static final String ADMIN_PHONE = "0509254011";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        db = AppDatabase.getInstance(MainActivity.this);

        startActivity(new Intent(MainActivity.this, GalleryActivity.class));

        Smartphone smartphone = new Smartphone();
        smartphone.setProductName("galaxy s21");
        smartphone.setProductPrice(2);
        smartphone.setProductStock(2);
        smartphone.setPhoneManufacturer(PhoneManufacturer.Samsung);
        smartphone.setPhoneStorageSize(128);

        db.smartphonesDao().insert(smartphone);

        Smartphone sp1 = db.smartphonesDao().getSmartphoneById(1);

        if (sp.contains("id")) {
            ((LinearLayout)findViewById(R.id.llGuestButtons)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.ivUser)).setImageBitmap(CommonMethods.getImage(sp.getString("image", "")));

            if (sp.getBoolean("admin", false)) {
                ((TextView)findViewById(R.id.tvTitle)).setText("שלום [המנהל] " + sp.getString("name", "_nameNotFound"));
                ((Button)findViewById(R.id.btnUsersActivity)).setText("Users screen");
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
        i.putExtra("id",2l);
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