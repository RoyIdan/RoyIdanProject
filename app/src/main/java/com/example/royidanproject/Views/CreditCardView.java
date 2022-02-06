package com.example.royidanproject.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.royidanproject.R;

import org.w3c.dom.Text;

public class CreditCardView extends LinearLayout {

    private TextView tvCardNumber, tvCardExpireDate, tvCardHolder;
    private TypedArray typedArray;

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.custom_credit_card, null);

        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);

        typedArray = context.obtainStyledAttributes(R.styleable.CreditCardView);

        tvCardNumber.setText(typedArray.getString(R.styleable.CreditCardView_cardNumber));
        tvCardExpireDate.setText(typedArray.getString(R.styleable.CreditCardView_cardExpireDate));
        tvCardHolder.setText(typedArray.getString(R.styleable.CreditCardView_cardHolder));

        typedArray.recycle();
    }

    public String getCardNumber() {
        return tvCardNumber.getText().toString();
    }

    public void setCardNumber(String cardNumber) {
        this.tvCardNumber.setText(cardNumber);
    }

    public String getCardExpireDate() {
        return tvCardExpireDate.getText().toString();
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.tvCardExpireDate.setText(cardExpireDate);
    }

    public String  getCardHolder() {
        return tvCardHolder.getText().toString();
    }

    public void setCardHolder(String cardHolder) {
        this.tvCardHolder.setText(cardHolder);
    }
}
