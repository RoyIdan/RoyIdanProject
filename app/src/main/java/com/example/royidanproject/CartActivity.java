package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.CartAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.PurchaseManager;

import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.Utility.CommonMethods.fmt;

public class CartActivity extends AppCompatActivity {

    private ListView lvCart;
    private List<CartDetails> detailsList;
    private CartAdapter adapter;
    private Button btnMainActivity;
    AppDatabase db;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    TextView tvTotalPrice;
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

    public void setTotalPrice(double totalPrice) {
        tvTotalPrice.setText(fmt(totalPrice));
        Log.i(CartActivity.class.getSimpleName(), "updated price to: " + totalPrice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();

        db = AppDatabase.getInstance(CartActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(CartActivity.this, toolbar);

        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finish();
            }
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        long userId = sp.getLong("id", 0);
        if (userId == 0) {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
            finish();
        }
        detailsList = db.cartDetailsDao().getCartDetailsByUserId(userId);
        adapter = new CartAdapter(CartActivity.this, detailsList);

        lvCart = findViewById(R.id.lvCart);
        lvCart.setAdapter(adapter);

//        tvTotalPrice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvTotalPrice.setText(String.valueOf(adapter.getTotalPrice()));
//            }
//        });

        findViewById(R.id.btnBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsList = db.cartDetailsDao().getCartDetailsByUserId(userId);
                //Dialogs.createSubmitPurchaseDialog(CartActivity.this, detailsList);
                new PurchaseManager(CartActivity.this, detailsList);
            }
        });

        double totalPrice = 0;
        for (int i = 0; i < detailsList.size(); i++) {
            double p;
            long pId = detailsList.get(i).getProductId();
            if (detailsList.get(i).getTableId() == 1) {
                p = db.smartphonesDao().getSmartphoneById(pId).getProductPrice();
            } else if (detailsList.get(i).getTableId() == 2) {
                p = db.watchesDao().getWatchById(pId).getProductPrice();
            } else {
                p = db.accessoriesDao().getAccessoryById(pId).getProductPrice();
            }
            totalPrice += p * detailsList.get(i).getProductQuantity();
        }
        tvTotalPrice.setText(CommonMethods.fmt(totalPrice));
    }

    public void onAdapterFinish(double totalPrice) {
        //tvTotalPrice.setText(fmt(totalPrice));
    }

    private void toast(String message) {
        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}