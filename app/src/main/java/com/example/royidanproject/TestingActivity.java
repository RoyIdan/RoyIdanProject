package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.royidanproject.Views.CreditCardView;

import java.util.Date;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        LinearLayout llRoot = findViewById(R.id.llRoot);

        CreditCardView ccv = new CreditCardView(TestingActivity.this, null);
        ccv.setCardExpireDate(new Date());

        llRoot.addView(ccv);
    }
}