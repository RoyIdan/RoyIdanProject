package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.CartAdapter;
import com.example.royidanproject.Adapters.UsersAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.Dialogs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.USERS_FOLDER_NAME;
import static com.example.royidanproject.Utility.Dialogs.createLoginDialog;

public class CartActivity extends AppCompatActivity {

    private ListView lvCart;
    private List<CartDetails> detailsList;
    private CartAdapter adapter;
    private Button btnMainActivity;
    AppDatabase db;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    TextView tvTotalPrice;

    public void addToTotalPrice(double amount) {
        String current = tvTotalPrice.getText().toString().trim();
        double currentVal = Double.parseDouble(current.trim().substring(1));

        double newVal = currentVal + amount;

        tvTotalPrice.setText('₪' + fmt(newVal));
    }

    public void removeFromTotalPrice(double amount) {
        String current = tvTotalPrice.getText().toString().trim();
        double currentVal = Double.parseDouble(current.trim().substring(1));

        double newVal = currentVal - amount;

        tvTotalPrice.setText('₪' + fmt(newVal));
    }

    public void setTotalPrice(double totalPrice) {
        tvTotalPrice.setText("₪" + fmt(totalPrice));
        Log.i(CartActivity.class.getSimpleName(), "updated price to: " + totalPrice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();

        db = AppDatabase.getInstance(CartActivity.this);

        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });

        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        long userId = sp.getLong("id", 0);
        if (userId == 0) {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
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
                Dialogs.createSubmitPurchaseDialog(CartActivity.this, detailsList);
            }
        });
    }

    public void onAdapterFinish(double totalPrice) {
        tvTotalPrice.setText('₪' + fmt(totalPrice));
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
                startActivity(new Intent(CartActivity.this, RegisterActivity.class));
                toast("reg");
                break;
            case R.id.menu_login:
                createLoginDialog(CartActivity.this);
                toast("in");
                break;
            case R.id.menu_logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                toast("out");
                break;
            default:
                int a = 1/0;
        }

        return true;
    }

    private void toast(String message) {
        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return new DecimalFormat("#.##").format(d);
    }
}