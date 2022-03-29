package com.example.royidanproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.royidanproject.Adapters.ProductsAdapter;
import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.Utility.ToolbarManager;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class GalleryActivity extends AppCompatActivity {

    TextInputEditText etFrom, etTo, etFilter;
    Button btnFilter, btnMainActivity;
    ListView lvProducts;
    ProductsAdapter adapter;
    AppDatabase db;
    SharedPreferences sp;

    private ToolbarManager toolbarManager;

    private void setViewPointers() {
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        etFilter = findViewById(R.id.etFilter);
        btnFilter = findViewById(R.id.btnFilter);
        lvProducts = findViewById(R.id.lvProducts);
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, MainActivity.class));
                finish();
            }
        });

        setViewPointers();
        db = AppDatabase.getInstance(GalleryActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        etFrom.clearFocus();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(GalleryActivity.this, toolbar);

        List<Product> productList = new LinkedList<>();
        if (sp.getBoolean("admin", false)) {
            productList.addAll(db.smartphonesDao().getAll());
            productList.addAll(db.watchesDao().getAll());
            productList.addAll(db.accessoriesDao().getAll());
        } else {
            productList.addAll(db.smartphonesDao().getAll_whereInStock());
            productList.addAll(db.watchesDao().getAll_whereInStock());
            productList.addAll(db.accessoriesDao().getAll_whereInStock());
        }

        // sort by product name
        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                return product.getProductName().compareTo(t1.getProductName());
            }
        });

        adapter = new ProductsAdapter(GalleryActivity.this, productList);
        lvProducts.setAdapter(adapter);

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] boxes = new boolean[3];
                LinearLayout llCategories = (LinearLayout) findViewById(R.id.llCategories);
                int count = llCategories.getChildCount();
                for (int i = 1; i < count; i++) {
                    View view = llCategories.getChildAt(i);
                    if (view instanceof CheckBox) {
                        boxes[i - 1] = (((CheckBox) view).isChecked());
                    }
                }
                String query = "";

                productList.clear();

                String from  = etFrom.getText().toString().trim();
                String to = etTo.getText().toString().trim();
                boolean from_valid = false;
                boolean to_valid = false;
                try {
                    Double.parseDouble(from);
                    from_valid = true;
                } catch (NumberFormatException e) { }
                try {
                    Double.parseDouble(to);
                    to_valid = true;
                } catch (NumberFormatException e) { }

                if (!from.isEmpty() && from_valid && !to.isEmpty() && to_valid) {

                    query += "productPrice between " + from + " and " + to;
                }
                else if (!from.isEmpty() && from_valid) {

                    query += "productPrice > " + from;
                }
                else if (!to.isEmpty() && to_valid) {

                    query += "productPrice < " + to;
                }

//                String filter = etFilter.getText().toString().trim();
//                if (!filter.isEmpty()) {
//                    if (!query.isEmpty()) {
//                        query += " and ";
//                    }
//                    query += "productName LIKE %" + filter + "%";
//                }

                RadioGroup rgSortBy = findViewById(R.id.rgSortBy);
                int orderBy = 0;
                for (int i = 0; i < rgSortBy.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rgSortBy.getChildAt(i);
                    if (rb.isChecked()) {
                        orderBy = i;
                        break;
                    }
                }

                // Smartphones
                if (boxes[0]) {
                    List<Smartphone> smartphonesList;
                    if (query.isEmpty()) {
                        if (sp.getBoolean("admin", false)) {
                            smartphonesList = db.smartphonesDao().getAll();
                        } else {
                            smartphonesList = db.smartphonesDao().getAll_whereInStock();
                        }
                    }
                    else {
                        String baseSPQuery;
                        if (sp.getBoolean("admin", false)) {
                            baseSPQuery = "SELECT * FROM tblSmartphones WHERE ";
                        } else {
                            baseSPQuery = "SELECT * FROM tblSmartphones WHERE productStock > 0 AND ";
                        }
                        String finalSPQuery = baseSPQuery + query;
                        SimpleSQLiteQuery ssq = new SimpleSQLiteQuery(finalSPQuery);
                        smartphonesList = db.smartphonesDao().getByQuery(ssq);
                    }

                    productList.addAll(smartphonesList);
                }

                //Watches
                if (boxes[1]) {
                    List<Watch> watchesList;
                    if (query.isEmpty()) {
                        if (sp.getBoolean("admin", false)) {
                            watchesList = db.watchesDao().getAll();
                        } else {
                            watchesList = db.watchesDao().getAll_whereInStock();
                        }
                    }
                    else {
                        String baseWatchesQuery;
                        if (sp.getBoolean("admin", false)) {
                            baseWatchesQuery = "SELECT * FROM tblWatches WHERE ";
                        } else {
                            baseWatchesQuery = "SELECT * FROM tblWatches WHERE productStock > 0 AND ";
                        }
                        String finalWatchesQuery = baseWatchesQuery + query;
                        SimpleSQLiteQuery ssq = new SimpleSQLiteQuery(finalWatchesQuery);
                        watchesList = db.watchesDao().getByQuery(ssq);
                    }

                    productList.addAll(watchesList);
                }

                if (boxes[2]) {
                    List<Accessory> accessoriesList;
                    if (query.isEmpty()) {
                        if (sp.getBoolean("admin", false)) {
                            accessoriesList = db.accessoriesDao().getAll();
                        } else {
                            accessoriesList = db.accessoriesDao().getAll_whereInStock();
                        }
                    }
                    else {
                        String baseAccessoriesQuery;
                        if (sp.getBoolean("admin", false)) {
                            baseAccessoriesQuery = "SELECT * FROM tblAccessories WHERE ";
                        } else {
                            baseAccessoriesQuery = "SELECT * FROM tblAccessories WHERE productStock > 0 AND ";
                        }
                        String finalAccessoriesQuery = baseAccessoriesQuery + query;
                        SimpleSQLiteQuery ssq = new SimpleSQLiteQuery(finalAccessoriesQuery);
                        accessoriesList = db.accessoriesDao().getByQuery(ssq);
                    }

                    productList.addAll(accessoriesList);
                }

                orderByName(productList);
                orderListBy(productList, orderBy);

                adapter.updateProductsList(productList);

                adapter.notifyDataSetInvalidated();

                adapter.getFilter().filter(etFilter.getText().toString().trim());
            }
        });
    }

    private void orderByName(List<Product> list) {
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                return product.getProductName().compareTo(t1.getProductName());
            }
        });
    }

    private void orderListBy(List<Product> list, int orderBy) {
        // product name, manufacturer name, price asc, price desc

        if (orderBy == 0) {

        } else if (orderBy == 1) {
            List<Manufacturer> manufacturers = db.manufacturersDao().getAll();
            orderByName(list);

            Collections.sort(list, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    Manufacturer m1 = null, m2 = null;
                    for (Manufacturer m : manufacturers) {
                        if (m.getManufacturerId() == product.getManufacturerId()) {
                            m1 = m;
                            if (m2 != null) {
                                break;
                            }
                        }
                        if (m.getManufacturerId() == t1.getManufacturerId()) {
                            m2 = m;
                            if (m1 != null) {
                                break;
                            }
                        }
                    }

                    return m1.getManufacturerName().compareTo(m2.getManufacturerName());
                }
            });
        }
        else if (orderBy == 2) {
            Collections.sort(list, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return (int) product.getProductPrice() - (int) t1.getProductPrice();
                }
            });
        }
        else if (orderBy == 3) {
            Collections.sort(list, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return (int) t1.getProductPrice() - (int) product.getProductPrice();
                }
            });
        }

    }
}