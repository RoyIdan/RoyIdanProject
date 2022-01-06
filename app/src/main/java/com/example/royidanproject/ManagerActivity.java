package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;

import java.util.LinkedList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private Button btnProducts, btnManufacturers, btnAddNewProduct, btnEditExistingProducts;
    private LinearLayout llProducts, llAddNewProduct, ll_spiCategory, ll_spiManufacturer, llAddNewProductCommon, llAddNewProductSmartphone;
    private EditText etName, etPrice, etStock, etScreenSize, etStorageSize, etRamSize;
    private RadioGroup rgSmartphoneColor;
    private Spinner spiGoTo, spiCategory, spiManufacturer;
    private AppDatabase db;

    private void assignPointers() {
        btnProducts = findViewById(R.id.btnProducts);
        btnManufacturers = findViewById(R.id.btnManufacturers);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        btnEditExistingProducts = findViewById(R.id.btnEditExistingProducts);
        llAddNewProduct = findViewById(R.id.llAddNewProduct);
        llProducts = findViewById(R.id.llProducts);
        ll_spiCategory = findViewById(R.id.ll_spiCategory);
        ll_spiManufacturer = findViewById(R.id.ll_spiManufacturer);
        llAddNewProductCommon = findViewById(R.id.llAddNewProductCommon);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        etScreenSize = findViewById(R.id.etScreenSize);
        etStorageSize = findViewById(R.id.etStorageSize);
        etRamSize = findViewById(R.id.etRamSize);
        rgSmartphoneColor = findViewById(R.id.rgSmartphoneColor);
        spiGoTo = findViewById(R.id.spiGoTo);
        spiCategory = findViewById(R.id.spiCategory);
        spiManufacturer = findViewById(R.id.spiManufacturer);
        llAddNewProductSmartphone = findViewById(R.id.llAddNewProductSmartphone);

    }

    private void createSpinnerData() {
        List<Manufacturer> manufacturersList = db.manufacturersDao().getAll();
        ArrayAdapter<Manufacturer> manufacturersAdapter = new ArrayAdapter<Manufacturer>(this, android.R.layout.simple_spinner_item, manufacturersList);
        spiManufacturer.setAdapter(manufacturersAdapter);

        String[] categoriesList = { "בחר קטגוריה", "סמארטפונים","שעונים","אביזרים"};
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesList);
        spiCategory.setAdapter(categoriesAdapter);

        spiCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetCategoriesFields();
                switch (i) {
                    case 1:
                        llAddNewProductSmartphone.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        // Watches
                        break;
                    case 3:
                        // Accessories
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] goToList = { "עבור אל" , "דף ראשי" , "דף משתמשים" , "***" };
        ArrayAdapter<String> goToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goToList);
        spiGoTo.setAdapter(goToAdapter);
        spiGoTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Class<? extends AppCompatActivity> destination = null;
                if (i == 1) {
                    destination = MainActivity.class;
                } else if (i == 2) {
                    destination = UsersActivity.class;
                }

                if (destination != null) {
                    startActivity(new Intent(ManagerActivity.this, destination));
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        db = AppDatabase.getInstance(ManagerActivity.this);

        assignPointers();

        createSpinnerData();

        llProducts.setVisibility(View.GONE);
        llAddNewProduct.setVisibility(View.GONE);
        llAddNewProductSmartphone.setVisibility(View.GONE);

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llProducts.getVisibility() == View.GONE) {
                    llProducts.setVisibility(View.VISIBLE);
                }
                else {
                    llProducts.setVisibility(View.GONE);
                }
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llProducts.getVisibility() == View.GONE) {
                    llProducts.setVisibility(View.VISIBLE);
                }
                else {
                    llProducts.setVisibility(View.GONE);
                }
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llAddNewProduct.getVisibility() == View.GONE) {
                    llAddNewProduct.setVisibility(View.VISIBLE);
                }
                else {
                    llAddNewProduct.setVisibility(View.GONE);
                    resetCreateNewProductFields();
                }
            }
        });



    }



    private void resetCreateNewProductFields() {
        resetCategoriesFields();
    }

    private void resetCategoriesFields() {
        //TODO - reset both fields and set the categories as GONE
    }
}