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
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;

import java.util.LinkedList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private Button btnProducts, btnManufacturers, btnAddNewProduct, btnEditExistingProducts, btnReset, btnAdd;
    private LinearLayout llProducts, llAddNewProduct, ll_spiCategory, ll_spiManufacturer, llAddNewProductCommon,
            llAddNewProductSmartphone, llFinalButtons;
    private EditText etName, etPrice, etStock, etScreenSize, etStorageSize, etRamSize;
    private RadioGroup rgSmartphoneColor;
    private Spinner spiGoTo, spiCategory, spiManufacturer;
    private int previousId = 0;
    private AppDatabase db;

    private void assignPointers() {
        btnProducts = findViewById(R.id.btnProducts);
        btnManufacturers = findViewById(R.id.btnManufacturers);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        btnEditExistingProducts = findViewById(R.id.btnEditExistingProducts);
        btnReset = findViewById(R.id.btnReset);
        btnAdd = findViewById(R.id.btnAdd);
        llAddNewProduct = findViewById(R.id.llAddNewProduct);
        llProducts = findViewById(R.id.llProducts);
        ll_spiCategory = findViewById(R.id.ll_spiCategory);
        ll_spiManufacturer = findViewById(R.id.ll_spiManufacturer);
        llAddNewProductCommon = findViewById(R.id.llAddNewProductCommon);
        llFinalButtons = findViewById(R.id.llFinalButtons);
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
                resetCategoriesFields(previousId);
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

                if (i > 0) {
                    llFinalButtons.setVisibility(View.VISIBLE);
                }
                else {
                    llFinalButtons.setVisibility(View.GONE);
                }

                previousId = i;
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

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean valid = validate();
                boolean valid = true;
                if (valid) {
                    submit();
                }


            }
        });


    }

    private void submit() {
        int type = spiCategory.getSelectedItemPosition();

        switch (type) {
            case 1:
                submit_smartphone();
                break;
            case 2:
                submit_watch();
                break;
            case 3:
                submit_accessory();
                break;
        }

        toast("Saved!");


    }

    private void submit_smartphone() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String screenSize = etScreenSize.getText().toString().trim();
        String storageSize = etStorageSize.getText().toString().trim();
        String ramSize = etRamSize.getText().toString().trim();
        long manufacturerId = spiManufacturer.getSelectedItemPosition();

//        Smartphone.PhoneColor phoneColor = getPhoneColor();
        Smartphone.PhoneColor phoneColor = Smartphone.PhoneColor.Black;

        Smartphone s = new Smartphone();
        s.setProductName(name);
        s.setManufacturerId(manufacturerId);
        s.setProductPrice(Double.parseDouble(price));
        s.setProductStock(Integer.parseInt(stock));
        s.setPhoneColor(phoneColor);
        s.setPhoneScreenSize(Integer.parseInt(screenSize));
        s.setPhoneStorageSize(Integer.parseInt(storageSize));
        s.setPhoneRamSize(Integer.parseInt(ramSize));

        db.smartphonesDao().insert(s);

        resetFields(1);
    }

    private void submit_watch() {

    }

    private void submit_accessory() {

    }

    private Smartphone.PhoneColor getPhoneColor() {
        int color = rgSmartphoneColor.getCheckedRadioButtonId();
        switch (color) {
            case R.id.radBlack:
                return Smartphone.PhoneColor.Black;
        }
        return null;
    }

    private void resetFields() {
        resetFields(spiCategory.getSelectedItemPosition());
    }

    private void resetFields(int i) {
        etName.setText("");
        etPrice.setText("");
        etStock.setText("");

        if (i == 1) {
            // Smartphone
            etScreenSize.setText("");
            etStorageSize.setText("");
            etRamSize.setText("");
        } else if (i == 2) {

        } else if (i == 3) {

        }
    }

    private void resetCreateNewProductFields() {
        resetCategoriesFields(4);

        spiCategory.setSelection(0);
        spiManufacturer.setSelection(0);

        etName.setText("");
        etPrice.setText("");
        etStock.setText("");

        llAddNewProduct.setVisibility(View.GONE);
    }

    private void resetCategoriesFields(int i) {
        //TODO - reset both fields and set the categories as GONE
        // i values: 1 - smartphone, 2 - watch, 3 - accessory, 4 - findSelected

        resetFields(i);

        // Smartphone
        if (i == 1) {
            llAddNewProductSmartphone.setVisibility(View.GONE);
        } else if (i == 2) {

        } else if (i == 3) {

        } else if (i == 4) {
            resetCategoriesFields(spiCategory.getSelectedItemPosition());
        }

    }

    private void toast(String msg) {
        Toast.makeText(ManagerActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}