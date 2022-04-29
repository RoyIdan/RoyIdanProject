package com.example.royidanproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.royidanproject.AddNewCardActivity;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.CreditCard.CardCompany;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;
import com.example.royidanproject.Views.CreditCardView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import static com.example.royidanproject.Utility.CommonMethods.fmt;

public class CreditCardsAdapter extends BaseAdapter {

    private Context context;
    private List<CreditCard> cardList;
    private LayoutInflater inflater;
    private AppDatabase db;
    private boolean useClickListener;

    public CreditCardsAdapter(List<CreditCard> cardsList, Context context) {
        this.context = context;
        this.cardList = cardsList;
        inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        useClickListener = true;
    }

    public CreditCardsAdapter(List<CreditCard> cardsList, Context context, boolean useClickListener) {
        this.context = context;
        this.cardList = cardsList;
        inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        this.useClickListener = useClickListener;
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


        // using the old method
//        view = inflater.inflate(R.layout.custom_credit_card, null);
//
//        TextView tvCardNumber = view.findViewById(R.id.tvCardNumber),
//                tvCardExpireDate = view.findViewById(R.id.tvCardExpireDate),
//                tvCardHolder = view.findViewById(R.id.tvCardHolder);

        CreditCardView ccv = new CreditCardView(context, null);

        String number = card.getCardNumber();

        CardCompany company = card.getCardCompany();

        String userName = user.getUserName() + " " + user.getUserSurname();

//        tvCardNumber.setText(spacedNumber);
//        tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
//        tvCardHolder.setText(userName);

        ccv.setCardNumber(number);
        ccv.setCardExpireDate(card.getCardExpireDate());
        ccv.setSpHolder();
        ccv.setCardCompany(company);

        if (!useClickListener) {
            return ccv;
        }

        ccv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                CreditCardView ccv = dialog.findViewById(R.id.ccvCard);

                Button btnAddBalance = dialog.findViewById(R.id.btnAddBalance),
                        btnRemove = dialog.findViewById(R.id.btnRemove),
                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
                        btnClose = dialog.findViewById(R.id.btnClose);


                TextView //tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
//                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
//                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);

                EditText etAddBalance = dialog.findViewById(R.id.etAddBalance);


                String number = card.getCardNumber();

                ccv.setCardNumber(number);
                ccv.setCardExpireDate(card.getCardExpireDate());
                ccv.setSpHolder();
                ccv.setCardCompany(company);
                tvCardBalance.setText(fmt(card.getCardBalance()));

                btnAddBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etAddBalance.getText().toString().trim().isEmpty()) {
                            return;
                        }
                        double bonusBalance = Double.parseDouble(etAddBalance.getText().toString().trim());
                        double newBalance = card.getCardBalance() + bonusBalance;
                        card.setCardBalance(newBalance);
                        db.creditCardDao().updateBalanceById(card.getCardId(), newBalance);

                        tvCardBalance.setText(fmt(newBalance));
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, "לא ניתן לעדכן בגרסה זו של האפליקציה", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, AddNewCardActivity.class);
                        intent.putExtra("cardToUpdate", card);
                        context.startActivity(intent);
                        ((AppCompatActivity) context).finish();
                    }
                });

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (db.ordersDao().hasAnyCard(card.getCardId()) == null) {
                            db.creditCardDao().delete(card);
                            dialog.dismiss();
                            cardList.remove(card);
                            notifyDataSetInvalidated();
                        } else {
                            Toast.makeText(context, "לא ניתן למחוק את הכרטיס כי כבר נעשה בו שימוש", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

//        tvCardNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setView(promptDialog);
//                final AlertDialog dialog = alert.create();
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                Button btnRemove = dialog.findViewById(R.id.btnRemove),
//                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
//                        btnClose = dialog.findViewById(R.id.btnClose);
//
//
//                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
//                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
//                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
//                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);
//
//                float currentX = tvCardExpireDate.getX();
//                tvCardExpireDate.setX(currentX - 60f);
//
//                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);
//
//                tvCardNumber.setGravity(Gravity.CENTER);
//
//                String number = card.getCardNumber();
//
//                String spacedNumber = "";
//                spacedNumber += number.substring(0,4);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(4,8);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(8,12);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(12,16);
//
//                String userName = user.getUserName() + " " + user.getUserSurname();
//
//                tvCardNumber.setText(spacedNumber);
//                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
//                tvCardHolder.setText(userName);
//                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));
//
//                btnClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                btnRemove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        db.creditCardDao().delete(card);
//                    }
//                });
//
//            }
//        });
//
//        tvCardExpireDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setView(promptDialog);
//                final AlertDialog dialog = alert.create();
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                Button btnRemove = dialog.findViewById(R.id.btnRemove),
//                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
//                        btnClose = dialog.findViewById(R.id.btnClose);
//
//
//                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
//                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
//                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
//                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);
//
//                float currentX = tvCardExpireDate.getX();
//                tvCardExpireDate.setX(currentX - 60f);
//
//                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);
//
//                tvCardNumber.setGravity(Gravity.CENTER);
//
//                String number = card.getCardNumber();
//
//                String spacedNumber = "";
//                spacedNumber += number.substring(0,4);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(4,8);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(8,12);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(12,16);
//
//                String userName = user.getUserName() + " " + user.getUserSurname();
//
//                tvCardNumber.setText(spacedNumber);
//                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
//                tvCardHolder.setText(userName);
//                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));
//
//                btnClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                btnRemove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        db.creditCardDao().delete(card);
//                    }
//                });
//
//            }
//        });
//
//        tvCardHolder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View promptDialog = inflater.inflate(R.layout.custom_credit_card_detailed, null);
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setView(promptDialog);
//                final AlertDialog dialog = alert.create();
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                Button btnRemove = dialog.findViewById(R.id.btnRemove),
//                        btnUpdate = dialog.findViewById(R.id.btnUpdate),
//                        btnClose = dialog.findViewById(R.id.btnClose);
//
//
//                TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber),
//                        tvCardExpireDate = dialog.findViewById(R.id.tvCardExpireDate),
//                        tvCardHolder = dialog.findViewById(R.id.tvCardHolder),
//                        tvCardBalance = dialog.findViewById(R.id.tvCardBalance);
//
//                float currentX = tvCardExpireDate.getX();
//                tvCardExpireDate.setX(currentX - 60f);
//
//                tvCardNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f);
//
//                tvCardNumber.setGravity(Gravity.CENTER);
//
//                String number = card.getCardNumber();
//
//                String spacedNumber = "";
//                spacedNumber += number.substring(0,4);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(4,8);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(8,12);
//                spacedNumber += "  ";
//                spacedNumber += number.substring(12,16);
//
//                String userName = user.getUserName() + " " + user.getUserSurname();
//
//                tvCardNumber.setText(spacedNumber);
//                tvCardExpireDate.setText(cardSdf.format(card.getCardExpireDate()));
//                tvCardHolder.setText(userName);
//                tvCardBalance.setText('₪' + fmt(card.getCardBalance()));
//
//                btnClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                btnRemove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        db.creditCardDao().delete(card);
//                    }
//                });
//
//            }
//        });

        return ccv;
    }


}
