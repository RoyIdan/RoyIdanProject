package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.royidanproject.Adapters.ProductsAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    TextInputEditText etFrom, etTo;
    Button btnFilter;
    ProductsAdapter adapter;
    AppDatabase db;

    private void setViewPointers() {
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        btnFilter = findViewById(R.id.btnFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setViewPointers();
        db = AppDatabase.getInstance(GalleryActivity.this);

        List<Product> productList = new LinkedList<>();
        adapter = new ProductsAdapter(GalleryActivity.this, productList);




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
                        boxes[index++] = (((CheckBox) v).isChecked());
                    }
                }
                String query = "";

                if (boxes[0]) {
                    query = "";
                    List<Smartphone> smartphonesList = db.smartphonesDao().getByQuery(query);
                    productList.addAll(smartphonesList);
                }

//                double from = Double.parseDouble(etFrom.getText().toString().trim());
//                double to = Double.parseDouble(etTo.getText().toString().trim());

                // TODO make the final query(ies)

                adapter.updateProductsList(productList); // TODO make actual list

                adapter.notifyDataSetInvalidated();
            }
        });

    }
}