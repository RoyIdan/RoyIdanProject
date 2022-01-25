package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.OrderAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    TextView tvOrderNumber, tvOrderDate, tvTotalPrice;
    Button btnReceipt, btnMainActivity;
    ListView lvDetails;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        db = AppDatabase.getInstance(OrderActivity.this);

        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnReceipt = findViewById(R.id.btnReceipt);
        btnMainActivity = findViewById(R.id.btnMainActivity);
        lvDetails = findViewById(R.id.lvDetails);

        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
            }
        });

        Intent intent = getIntent();
        if (intent == null || !intent.getExtras().containsKey("order")) {
            Toast.makeText(OrderActivity.this, "ההזמנה לא נמצאה", Toast.LENGTH_LONG).show();
            startActivity(new Intent(OrderActivity.this, MainActivity.class));
        }

        Order order = (Order) intent.getSerializableExtra("order");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        tvOrderNumber.setText(String.valueOf(order.getOrderId()));
        tvOrderDate.setText(sdf.format(order.getOrderDatePurchased()));

        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OrderActivity.this, ReceiptActivity.class);
                intent1.putExtra("order", order);
                intent1.putExtra("fromOrderActivity", true);
                startActivity(intent1);
            }
        });

        List<OrderDetails> detailsList = db.orderDetailsDao().getByOrderId(order.getOrderId());
        OrderAdapter adapter = new OrderAdapter(OrderActivity.this, detailsList);
        lvDetails.setAdapter(adapter);

    }

    public void onAdapterFinish(double totalPrice) {
        tvTotalPrice.setText(fmt(totalPrice));
    }

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return new DecimalFormat("#.##").format(1.199);
    }
}