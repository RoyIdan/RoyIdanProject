package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.ToolbarManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static com.example.royidanproject.Utility.CommonMethods.fmt;

public class ReceiptActivity extends AppCompatActivity {

    TextView tvReceiptNumber, tvReceiptDate, tvReceiptCustomer, tvReceiptPhone, tvReceiptAddress, tvReceiptCity,
            tvReceiptCreditCard, tvReceiptCreditCardCompany, tvReceiptBeforeVat, tvReceiptVat, tvReceiptTotalPrice;
    ListView lvDetails;
    Button btnReturn;
    ReceiptAdapter adapter;
    AppDatabase db;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        db = AppDatabase.getInstance(ReceiptActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(ReceiptActivity.this, toolbar);

        tvReceiptNumber = findViewById(R.id.tvReceiptNumber);
        tvReceiptDate = findViewById(R.id.tvReceiptDate);
        tvReceiptCustomer = findViewById(R.id.tvReceiptCustomer);
        tvReceiptPhone = findViewById(R.id.tvReceiptPhone);
        tvReceiptAddress = findViewById(R.id.tvReceiptAddress);
        tvReceiptCity = findViewById(R.id.tvReceiptCity);
        tvReceiptCreditCard = findViewById(R.id.tvReceiptCreditCard);
        tvReceiptCreditCardCompany = findViewById(R.id.tvReceiptCreditCardCompany);
        tvReceiptBeforeVat = findViewById(R.id.tvReceiptBeforeVat);
        tvReceiptVat = findViewById(R.id.tvReceiptVat);
        tvReceiptTotalPrice = findViewById(R.id.tvReceiptTotalPrice);
        lvDetails = findViewById(R.id.lvDetails);
        btnReturn = findViewById(R.id.btnReturn);

        Intent intent = getIntent();
        if (intent == null || !intent.getExtras().containsKey("order")) {
            Toast.makeText(ReceiptActivity.this, "ההזמנה לא נמצאה", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
            finish();
        }

        Order order = (Order) intent.getSerializableExtra("order");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Users user = db.usersDao().getUserById(order.getCustomerId());

        long creditCardId = order.getCreditCardId();
        CreditCard creditCard = db.creditCardDao().getById(creditCardId);
        String lastFourDigits = creditCard.getCardNumber().substring(12);

        tvReceiptNumber.setText(String.valueOf(order.getOrderId()));
        tvReceiptDate.setText(sdf.format(order.getOrderDatePurchased()));
        tvReceiptCustomer.setText(user.getUserName() + " " + user.getUserSurname());
        tvReceiptPhone.setText(user.getUserPhone());
        tvReceiptAddress.setText(user.getUserAddress());
        tvReceiptCity.setText(user.getUserCity());
        tvReceiptCreditCard.setText("xxxx-xxxx-xxxx-" + lastFourDigits);
        tvReceiptCreditCardCompany.setText(creditCard.getCardCompany().toString());

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
                finish();
            }
        });

        if (intent.getBooleanExtra("fromOrderActivity", false)) {
            btnReturn.setText("חזרה לדף הזמנה");
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ReceiptActivity.this, OrderActivity.class).putExtra("order", order));
                    finish();
                }
            });
        }

        adapter = new ReceiptAdapter(ReceiptActivity.this, order);
        lvDetails.setAdapter(adapter);

    }

    public void onAdapterFinish(double totalPrice) {
        double beforeVat = totalPrice / 1.17;
        double vat = totalPrice - beforeVat;
        tvReceiptBeforeVat.setText(fmt(beforeVat));
        tvReceiptVat.setText(fmt(vat));
        tvReceiptTotalPrice.setText(fmt(totalPrice));
    }


}