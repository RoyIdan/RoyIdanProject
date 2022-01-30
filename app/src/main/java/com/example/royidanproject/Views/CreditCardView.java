package com.example.royidanproject.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.royidanproject.R;

import androidx.annotation.Nullable;

public class CreditCardView extends View {

    private String cardNumber, cardExpireDate, cardHolder;
    private Paint textPaint;
    private int textColor;
    private float textHeight;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        invalidate();
        requestLayout();
    }

    public String getCardExpireDate() {
        return cardExpireDate;
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.cardExpireDate = cardExpireDate;
        invalidate();
        requestLayout();
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
        invalidate();
        requestLayout();
    }

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

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
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
