package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.ReceiptAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ReceiptActivity extends AppCompatActivity {

    TextView tvReceiptNumber, tvReceiptDate, tvReceiptCreditCard, tvReceiptTotalPrice;
    ListView lvDetails;
    Button btnReturn;
    ReceiptAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        db = AppDatabase.getInstance(ReceiptActivity.this);

        tvReceiptNumber = findViewById(R.id.tvReceiptNumber);
        tvReceiptDate = findViewById(R.id.tvReceiptDate);
        tvReceiptCreditCard = findViewById(R.id.tvReceiptCreditCard);
        tvReceiptTotalPrice = findViewById(R.id.tvReceiptTotalPrice);
        lvDetails = findViewById(R.id.lvDetails);
        btnReturn = findViewById(R.id.btnReturn);

        Intent intent = getIntent();
        if (intent == null || !intent.getExtras().containsKey("order")) {
            Toast.makeText(ReceiptActivity.this, "ההזמנה לא נמצאה", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
        }

        Order order = (Order) intent.getSerializableExtra("order");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        long creditCardId = order.getCreditCardId();
        CreditCard creditCard = db.creditCardDao().getById(creditCardId);
        String lastFourDigits = creditCard.getCardNumber().substring(12);

        tvReceiptNumber.setText(String.valueOf(order.getOrderId()));
        tvReceiptDate.setText(sdf.format(order.getOrderDatePurchased()));
        tvReceiptCreditCard.setText("xxxx-xxxx-xxxx-" + lastFourDigits);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
            }
        });

        if (intent.getBooleanExtra("fromOrderActivity", false)) {
            btnReturn.setText("חזרה לדף הזמנה");
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ReceiptActivity.this, OrderActivity.class).putExtra("order", order));
                }
            });
        }

        adapter = new ReceiptAdapter(ReceiptActivity.this, order);
        lvDetails.setAdapter(adapter);

    }

    public void onAdapterFinish(double totalPrice) {
        tvReceiptTotalPrice.setText(fmt(totalPrice));
    }

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return new DecimalFormat("#.##").format(d);
    }

}