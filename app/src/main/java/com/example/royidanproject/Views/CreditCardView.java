package com.example.royidanproject.Views;

import static com.example.royidanproject.MainActivity.SP_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardView extends LinearLayout {

    public static SimpleDateFormat cardSdf = new SimpleDateFormat("MM/yy");

    private TextView tvCardNumber, tvCardExpireDate, tvCardHolder;
    private TypedArray typedArray;

    SharedPreferences sp;
    AppDatabase db;

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        sp = context.getSharedPreferences(SP_NAME, 0);
        db = AppDatabase.getInstance(context);

        inflate(context, R.layout.custom_credit_card, this);

        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);

        if (attrs != null) {

            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardView);

            String b = typedArray.getString(R.styleable.CreditCardView_cardNumber);
            setCardNumber(typedArray.getString(R.styleable.CreditCardView_cardNumber));
            setCardExpireDate(typedArray.getString(R.styleable.CreditCardView_cardExpireDate));
            setCardHolder(typedArray.getString(R.styleable.CreditCardView_cardHolder));

            typedArray.recycle();
        }


    }

    public void setSpHolder() {
        setCardHolder(getSpHolderName());
    }

    public void centerViews() {
        float numberX, expX, holderX;;
        expX = tvCardExpireDate.getX();
        holderX = tvCardHolder.getX();
        numberX = tvCardNumber.getX();
        tvCardNumber.setX(0);
        tvCardExpireDate.setX(expX - 20);
        tvCardHolder.setX(holderX - 20);
    }

    private String getSpHolderName() {
        long userId = sp.getLong("id", 0);
        Users user = db.usersDao().getUserById(userId);
        if (user != null) {
            return user.getUserName() + " " + user.getUserSurname();
        }
        return "UNKNOWN";
    }

    public String getCardNumber() {
        return tvCardNumber.getText().toString().replace(" ", "");
    }

    public void setCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            tvCardNumber.setText("");
            return;
        }
        String spacedNumber = "";
        for (int i = 0; i < cardNumber.length(); i+= 4) {
            String after = cardNumber.substring(i);
            if (after.length() < 4) {
                spacedNumber += " " + after;
            } else {
                spacedNumber += " " + after.substring(0, 4);
            }
        }

        this.tvCardNumber.setText(spacedNumber);
    }

    public String getCardExpireDate() {
        return tvCardExpireDate.getText().toString();
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.tvCardExpireDate.setText(cardExpireDate);
    }

    public void setCardExpireDate(Date expireDate) {
        String cardExpireDate = cardSdf.format(expireDate);
        setCardExpireDate(cardExpireDate);
    }

    public String  getCardHolder() {
        return tvCardHolder.getText().toString();
    }

    public void setCardHolder(String cardHolder) {
        this.tvCardHolder.setText(cardHolder);
    }
}
