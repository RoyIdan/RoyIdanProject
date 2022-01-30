package com.example.royidanproject.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.royidanproject.R;

import androidx.annotation.Nullable;

public class CreditCardView1 extends LinearLayout {

    private String cardNumber, cardExpireDate, cardHolder;
    private TextView tvCardNumber, tvCardExpireDate, tvCardHolder;

    public CreditCardView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_credit_card, null);

        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CreditCardView, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            cardNumber = a.getString(R.styleable.CreditCardView_cardNumber);
            cardExpireDate = a.getString(R.styleable.CreditCardView_cardExpireDate);
            cardHolder = a.getString(R.styleable.CreditCardView_cardHolder);

        } finally {
            a.recycle();
        }

        tvCardNumber.setText(cardNumber);
        tvCardExpireDate.setText(cardExpireDate);
        tvCardHolder.setText(cardHolder);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        tvCardNumber.setText(cardNumber);
    }

    public String getCardExpireDate() {
        return cardExpireDate;
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.cardExpireDate = cardExpireDate;
        tvCardExpireDate.setText(cardExpireDate);
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
        tvCardHolder.setText(cardHolder);
    }
}
