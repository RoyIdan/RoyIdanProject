package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CreditCard;

public class AddNewCardActivity extends AppCompatActivity {
    TextView tvCardNumber, tvCardExpireDate, tvCardHolder;
    EditText etCardNumber, etCardExpireDate, etCardHolder, etCardCvv;
    Button btnMainActivity, btnCreditCardsActivity, btnAdd;

    SharedPreferences sp;
    AppDatabase db;

    private void setViewPointers() {
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpireDate = findViewById(R.id.etCardExpireDate);
        etCardHolder = findViewById(R.id.etCardHolder);
        etCardCvv = findViewById(R.id.etCardCvv);
        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnCreditCardsActivity = findViewById(R.id.btnCreditCardsActivity);
        btnAdd = findViewById(R.id.btnAdd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        sp = getSharedPreferences(SP_NAME, 0);
        db = AppDatabase.getInstance(AddNewCardActivity.this);

        setViewPointers();

        String userName = sp.getString("name", "ERROR");

        etCardHolder.setText(userName);
        tvCardHolder.setText(userName);
        etCardHolder.setFocusable(false);

        etCardExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDatePickerDialog();
            }
        });

        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String number = charSequence.toString().trim();
                String formatted = "";
                for (int j = 0; j < number.length(); j++) {
                    if (j != 0 && j % 4 == 0) {
                        formatted += " ";
                    }

                    formatted += number.charAt(j);
                }

                tvCardNumber.setText(formatted);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewCardActivity.this, MainActivity.class));
            }
        });

        btnCreditCardsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewCardActivity.this, CreditCardsActivity.class));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long userId = sp.getLong("id", 0);
                String number = etCardNumber.getText().toString().trim();
                String cvv = etCardCvv.getText().toString().trim();
                String expire = etCardExpireDate.getText().toString().trim();
                Date expireDate = null;
                try {
                    expireDate = new SimpleDateFormat("MM/yy").parse(expire);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CreditCard cc = new CreditCard();
                cc.setUserId(userId);
                cc.setCardNumber(number);
                cc.setCvv(cvv);
                cc.setCardExpireDate(expireDate);
                cc.setCardBalance(0d);

                db.creditCardDao().insert(cc);
            }
        });
    }

    private void buildDatePickerDialog() {
        View promptDialog = LayoutInflater.from(AddNewCardActivity.this).inflate(R.layout.custom_month_year_picker_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(AddNewCardActivity.this);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Spinner year = dialog.findViewById(R.id.spiYear),
            month = dialog.findViewById(R.id.spiMonth);

        Button btnSave = dialog.findViewById(R.id.btnSave);

        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        LinkedList<String> ll = new LinkedList();
        for (int i = 0; i < 20; i++) {
            ll.add(String.valueOf(currYear + i));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter(AddNewCardActivity.this, R.layout.support_simple_spinner_dropdown_item, ll);

        year.setAdapter(adapter);

        int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        LinkedList<String> monthLl = new LinkedList();
        for (int i = currMonth; i < 13; i++) {
            monthLl.add(String.valueOf(i));
        }

        ArrayAdapter<String> monthAdapter =
                new ArrayAdapter(AddNewCardActivity.this, R.layout.support_simple_spinner_dropdown_item, monthLl);

        month.setAdapter(monthAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valYear = ((String)year.getSelectedItem()).substring(2);
                String valMonth = (String)month.getSelectedItem();

                int intYear = Integer.parseInt((String)year.getSelectedItem());
                int intMonth = Integer.parseInt(valMonth);

                Calendar now = Calendar.getInstance();

                int currentYear = now.get(Calendar.YEAR);
                int currentMonth = now.get(Calendar.MONTH);
                currentMonth++;

                if (intYear == currentYear && intMonth <= currentMonth) {
                    Toast.makeText(AddNewCardActivity.this, "התאריך לא בתוקף", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valYear.length() == 1) {
                    valYear = "0" + valYear;
                }

                if (valMonth.length() == 1) {
                    valMonth = "0" + valMonth;
                }

                String finalDate = valMonth + "/" + valYear;
                etCardExpireDate.setText(finalDate);
                tvCardExpireDate.setText(finalDate);

                dialog.dismiss();
            }
        });
    }
}