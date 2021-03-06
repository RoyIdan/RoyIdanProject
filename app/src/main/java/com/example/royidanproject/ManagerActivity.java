package com.example.royidanproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.ManufacturerAdapter;
import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.ToolbarManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class ManagerActivity extends AppCompatActivity {

    private Button btnProducts, btnManufacturers, btnMainActivity, btnAddNewProduct, btnEditExistingProducts, btnReset, btnAdd,
            btnAddNewManufacturer, btnEditExistingManufacturers, btnOpenDescriptionDialog, btnAddManufacturerSubmit,
            btnUpdate_mainActivity, btnUpdate;
    private ImageButton ibCamera, ibGallery;
    private ImageView ivPhoto1;
    private LinearLayout llButtonsLayout, llProducts, llAddNewProduct, llAddNewProductButtons, ll_spiCategory, ll_spiManufacturer, llAddNewProductCommon,
            llAddNewProductSmartphone, llAddNewProductWatch, llFinalButtons, llUpdate_buttons, llManufacturers, llAddNewManufacturer, llEditExistingManufacturers;
    private EditText etName, etPrice, etStock, etWatchSize, etManufacturerName, etSearchManufacturerByName;
    private RadioGroup rgSmartphoneColor, rgWatchColor;
    private Spinner /*spiGoTo,*/ spiCategory, spiManufacturer, spiScreenSize, spiStorageSize, spiRamSize;
    private ListView lvManufacturers;
    private Bitmap bmProduct;
    private String description;
    private int previousId = 0;
    private AppDatabase db;
    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

        toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        toolbarManager.onDestroy();
    }

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
        btnAddManufacturerSubmit = findViewById(R.id.btnAddManufacturerSubmit);
        btnUpdate_mainActivity = findViewById(R.id.btnUpdate_mainActivity);
        btnUpdate = findViewById(R.id.btnUpdate);
        ibCamera = findViewById(R.id.ibCamera);
        ibGallery = findViewById(R.id.ibGallery);
        ivPhoto1 = findViewById(R.id.ivPhoto1);
        llButtonsLayout = findViewById(R.id.llButtonsLayout);
        llAddNewProduct = findViewById(R.id.llAddNewProduct);
        llAddNewProductButtons = findViewById(R.id.llAddNewProductButtons);
        llProducts = findViewById(R.id.llProducts);
        ll_spiCategory = findViewById(R.id.ll_spiCategory);
        ll_spiManufacturer = findViewById(R.id.ll_spiManufacturer);
        llAddNewProductCommon = findViewById(R.id.llAddNewProductCommon);
        llFinalButtons = findViewById(R.id.llFinalButtons);
        llUpdate_buttons = findViewById(R.id.llUpdate_buttons);
        llManufacturers = findViewById(R.id.llManufacturers);
        llAddNewManufacturer = findViewById(R.id.llAddNewManufacturer);
        llEditExistingManufacturers = findViewById(R.id.llEditExistingManufacturers);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        etWatchSize = findViewById(R.id.etWatchSize);
        etManufacturerName = findViewById(R.id.etManufacturerName);
        etSearchManufacturerByName = findViewById(R.id.etSearchManufacturerByName);
        spiScreenSize = findViewById(R.id.spiScreenSize);
        spiStorageSize = findViewById(R.id.spiStorageSize);
        spiRamSize = findViewById(R.id.spiRamSize);
        rgSmartphoneColor = findViewById(R.id.rgSmartphoneColor);
        rgWatchColor = findViewById(R.id.rgWatchColor);
//        spiGoTo = findViewById(R.id.spiGoTo);
        btnMainActivity = findViewById(R.id.btnMainActivity);

        spiCategory = findViewById(R.id.spiCategory);
        spiManufacturer = findViewById(R.id.spiManufacturer);
        llAddNewProductSmartphone = findViewById(R.id.llAddNewProductSmartphone);
        llAddNewProductWatch = findViewById(R.id.llAddNewProductWatch);
        lvManufacturers = findViewById(R.id.lvManufacturers);
    }

    private void createSpinnerData() {
        List<Manufacturer> manufacturersList = db.manufacturersDao().getAll();
        ArrayAdapter<Manufacturer> manufacturersAdapter = new ArrayAdapter<Manufacturer>(this, android.R.layout.simple_spinner_item, manufacturersList);
        spiManufacturer.setAdapter(manufacturersAdapter);

        String[] categoriesList = { "?????? ??????????????", "????????????????????","????????????","??????????????"};
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
                        llAddNewProductWatch.setVisibility(View.VISIBLE);
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

//        String[] goToList = { "???????? ????" , "???? ????????" , "???? ??????????????" , "***" };
//        ArrayAdapter<String> goToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goToList);
//        spiGoTo.setAdapter(goToAdapter);
//        spiGoTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Class<? extends AppCompatActivity> destination = null;
//                if (i == 1) {
//                    destination = MainActivity.class;
//                } else if (i == 2) {
//                    destination = UsersActivity.class;
//                }
//
//                if (destination != null) {
//                    startActivity(new Intent(ManagerActivity.this, destination));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        db = AppDatabase.getInstance(ManagerActivity.this);

        assignPointers();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(ManagerActivity.this, toolbar);

        createSpinnerData();

        llProducts.setVisibility(View.GONE);
        llAddNewProduct.setVisibility(View.GONE);
        llAddNewProductSmartphone.setVisibility(View.GONE);


        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, MainActivity.class));
                finish();
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llProducts.getVisibility() == View.GONE) {
                    if (llManufacturers.getVisibility() == View.VISIBLE) {
                        llManufacturers.setVisibility(View.GONE);
                        if (llAddNewManufacturer.getVisibility() == View.VISIBLE) {
                            etManufacturerName.setText("");
                            llAddNewManufacturer.setVisibility(View.GONE);
                        } else if (llEditExistingManufacturers.getVisibility() == View.VISIBLE) {
                            etSearchManufacturerByName.setText("");
                            llEditExistingManufacturers.setVisibility(View.GONE);
                        }
                    }
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

        btnManufacturers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llManufacturers.getVisibility() == View.GONE) {
                    if (llProducts.getVisibility() == View.VISIBLE) {
                        llProducts.setVisibility(View.GONE);
                        if (llAddNewProduct.getVisibility() == View.VISIBLE) {
                            resetCreateNewProductFields();
                        }
                    }
                    llManufacturers.setVisibility(View.VISIBLE);
                } else {
                    llManufacturers.setVisibility(View.GONE);
                    if (llAddNewManufacturer.getVisibility() == View.VISIBLE) {
                        etManufacturerName.setText("");
                        llAddNewManufacturer.setVisibility(View.GONE);
                    } else if (llEditExistingManufacturers.getVisibility() == View.VISIBLE) {
                        etSearchManufacturerByName.setText("");
                        llEditExistingManufacturers.setVisibility(View.GONE);
                    }
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

        btnEditExistingProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("???????????? ???? ???????? ??????\n???????? ?????????? ???????????? ?????? ??????????????");
            }
        });

        btnAddNewManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llAddNewManufacturer.getVisibility() == View.GONE) {
                    llAddNewManufacturer.setVisibility(View.VISIBLE);
                }
                else {
                    llAddNewManufacturer.setVisibility(View.GONE);
                    etManufacturerName.setText("");
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
                ((TextView) descriptionDialog.findViewById(R.id.tvTitle)).setText(productName.isEmpty() ? "???? ???????? ????????" : productName);
                if (!description.isEmpty()) {
                    ((EditText)descriptionDialog.findViewById(R.id.etTextBox)).setText(description);
                }
                descriptionDialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String description = ((EditText)descriptionDialog.findViewById(R.id.etTextBox)).getText().toString().trim();
                        if (description.split("\r\n|\r|\n").length > 3) {
                            toast("?????????? ?????????? ???? ???????? ?????????? ???????? ??3 ??????????");
                            return;
                        }
                        ManagerActivity.this.description = description;
                        toast("?????????? ?????????? ???????? ????????????");
                        descriptionDialog.dismiss();
                    }
                });
            }
        });


        // Manufacturer layout

        btnAddNewManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llAddNewManufacturer.getVisibility() == View.GONE) {
                    if (llEditExistingManufacturers.getVisibility() == View.VISIBLE) {
                        llEditExistingManufacturers.setVisibility(View.GONE);
                        etSearchManufacturerByName.setText("");
                    }
                    llAddNewManufacturer.setVisibility(View.VISIBLE);
                }
                else {
                    etManufacturerName.setText("");
                    llAddNewManufacturer.setVisibility(View.GONE);
                }
            }
        });

        btnAddManufacturerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manufacturerName = etManufacturerName.getText().toString().trim();
                if (db.manufacturersDao().hasName_caseInsensitive(manufacturerName)) {
                    etManufacturerName.setError("???? ?????????? ?????? ???????? ????????????");
                    return;
                }
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setManufacturerName(manufacturerName);

                db.manufacturersDao().insert(manufacturer);
                toast("?????????? " + manufacturerName + " ???????? ????????????");
                List<Manufacturer> manufacturersList = db.manufacturersDao().getAll();
                ArrayAdapter<Manufacturer> manufacturersAdapter = new ArrayAdapter<Manufacturer>(
                        ManagerActivity.this, android.R.layout.simple_spinner_item, manufacturersList);
                spiManufacturer.setAdapter(manufacturersAdapter);

                etManufacturerName.setText("");
            }
        });

        btnEditExistingManufacturers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llEditExistingManufacturers.getVisibility() == View.GONE) {
                    if (llAddNewManufacturer.getVisibility() == View.VISIBLE) {
                        llAddNewManufacturer.setVisibility(View.GONE);
                        etManufacturerName.setText("");
                    }
                    llEditExistingManufacturers.setVisibility(View.VISIBLE);
                }
                else {
                    etSearchManufacturerByName.setText("");
                    llEditExistingManufacturers.setVisibility(View.GONE);
                }
            }
        });

        List<Manufacturer> manufacturersList = db.manufacturersDao().getAll();
        ManufacturerAdapter adapter = new ManufacturerAdapter(ManagerActivity.this, manufacturersList);
        lvManufacturers.setAdapter(adapter);

        etSearchManufacturerByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey("productToUpdate")) {
            Product product = (Product) intent.getExtras().getSerializable("productToUpdate");
            boolean isPurchased = intent.getBooleanExtra("isProductPurchased", false);
            updateMode(product, isPurchased);
        }

    }

    public void updateManufacturerList(List<Manufacturer> manufacturersList) {
        ArrayAdapter<Manufacturer> manufacturersAdapter = new ArrayAdapter<Manufacturer>(
                ManagerActivity.this, android.R.layout.simple_spinner_item, manufacturersList);
        spiManufacturer.setAdapter(manufacturersAdapter);
    }

    private void update_submit(Product product) {
        int type = spiCategory.getSelectedItemPosition();

        if (!validate(type, false)) {
            return;
        }

        switch (type) {
            case 1:
                update_smartphone(product);
                break;
            case 2:
                update_watch(product);
                break;
            case 3:
                update_accessory(product);
                break;
        }

        toast("?????????? ?????????? ????????????");

        startActivity(new Intent(ManagerActivity.this, GalleryActivity.class));
        finish();
    }

    private void update_smartphone(Product product) {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String screenSize = (String)spiScreenSize.getSelectedItem();
        String storageSize = (String)spiStorageSize.getSelectedItem();
        String ramSize = (String)spiRamSize.getSelectedItem();
        long manufacturerId = spiManufacturer.getSelectedItemPosition() + 1;


        String photo = product.getProductPhoto();
        if (bmProduct != null) {
            photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
            if (photo == null) {
                toast("???? ???????? ?????????? ???? ????????????");
                return;
            }
        }

        Smartphone.PhoneColor phoneColor = getPhoneColor();

        Smartphone s = new Smartphone();
        s.setProductId(product.getProductId());
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

        db.smartphonesDao().update(s);

    }

    private void update_watch(Product product) {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String watchSize = etWatchSize.getText().toString().trim();
        long manufacturerId = spiManufacturer.getSelectedItemPosition() + 1;


        String photo = product.getProductPhoto();
        if (bmProduct != null) {
            photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
            if (photo == null) {
                toast("???? ???????? ?????????? ???? ????????????");
                return;
            }
        }

        Watch.WatchColor watchColor = getWatchColor();

        Watch w = new Watch();
        w.setProductId(product.getProductId());
        w.setProductName(name);
        w.setManufacturerId(manufacturerId);
        w.setProductPrice(Double.parseDouble(price));
        w.setProductStock(Integer.parseInt(stock));
        w.setProductDescription(description);
        w.setWatchColor(watchColor);
        w.setWatchSize(Integer.parseInt(watchSize));
        w.setProductPhoto(photo);

        db.watchesDao().update(w);
    }

    private void update_accessory(Product product) {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        long manufacturerId = spiManufacturer.getSelectedItemPosition() + 1;


        String photo = product.getProductPhoto();
        if (bmProduct != null) {
            photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
            if (photo == null) {
                toast("???? ???????? ?????????? ???? ????????????");
                return;
            }
        }

        Accessory a = new Accessory();
        a.setProductId(product.getProductId());
        a.setProductName(name);
        a.setManufacturerId(manufacturerId);
        a.setProductPrice(Double.parseDouble(price));
        a.setProductStock(Integer.parseInt(stock));
        a.setProductDescription(description);
        a.setProductPhoto(photo);

        db.accessoriesDao().update(a);
    }

    private void updateMode(Product product, boolean isPurchased) {
        ((TextView) findViewById(R.id.tvAddProductTitle)).setText("???? ?????????? ????????");
        llButtonsLayout.setVisibility(View.GONE);
        llProducts.setVisibility(View.VISIBLE);
        llAddNewProduct.setVisibility(View.VISIBLE);
        llAddNewProductCommon.setVisibility(View.VISIBLE);
        llAddNewProductButtons.setVisibility(View.GONE);
        llUpdate_buttons.setVisibility(View.VISIBLE);
        spiCategory.setEnabled(false);
        llAddNewProduct.removeView(llFinalButtons);
        // disable it so it won't reset the fields
        spiCategory.setOnItemSelectedListener(null);
        btnUpdate_mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this, MainActivity.class));
                finish();
            }
        });

        long tableId;
        if (product instanceof Smartphone) {
            tableId = 1;
        } else if (product instanceof Watch) {
            tableId = 2;
        } else {
            tableId = 3;
        }
        ivPhoto1.setImageURI(ProductImages.getImage(product.getProductPhoto(), ManagerActivity.this));
        spiCategory.setSelection((int) tableId);
        spiManufacturer.setSelection((int) product.getManufacturerId() - 1);
        etName.setText(product.getProductName());
        etPrice.setText(String.valueOf(product.getProductPrice()));
        etStock.setText(String.valueOf(product.getProductStock()));
        description = product.getProductDescription();

        if (tableId == 1) {
            llAddNewProductSmartphone.setVisibility(View.VISIBLE);
            Smartphone smartphone = (Smartphone) product;
            int colorIndex = smartphone.getPhoneColor().ordinal();
            ((RadioButton) rgSmartphoneColor.getChildAt(colorIndex * 2)).setChecked(true);
            String[] screenSizes = getResources().getStringArray(R.array.screen_size);
            int screenSizeIndex = Arrays.asList(screenSizes).indexOf(String.valueOf(smartphone.getPhoneScreenSize()));
            spiScreenSize.setSelection(screenSizeIndex);
            String[] storageSizes = getResources().getStringArray(R.array.storage_size);
            int storageSizeIndex = Arrays.asList(storageSizes).indexOf(String.valueOf(smartphone.getPhoneStorageSize()));
            spiStorageSize.setSelection(storageSizeIndex);
            String[] ramSizes = getResources().getStringArray(R.array.ram_size);
            int ramSizeIndex = Arrays.asList(ramSizes).indexOf(String.valueOf(smartphone.getPhoneRamSize()));
            spiRamSize.setSelection(ramSizeIndex);
        } else if (tableId == 2) {
            llAddNewProductWatch.setVisibility(View.VISIBLE);
            Watch watch = (Watch) product;
            int colorIndex = watch.getWatchColor().ordinal();
            ((RadioButton) rgWatchColor.getChildAt(colorIndex * 2)).setChecked(true);
            etWatchSize.setText(String.valueOf((watch).getWatchSize()));
        }

        if (isPurchased) {
            etName.setEnabled(false);
            spiManufacturer.setEnabled(false);
            if (tableId == 1) {
                for (int i = 0; i < rgSmartphoneColor.getChildCount(); i+= 2) {
                    rgSmartphoneColor.getChildAt(i).setEnabled(false);
                }
                spiScreenSize.setEnabled(false);
                spiStorageSize.setEnabled(false);
                spiRamSize.setEnabled(false);
            } else if (tableId == 2) {
                for (int i = 0; i < rgWatchColor.getChildCount(); i+= 2) {
                    rgWatchColor.getChildAt(i).setEnabled(false);
                }
                etWatchSize.setEnabled(false);
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_submit(product);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                bmProduct = (Bitmap) data.getExtras().get("data");
                ivPhoto1.setImageBitmap(bmProduct);
            }
        }

        else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bmProduct = selectedImage;
                    ivPhoto1.setImageBitmap(bmProduct);
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

        if (spiManufacturer.getCount() == 0) {
            toast("???? ???????????? ????????????, ???????? ???????????? ???????????? ????????????");
            return;
        }

        if (!validate(type, true)) {
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

        String productName = etName.getText().toString().trim();
        toast("?????????? " + productName + " ???????? ????????????!");

        bmProduct = null;
        ivPhoto1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.question_mark_pfp, null));

    }

    private boolean validate(int type, boolean validateBitmap) {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String strPrice = etPrice.getText().toString().trim();
        String strStock = etStock.getText().toString().trim();
        double price = 0;
        int stock = 0;

        if (name.isEmpty()) {
            etName.setError("?????? ?????? ?????? ????");
            isValid = false;
        } else {
            if (name.length() < 3) {
                etName.setError("???? ?????????? ???????? ?????????? ?????????? 3 ??????????");
                isValid = false;
            }
        }

        if (strPrice.isEmpty()) {
            etPrice.setError("?????? ?????? ?????? ????");
            isValid = false;
        } else {
            price = Double.parseDouble(strPrice);
            if (price < 10) {
                etPrice.setError("?????????? ???????????????? ???? ???????? ?????? 10 ??????????");
                isValid = false;
            } else if (price > 25000) {
                etPrice.setError("?????????? ???????????????? ???? ???????? ?????? 25,000 ??????????");
                isValid = false;
            }
        }

        if (strStock.isEmpty()) {
            etStock.setError("?????? ?????? ?????? ????");
            isValid = false;
        } else {
            stock = Integer.parseInt(strStock);
            if (stock < 1) {
                etStock.setError("???????? ?????????? ?????????? ???????? ?????? ??????????");
                isValid = false;
            } else if (stock > 50) {
                etStock.setError("???? ???????? ???????????? ???????? ??50 ???????????? ??????????");
                isValid = false;
            }
        }

        if (type == 2) {
            int watchSize = 0;
            String strWatchSize = etWatchSize.getText().toString().trim();
            if (strWatchSize.isEmpty()) {
                etWatchSize.setError("?????? ?????? ?????? ????");
                isValid = false;
            } else {
                watchSize = Integer.parseInt(strWatchSize);
                if (watchSize < 20) {
                    etWatchSize.setError("???????? ?????????? ???????? ?????????? ?????????? 20");
                    isValid = false;
                }
            }
        }

        if (validateBitmap && bmProduct == null) {
            toast("?????? ??????????");
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
        long manufacturerId = ((Manufacturer) spiManufacturer.getSelectedItem()).getManufacturerId();

        String photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
        if (photo == null) {
            toast("?????????? ?????????? ?????????????? ?????????? ???? ????????????");
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
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        String watchSize = etWatchSize.getText().toString().trim();
        long manufacturerId = ((Manufacturer) spiManufacturer.getSelectedItem()).getManufacturerId();

        String photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
        if (photo == null) {
            toast("Failed to save the photo");
            return;
        }

        Watch.WatchColor watchColor = getWatchColor();

        Watch w = new Watch();
        w.setProductName(name);
        w.setManufacturerId(manufacturerId);
        w.setProductPrice(Double.parseDouble(price));
        w.setProductStock(Integer.parseInt(stock));
        w.setProductDescription(description);
        w.setWatchColor(watchColor);
        w.setWatchSize(Integer.parseInt(watchSize));
        w.setProductPhoto(photo);

        db.watchesDao().insert(w);

        resetFields(2);
    }

    private void submit_accessory() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String stock = etStock.getText().toString().trim();
        long manufacturerId = ((Manufacturer) spiManufacturer.getSelectedItem()).getManufacturerId();

        String photo = ProductImages.savePhoto(bmProduct, ManagerActivity.this);
        if (photo == null) {
            toast("Failed to save the photo");
            return;
        }

        Accessory a = new Accessory();
        a.setProductName(name);
        a.setManufacturerId(manufacturerId);
        a.setProductPrice(Double.parseDouble(price));
        a.setProductStock(Integer.parseInt(stock));
        a.setProductDescription(description);
        a.setProductPhoto(photo);

        db.accessoriesDao().insert(a);

        resetFields(3);
    }

    private Smartphone.PhoneColor getPhoneColor() {
        int color = rgSmartphoneColor.getCheckedRadioButtonId();
        switch (color) {
            case R.id.radBlack:
                return Smartphone.PhoneColor.????????;
            case R.id.radGray:
                return Smartphone.PhoneColor.????????;
            default:
                return Smartphone.PhoneColor.??????;
        }
    }

    private Watch.WatchColor getWatchColor() {
        int color = rgWatchColor.getCheckedRadioButtonId();
        switch (color) {
            case R.id.radWatchBlack:
                return Watch.WatchColor.????????;
            case R.id.radWatchGray:
                return Watch.WatchColor.????????;
            default:
                return Watch.WatchColor.??????;
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
            rgSmartphoneColor.check(R.id.radBlack);
            spiScreenSize.setSelection(0);
            spiStorageSize.setSelection(0);
            spiRamSize.setSelection(0);
            bmProduct = null;
            ivPhoto1.setImageBitmap(null);
        } else if (i == 2) {
            // Watch
            rgWatchColor.check(R.id.radWatchBlack);
            etWatchSize.setText("");
        } else if (i == 3) {
            // watch don't have a special layout
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

        // i values: 1 - smartphone, 2 - watch, 3 - accessory, 4 - findSelected

        resetFields(i);

        // Smartphone
        if (i == 1) {
            llAddNewProductSmartphone.setVisibility(View.GONE);
        } else if (i == 2) {
            llAddNewProductWatch.setVisibility(View.GONE);
        } else if (i == 3) {

        } else if (i == 4) {
            resetCategoriesFields(spiCategory.getSelectedItemPosition());
        }

    }

    private void toast(String msg) {
        Toast.makeText(ManagerActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}