package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.royidanproject.Adapters.CreditCardsAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;

import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class CreditCardsActivity extends AppCompatActivity {

    CreditCardsAdapter adapter;
    ListView lvCards;

    Button btnMainActivity, btnAddNewCard;

    AppDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_cards);

        db = AppDatabase.getInstance(CreditCardsActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnAddNewCard = findViewById(R.id.btnAddNewCard);

        lvCards = findViewById(R.id.lvCards);

        long userId = sp.getLong("id", 0);
        List<CreditCard> cardsList = db.creditCardDao().getByUserId(userId);

        adapter = new CreditCardsAdapter(cardsList, CreditCardsActivity.this);

        lvCards.setAdapter(adapter);

        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditCardsActivity.this, MainActivity.class));
                finish();
            }
        });

        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditCardsActivity.this, AddNewCardActivity.class));
                finish();
            }
        });
    }


}