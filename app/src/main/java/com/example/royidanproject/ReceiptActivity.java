package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.Order;

public class ReceiptActivity extends AppCompatActivity {

    TextView tvReceiptNumber, tvReceiptDate, tvReceiptCreditCard, tvReceiptTotalPrice;
    ListView lvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        tvReceiptNumber = findViewById(R.id.tvReceiptNumber);
        tvReceiptDate = findViewById(R.id.tvReceiptDate);
        tvReceiptCreditCard = findViewById(R.id.tvReceiptCreditCard);
        tvReceiptTotalPrice = findViewById(R.id.tvReceiptTotalPrice);
        lvDetails = findViewById(R.id.lvDetails);

        Intent intent = getIntent();
        if (intent == null || !intent.getExtras().containsKey("order")) {
            Toast.makeText(ReceiptActivity.this, "ההזמנה לא נמצאה", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
        }

        Order order = (Order) intent.getSerializableExtra("order");

    }
}