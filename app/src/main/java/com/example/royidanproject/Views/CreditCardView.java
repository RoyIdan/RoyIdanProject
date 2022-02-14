package com.example.royidanproject.Views;

import static com.example.royidanproject.MainActivity.SP_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard.CardCompany;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardView extends LinearLayout {

    public static SimpleDateFormat cardSdf = new SimpleDateFormat("MM/yy");

    private TextView tvCardNumber, tvCardExpireDate, tvCardHolder;
    private ImageView ivCardCompany;
    private TypedArray typedArray;

    private long w, h;

    SharedPreferences sp;
    AppDatabase db;

    private void setViewPointers() {
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);
        ivCardCompany = findViewById(R.id.ivCardCompany);
    }

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        sp = context.getSharedPreferences(SP_NAME, 0);
        db = AppDatabase.getInstance(context);

        inflate(context, R.layout.custom_credit_card, this);

        setViewPointers();

        init();

        String attrsHolder = "";

        if (attrs != null) {

            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardView);

            setCardNumber(typedArray.getString(R.styleable.CreditCardView_cardNumber));
            setCardExpireDate(typedArray.getString(R.styleable.CreditCardView_cardExpireDate));
            attrsHolder = typedArray.getString(R.styleable.CreditCardView_cardHolder);

            if (typedArray.hasValue(R.styleable.CreditCardView_cardCompany)) {
                int value = typedArray.getInt(R.styleable.CreditCardView_cardCompany, -1);
                if (value != -1)
                    setCardCompany(value);
            }

            typedArray.recycle();
        }

        String holder = "UNKNOWN";
        if (sp.getLong("id", 0) != 0) {
            holder = getSpHolderName();
        }

        if (attrsHolder != null && !attrsHolder.isEmpty()) {
            holder = attrsHolder;
        }

        setCardHolder(holder);


    }

    private void init() {

    }

    public void setSpHolder() {
        setCardHolder(getSpHolderName());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        w = this.getWidth();
        h = this.getHeight();

        setMargins();
    }

    private void setMargins() {
        long numberW = tvCardNumber.getWidth();
        long numberMargin = (w - numberW) / 2;
        tvCardNumber.setX(numberMargin);

        long expW = tvCardExpireDate.getWidth();
        float expMargin = (w - expW) * 0.445f;
        tvCardExpireDate.setX(expMargin);

        long holderW = tvCardHolder.getWidth();
        long holderMargin = (w - holderW) / 8;
        tvCardHolder.setX(holderMargin);

        long companyW = ivCardCompany.getWidth();
        float companyMargin = (w - companyW) * 0.84f;
        ivCardCompany.setX(companyMargin);

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

    public String getCardHolder() {
        return tvCardHolder.getText().toString();
    }

    public void setCardHolder(String cardHolder) {
        this.tvCardHolder.setText(cardHolder);
    }

    public Drawable getCardCompany() {
        return ivCardCompany.getDrawable();
    }

    public void setCardCompany(int ordinal) {
        CardCompany cc;
        switch(ordinal) {
            case 0:
                cc = CardCompany.AMERICAN_EXPRESS;
                break;
            case 1:
                cc = CardCompany.ISRACARD;
                break;
            case 2:
                cc = CardCompany.MASTERCARD;
                break;
            default:
                cc = CardCompany.VISA;
                break;
        }

        setCardCompany(cc);
    }

    public void setCardCompany(CardCompany cardCompany) {
        int resId;
        switch (cardCompany) {
            case VISA:
                resId = R.drawable.visa;
                break;
            case ISRACARD:
                resId = R.drawable.isracard;
                break;
            case MASTERCARD:
                resId = R.drawable.mastercard;
                break;
            case AMERICAN_EXPRESS:
                resId = R.drawable.american;
                break;
            default:
                // error
                resId = 0;
        }

        Drawable companyImg =
                ResourcesCompat.getDrawable(getResources(), resId, null);

        ivCardCompany.setImageDrawable(companyImg);
    }
}
