package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.example.royidanproject.Adapters.CreditCardsAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;

import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class CreditCardsActivity extends AppCompatActivity {

    CreditCardsAdapter adapter;
    ListView lvCards;

    AppDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_cards);

        db = AppDatabase.getInstance(CreditCardsActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        lvCards = findViewById(R.id.lvCards);

        long userId = sp.getLong("id", 0);
        List<CreditCard> cardsList = db.creditCardDao().getByUserId(userId);

        adapter = new CreditCardsAdapter(cardsList, CreditCardsActivity.this);

        lvCards.setAdapter(adapter);
    }


}