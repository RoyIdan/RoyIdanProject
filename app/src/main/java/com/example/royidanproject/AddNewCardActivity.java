package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class AddNewCardActivity extends AppCompatActivity {
    TextView tvCardNumber, tvCardExpireDate, tvCardHolder;
    EditText etCardNumber, etCardExpireDate, etCardHolder, etCardCvv;

    SharedPreferences sp;

    private void setViewPointers() {
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardExpireDate = findViewById(R.id.tvCardExpireDate);
        tvCardHolder = findViewById(R.id.tvCardHolder);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpireDate = findViewById(R.id.etCardExpireDate);
        etCardHolder = findViewById(R.id.etCardHolder);
        etCardCvv = findViewById(R.id.etCardCvv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        sp = getSharedPreferences(SP_NAME, 0);

        String userName = sp.getString("name", "ERROR");

        etCardHolder.setText(userName);
        tvCardHolder.setText(userName);

    }


    private void buildDateDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        if(etBirthdate.getText().toString().trim().length() != 0)
        {
            try
            {
                cal.setTime(sdf.parse(etBirthdate.getText().toString()));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog picker = new DatePickerDialog(RegisterActivity.this, new RegisterActivity.setDate(), year, month, day);

        Calendar maxCalender = Calendar.getInstance();

        maxCalender.set(Calendar.YEAR, maxCalender.get(Calendar.YEAR));

        picker.getDatePicker().setMaxDate(maxCalender.getTimeInMillis());

        picker.show();
    }

    private class setDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month++;

            String str = day + "/" + month + "/" + year;

            etBirthdate.setText(str);
        }
    }

}