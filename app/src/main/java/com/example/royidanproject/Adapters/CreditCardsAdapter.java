package com.example.royidanproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.core.content.res.ResourcesCompat;

public class CreditCardsAdapter extends BaseAdapter {

    private Context context;
    private List<CreditCard> cardList;
    private LayoutInflater inflater;
    private AppDatabase db;

    public CreditCardsAdapter(List<CreditCard> cardsList, Context context) {
        this.context = context;
        this.cardList = cardsList;
        inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int i) {
        return cardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return cardList.get(i).getCardId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CreditCard card = cardList.get(i);

        long userId = card.getUserId();
        Users user = db.usersDao().getUserById(userId);

        SimpleDateFormat cardSdf = new SimpleDateFormat("MM/yy");

        view = inflater.inflate(R.layout.custom_credit_card, null);

        TextView tvCardNumber = view.findViewById(R.id.tvCardNumber),
                tvCardExpireDate = view.findViewById(R.id.tvCardExpireDate),
                tvCardHolder = view.findViewById(R.id.tvCardHolder);

        String number = card.getCardNumber();

        String spacedNumber = "";
        spacedNumber += number.substring(0,4);
        spacedNumber += "  ";
        spacedNumber += number.substring(4,8);
        spacedNumber += "  ";
        spacedNumber += number.substring(8,12);
        spacedNumber += "  ";
        spacedNumber += number.substring(12,16);

        String userName = user.getUserName() + " " + user.getUserSurname();

        tvCardNumber.setText(spacedNumber);
        tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
        tvCardHolder.setText(userName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnRemove = dialog.findViewById(R.id.btnRemove),
                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
                        btnClose = dialog.findViewById(R.id.btnClose);


                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);

                float currentX = tvCardExpireDate.getX();
                tvCardExpireDate.setX(currentX - 60f);

                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);

                tvCardNumber.setGravity(Gravity.CENTER);

                String number = card.getCardNumber();

                String spacedNumber = "";
                spacedNumber += number.substring(0,4);
                spacedNumber += "  ";
                spacedNumber += number.substring(4,8);
                spacedNumber += "  ";
                spacedNumber += number.substring(8,12);
                spacedNumber += "  ";
                spacedNumber += number.substring(12,16);

                String userName = user.getUserName() + " " + user.getUserSurname();

                tvCardNumber.setText(spacedNumber);
                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
                tvCardHolder.setText(userName);
                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.creditCardDao().delete(card);
                    }
                });

            }
        });

        tvCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnRemove = dialog.findViewById(R.id.btnRemove),
                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
                        btnClose = dialog.findViewById(R.id.btnClose);


                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);

                float currentX = tvCardExpireDate.getX();
                tvCardExpireDate.setX(currentX - 60f);

                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);

                tvCardNumber.setGravity(Gravity.CENTER);

                String number = card.getCardNumber();

                String spacedNumber = "";
                spacedNumber += number.substring(0,4);
                spacedNumber += "  ";
                spacedNumber += number.substring(4,8);
                spacedNumber += "  ";
                spacedNumber += number.substring(8,12);
                spacedNumber += "  ";
                spacedNumber += number.substring(12,16);

                String userName = user.getUserName() + " " + user.getUserSurname();

                tvCardNumber.setText(spacedNumber);
                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
                tvCardHolder.setText(userName);
                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.creditCardDao().delete(card);
                    }
                });

            }
        });

        tvCardExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnRemove = dialog.findViewById(R.id.btnRemove),
                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
                        btnClose = dialog.findViewById(R.id.btnClose);


                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);

                float currentX = tvCardExpireDate.getX();
                tvCardExpireDate.setX(currentX - 60f);

                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);

                tvCardNumber.setGravity(Gravity.CENTER);

                String number = card.getCardNumber();

                String spacedNumber = "";
                spacedNumber += number.substring(0,4);
                spacedNumber += "  ";
                spacedNumber += number.substring(4,8);
                spacedNumber += "  ";
                spacedNumber += number.substring(8,12);
                spacedNumber += "  ";
                spacedNumber += number.substring(12,16);

                String userName = user.getUserName() + " " + user.getUserSurname();

                tvCardNumber.setText(spacedNumber);
                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
                tvCardHolder.setText(userName);
                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.creditCardDao().delete(card);
                    }
                });

            }
        });

        tvCardHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnRemove = dialog.findViewById(R.id.btnRemove),
                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
                        btnClose = dialog.findViewById(R.id.btnClose);


                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);

                float currentX = tvCardExpireDate.getX();
                tvCardExpireDate.setX(currentX - 60f);

                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);

                tvCardNumber.setGravity(Gravity.CENTER);

                String number = card.getCardNumber();

                String spacedNumber = "";
                spacedNumber += number.substring(0,4);
                spacedNumber += "  ";
                spacedNumber += number.substring(4,8);
                spacedNumber += "  ";
                spacedNumber += number.substring(8,12);
                spacedNumber += "  ";
                spacedNumber += number.substring(12,16);

                String userName = user.getUserName() + " " + user.getUserSurname();

                tvCardNumber.setText(spacedNumber);
                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
                tvCardHolder.setText(userName);
                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.creditCardDao().delete(card);
                    }
                });

            }
        });

        return view;
    }

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return new DecimalFormat("#.##").format(d);
    }
}
