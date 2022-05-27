package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.example.royidanproject.Adapters.ContactsAdapter;
import com.example.royidanproject.Custom.Contact;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;
import com.example.royidanproject.Views.CreditCardView;
import com.example.royidanproject.Views.WifiView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.READ_CONTACTS;
import static com.example.royidanproject.MainActivity.SP_NAME;

public class TestingActivity extends AppCompatActivity {

    AppDatabase db;
    SharedPreferences sp;


    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;
    Toolbar toolbar;
    private ImageView ivImage;
    private ImageButton ibCamera, ibGallery;
    private Bitmap bmProduct;
    private Button btnAdd;
    private TextView tvProductName;

    private List<Watch> products;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        db = AppDatabase.getInstance(this);
        sp = getSharedPreferences(SP_NAME, 0);

        llRoot = findViewById(R.id.llRoot);

        toolbar = findViewById(R.id.toolbar);

        ToolbarManager toolbarManager = new ToolbarManager(TestingActivity.this, toolbar);

        //addALotOfProducts();

        ivImage = findViewById(R.id.ivImage);
        ibCamera = findViewById(R.id.ibCamera);
        ibGallery = findViewById(R.id.ibGallery);
        btnAdd = findViewById(R.id.btnAdd);
        tvProductName = findViewById(R.id.tvProductName);

        products = db.watchesDao().getAll();
        i = 0;

        List<Watch> l1 = new LinkedList<>();
        for (int j = 0; j < products.size(); j++) {
            Watch s = products.get(j);
            if (s.getProductPhoto() == null) {
                l1.add(s);
            }
        }
        products.clear();
        products.addAll(l1);
        l1 = null;

        tvProductName.setText(products.get(i).getProductName());

        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        ibGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bmProduct == null) {
                    return;
                }
                Watch p = products.get(i);
                String photo = ProductImages.savePhoto(bmProduct, TestingActivity.this);
                p.setProductPhoto(photo);
                db.watchesDao().update(p);

                bmProduct = null;

                i++;
                tvProductName.setText(products.get(i).getProductName());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                bmProduct = (Bitmap) data.getExtras().get("data");
                ivImage.setImageBitmap(bmProduct);
            }
        }

        else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bmProduct = selectedImage;
                    ivImage.setImageBitmap(bmProduct);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void addALotOfProducts() {
//        Manufacturer xiaomi = new Manufacturer();
//        xiaomi.setManufacturerName("Xiaomi");
//        Manufacturer lg = new Manufacturer();
//        lg.setManufacturerName("LG");
//        Manufacturer google = new Manufacturer();
//        google.setManufacturerName("Google");
//
//        db.manufacturersDao().insert(xiaomi);
//        db.manufacturersDao().insert(lg);
//        db.manufacturersDao().insert(google);
//
//        String[] xiaomis = new String[] {
//                "10", "10 Pro", "11", "11T Pro", "11T Pro plus"
//        };
//
//        String[] lgs = new String[] {
//                "5", "6", "7", "8"
//        };
//
//        String[] googles = new String[] {
//                "3", "4", "5", "6", "6 Pro"
//        };
//
//        List<Watch> Watchs = new LinkedList<>();
//
//        int p = 800;
//        for (String x : xiaomis) {
//            Watch sp = new Watch();
//            sp.setManufacturerId(3);
//            sp.setProductName("Redmi Note " + x);
//            sp.setProductPrice(p += 400);
//            sp.setProductStock(new Random().nextInt(15));
//            int colorIndex = new Random().nextInt(3);
//            sp.setPhoneColor(Watch.PhoneColor.values()[colorIndex]);
//            float screenSize = new Random().nextInt(31);
//            screenSize /= 10;
//            screenSize += 5;
//            sp.setPhoneScreenSize(screenSize);
//            int storageSizeExpo = new Random().nextInt(6) + 3;
//            int storageSize = (int) Math.pow(2, storageSizeExpo);
//            sp.setPhoneStorageSize(storageSize);
//            int[] rams = {1, 2, 3, 4, 6, 8, 12 ,16, 18};
//            int ram = rams[new Random().nextInt(9)];
//            sp.setPhoneRamSize(ram);
//            Watchs.add(sp);
//        }
//
//        p = 1100;
//        for (String l : lgs) {
//            Watch sp = new Watch();
//            sp.setManufacturerId(4);
//            sp.setProductName("LG G " + l);
//            sp.setProductPrice(p += 350);
//            sp.setProductStock(new Random().nextInt(15));
//            int colorIndex = new Random().nextInt(3);
//            sp.setPhoneColor(Watch.PhoneColor.values()[colorIndex]);
//            float screenSize = new Random().nextInt(31);
//            screenSize /= 10;
//            screenSize += 5;
//            sp.setPhoneScreenSize(screenSize);
//            int storageSizeExpo = new Random().nextInt(6) + 3;
//            int storageSize = (int) Math.pow(2, storageSizeExpo);
//            sp.setPhoneStorageSize(storageSize);
//            int[] rams = {1, 2, 3, 4, 6, 8, 12 ,16, 18};
//            int ram = rams[new Random().nextInt(9)];
//            sp.setPhoneRamSize(ram);
//            Watchs.add(sp);
//        }
//
//        p = 700;
//        for (String g : googles) {
//            Watch sp = new Watch();
//            sp.setManufacturerId(5);
//            sp.setProductName("Pixel " + g);
//            sp.setProductPrice(p += 450);
//            sp.setProductStock(new Random().nextInt(15));
//            int colorIndex = new Random().nextInt(3);
//            sp.setPhoneColor(Watch.PhoneColor.values()[colorIndex]);
//            float screenSize = new Random().nextInt(31);
//            screenSize /= 10;
//            screenSize += 5;
//            sp.setPhoneScreenSize(screenSize);
//            int storageSizeExpo = new Random().nextInt(6) + 3;
//            int storageSize = (int) Math.pow(2, storageSizeExpo);
//            sp.setPhoneStorageSize(storageSize);
//            int[] rams = {1, 2, 3, 4, 6, 8, 12 ,16, 18};
//            int ram = rams[new Random().nextInt(9)];
//            sp.setPhoneRamSize(ram);
//            Watchs.add(sp);
//        }
//
//        db.WatchsDao().insert(Watchs);
//
//    }

}