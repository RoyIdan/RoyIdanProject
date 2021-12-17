package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.royidanproject.Adapters.ProductsAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    TextInputEditText etFrom, etTo;
    Button btnFilter, btnMainActivity;
    ListView lvProducts;
    ProductsAdapter adapter;
    AppDatabase db;

    private void setViewPointers() {
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        btnFilter = findViewById(R.id.btnFilter);
        lvProducts = findViewById(R.id.lvProducts);
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
            }
        });

        setViewPointers();
        db = AppDatabase.getInstance(GalleryActivity.this);

        List<Product> productList = new LinkedList<>();
        productList.addAll(db.smartphonesDao().getAll());
        // TODO - add the rest

        adapter = new ProductsAdapter(GalleryActivity.this, productList);
        lvProducts.setAdapter(adapter);




        findViewById(R.id.btnFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] boxes = new boolean[3];
                int index = 0;
                LinearLayout llCategories = (LinearLayout) findViewById(R.id.llCategories);
                int count = llCategories.getChildCount();
                for (int i = 1; i < count; i++) {
                    View view = llCategories.getChildAt(i);
                    if (view instanceof CheckBox) {
                        boxes[index++] = (((CheckBox) view).isChecked());
                    }
                }
                String query = "";

                productList.clear();

                String from  = etFrom.getText().toString().trim();
                String to = etTo.getText().toString().trim();

                if (!from.isEmpty() && !to.isEmpty()) {

                    if (!query.isEmpty())
                    query += " and ";

                    query += "productPrice between " + from + " and " + to;
                }
                else if (!from.isEmpty()) {

                    if (!query.isEmpty())
                        query += " and ";

                    query += "productPrice > " + from;
                }
                else if (!to.isEmpty()) {

                    if (!query.isEmpty())
                        query += " and ";

                    query += "productPrice < " + to;
                }

                if (boxes[0]) {
                    List<Smartphone> smartphonesList;
                    if (query.isEmpty()) {
                        smartphonesList = db.smartphonesDao().getAll();
                    }
                    else {
                        smartphonesList = db.smartphonesDao().getByQuery(query);
                    }

                    productList.addAll(smartphonesList);
                }

                // TODO make the final query(ies)

                adapter.updateProductsList(productList); // TODO make actual list

                adapter.notifyDataSetInvalidated();
            }
        });

    }
}