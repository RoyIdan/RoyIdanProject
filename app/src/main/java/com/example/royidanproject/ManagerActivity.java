package com.example.royidanproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.UserImages;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class ManagerActivity extends AppCompatActivity {

    private Button btnProducts, btnManufacturers, btnAddNewProduct, btnEditExistingProducts, btnReset, btnAdd,
            btnAddNewManufacturer, btnEditExistingManufacturers, btnOpenDescriptionDialog;
    private ImageButton ibCamera, ibGallery;
    private ImageView ivPhoto;
    private LinearLayout llProducts, llAddNewProduct, ll_spiCategory, ll_spiManufacturer, llAddNewProductCommon,
            llAddNewProductSmartphone, llFinalButtons, llAddNewManufacturer;
    private EditText etName, etPrice, etStock;
    private RadioGroup rgSmartphoneColor;
    private Spinner spiGoTo, spiCategory, spiManufacturer, spiScreenSize, spiStorageSize, spiRamSize;
    private Bitmap bmProduct;
    private String description;
    private int previousId = 0;
    private AppDatabase db;

    private void assignPointers() {
        btnProducts = findViewById(R.id.btnProducts);
        btnManufacturers = findViewById(R.id.btnManufacturers);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        btnEditExistingProducts = findViewById(R.id.btnEditExistingProducts);
        btnReset = findViewById(R.id.btnReset);
        btnAdd = findViewById(R.id.btnAdd);
        btnAddNewManufacturer = findViewById(R.id.btnAddNewManufacturer);
        btnEditExistingManufacturers = findViewById(R.id.btnEditExistingManufacturers);
        btnOpenDescriptionDialog = findViewById(R.id.btnOpenDescriptionDialog);
        ibCamera = findViewById(R.id.ibCamera);
        ibGallery = findViewById(R.id.ibGallery);
        ivPhoto = findViewById(R.id.ivPhoto);
        llAddNewProduct = findViewById(R.id.llAddNewProduct);
        llProducts = findViewById(R.id.llProducts);
        ll_spiCategory = findViewById(R.id.ll_spiCategory);
        ll_spiManufacturer = findViewById(R.id.ll_spiManufacturer);
        llAddNewProductCommon = findViewById(R.id.llAddNewProductCommon);
        llFinalButtons = findViewById(R.id.llFinalButtons);
        llAddNewManufacturer = findViewById(R.id.llAddNewManufacturer);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        spiScreenSize = findViewById(R.id.spiScreenSize);
        spiStorageSize = findViewById(R.id.spiStorageSize);
        spiRamSize = findViewById(R.id.spiRamSize);
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
                    if (llAddNewProduct.getVisibility() == View.VISIBLE) {
                        resetCreateNewProductFields();
                    }
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


        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission(ManagerActivity.this)) {
                    getPermission(ManagerActivity.this);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
            }
        });

        ibGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission(ManagerActivity.this)) {
                    getPermission(ManagerActivity.this);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            }
        });

        btnOpenDescriptionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog descriptionDialog = Dialogs.createGetDescriptionDialog(ManagerActivity.this);
                String productName = etName.getText().toString().trim();
                ((TextView) descriptionDialog.findViewById(R.id.tvTitle)).setText(productName.isEmpty() ? "לא נבחר מוצר" : productName);
                if (!description.isEmpty()) {
                    ((EditText)descriptionDialog.findViewById(R.id.etTextBox)).setText(description);
                }
                descriptionDialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description = ((EditText)descriptionDialog.findViewById(R.id.etTextBox)).getText().toString().trim();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                bmProduct = (Bitmap) data.getExtras().get("data");
                ivPhoto.setImageBitmap(bmProduct);
            }
        }

        else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bmProduct = selectedImage;
                    ivPhoto.setImageBitmap(bmProduct);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, 1);
    }

    private boolean checkPermission(Context context) {
        int cam = ContextCompat.checkSelfPermission(context, CAMERA);
        int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

        return cam == PERMISSION_GRANTED &&
                write == PERMISSION_GRANTED &&
                read == PERMISSION_GRANTED;
    }

    private void submit() {
        int type = spiCategory.getSelectedItemPosition();

        if (!validate(type)) {
            return;
        }

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

    private boolean validate(int type) {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String strPrice = etPrice.getText().toString().trim();
        String strStock = etStock.getText().toString().trim();
        double price = 0;
        int stock = 0;

        if (name.isEmpty()) {
            etName.setError("אנא מלא שדה זה");
            isValid = false;
        } else {
            if (name.length() < 3) {
                etName.setError("שם המוצר חייב להכיל לפחות 3 תווים");
                isValid = false;
            }
        }

        if (strPrice.isEmpty()) {
            etPrice.setError("אנא מלא שדה זה");
            isValid = false;
        } else {
            price = Double.parseDouble(strPrice);
            if (price < 10) {
                etPrice.setError("המחיר המינימלי של מוצר הוא 10 שקלים");
                isValid = false;
            }
        }

        if (strStock.isEmpty()) {
            etStock.setError("אנא מלא שדה זה");
            isValid = false;
        } else {
            stock = Integer.parseInt(strStock);
            if (stock < 1) {
                etStock.setError("חייב להיות לפחות מוצר אחד במלאי");
                isValid = false;
            }
        }

        if (bmProduct == null) {
            toast("בחר תמונה");
            isValid = false;
        }

        return isValid;
    }

    private void submit_smartphone() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String screenSize = (String)spiScreenSize.getSelectedItem();
        String storageSize = (String)spiStorageSize.getSelectedItem();
        String ramSize = (String)spiRamSize.getSelectedItem();
        long manufacturerId = spiManufacturer.getSelectedItemPosition() + 1;

        String photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
        if (photo == null) {
            toast("Failed to save the photo");
            return;
        }

        Smartphone.PhoneColor phoneColor = getPhoneColor();

        Smartphone s = new Smartphone();
        s.setProductName(name);
        s.setManufacturerId(manufacturerId);
        s.setProductPrice(Double.parseDouble(price));
        s.setProductStock(Integer.parseInt(stock));
        s.setProductDescription(description);
        s.setPhoneColor(phoneColor);
        s.setPhoneScreenSize(Float.parseFloat(screenSize));
        s.setPhoneStorageSize(Integer.parseInt(storageSize));
        s.setPhoneRamSize(Integer.parseInt(ramSize));
        s.setProductPhoto(photo);

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
                return Smartphone.PhoneColor.שחור;
            case R.id.radGray:
                return Smartphone.PhoneColor.אפור;
            default:
                return Smartphone.PhoneColor.לבן;
        }
    }

    private void resetFields() {
        resetFields(spiCategory.getSelectedItemPosition());
    }

    private void resetFields(int i) {
        etName.setText("");
        etPrice.setText("");
        etStock.setText("");
        description = "";

        if (i == 1) {
            // Smartphone
            spiScreenSize.setSelection(0);
            spiStorageSize.setSelection(0);
            spiRamSize.setSelection(0);
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
        //TODO - reset both fields and set the categories as GONE\

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