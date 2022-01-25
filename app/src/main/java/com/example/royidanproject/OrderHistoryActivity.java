package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.royidanproject.Adapters.OrderHistoryAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;

import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class OrderHistoryActivity extends AppCompatActivity {
    Button btnMainActivity;
    RadioGroup rgOrderHistory;
    RadioButton rbManagerOnly, rbEveryone;
    ListView lvOrderHistory;
    AppDatabase db;
    SharedPreferences sp;

    List<Order> orders;
    OrderHistoryAdapter adapter;

    private void setViewPointers() {
        btnMainActivity = findViewById(R.id.btnMainActivity);
        rgOrderHistory = findViewById(R.id.rgOrderHistory);
        rbManagerOnly = findViewById(R.id.rbManagerOnly);
        rbEveryone = findViewById(R.id.rbEveryone);
        lvOrderHistory = findViewById(R.id.lvOrderHistory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        db = AppDatabase.getInstance(OrderHistoryActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        orders = new LinkedList<>();

        setViewPointers();

        if (sp.getBoolean("admin", false)) {
            orders = db.ordersDao().getAll();
        } else {
            long userId = sp.getLong("id", 0);
            orders = db.ordersDao().getByCustomerId(userId);
            rgOrderHistory.setVisibility(View.GONE);
        }

        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orders);
        lvOrderHistory.setAdapter(adapter);


        rgOrderHistory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbManagerOnly.getId()) {
                    long userId = sp.getLong("id", 0);
                    orders = db.ordersDao().getByCustomerId(userId);
                    adapter.updateList(orders);
                    adapter.notifyDataSetInvalidated();
                } else {
                    orders = db.ordersDao().getAll();
                    adapter.updateList(orders);
                    adapter.notifyDataSetInvalidated();
                }
            }
        });

    }
}