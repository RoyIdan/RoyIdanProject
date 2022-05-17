package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.royidanproject.Adapters.OrderHistoryAdapter;
import com.example.royidanproject.Adapters.TransactionsHistoryAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Transaction;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.TransactionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class TransactionsHistoryActivity extends AppCompatActivity {
    EditText etFromDate, etUntilDate;
    Spinner spiTransactionsType;
    ListView lvTransactions;
    AppDatabase db;
    SharedPreferences sp;

    List<Transaction> transactions;
    TransactionsHistoryAdapter adapter;
    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

        toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        toolbarManager.onDestroy();
    }

    private void setViewPointers() {
//        btnMainActivity = findViewById(R.id.btnMainActivity);
        //rgOrderHistory = findViewById(R.id.rgOrderHistory);
//        rbManagerOnly = findViewById(R.id.rbManagerOnly);
//        rbEveryone = findViewById(R.id.rbEveryone);
        etFromDate = findViewById(R.id.etFromDate);
        etUntilDate = findViewById(R.id.etUntilDate);
//        etFilter = findViewById(R.id.etFilter);
        spiTransactionsType = findViewById(R.id.spiTransactionsType);
        lvTransactions = findViewById(R.id.lvTransactions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_history);

        db = AppDatabase.getInstance(TransactionsHistoryActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(TransactionsHistoryActivity.this, toolbar);

        setViewPointers();

        LinkedList<String> transactionTypes = new LinkedList<>();
        transactionTypes.add("הכל");
        transactionTypes.add("רכישות");
        transactionTypes.add("הפקדות");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                TransactionsHistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, transactionTypes);
        spiTransactionsType.setAdapter(adapter);
        spiTransactionsType.setSelection(0);

        transactions = db.transactionsDao().getTransactionsByUserId(sp.getLong("id", 0));

        this.adapter = new TransactionsHistoryAdapter(TransactionsHistoryActivity.this, transactions);
        lvTransactions.setAdapter(this.adapter);


        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDateDialog(0);
            }
        });

        etUntilDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFromDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(TransactionsHistoryActivity.this, "יש לבחור תאריך התחלה לפני תאריך סיום",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                buildDateDialog(1);
            }
        });

//        etFilter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String val = etFilter.getText().toString().trim();
//                adapter.getFilter().filter(val);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        spiTransactionsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long userId = sp.getLong("id", 0);
                String from = etFromDate.getText().toString();
                String until = etUntilDate.getText().toString();
                if (!from.isEmpty() && !until.isEmpty()) {
                    filterAdapterByDate();
                } else {
                    if (position == 0) {
                        transactions = db.transactionsDao().getTransactionsByUserId(userId);
                    } else if (position == 1) {
                        transactions = db.transactionsDao().getPurchasesByUserId(userId);
                    } else {
                        transactions = db.transactionsDao().getDepositsByUserId(userId);
                    }
                }
                TransactionsHistoryActivity.this.adapter.updateList(transactions);
                TransactionsHistoryActivity.this.adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void buildDateDialog(int view) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        EditText etView = view == 0 ? etFromDate : etUntilDate;

        if(etView.getText().toString().trim().length() != 0)
        {
            try
            {
                cal.setTime(sdf.parse(etView.getText().toString()));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        int year;
        int month;
        int day;

        Calendar calendar = null;

        if (view == 0) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            String formatted = etFromDate.getText().toString().trim();
            Date date = null;
            try {
                date = sdf.parse(formatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month++;
        }

        DatePickerDialog picker = new DatePickerDialog(TransactionsHistoryActivity.this, new TransactionsHistoryActivity.setDate(view), year, month, day);

        Calendar maxCalender = Calendar.getInstance();

        maxCalender.set(Calendar.YEAR, maxCalender.get(Calendar.YEAR));

        picker.getDatePicker().setMaxDate(maxCalender.getTimeInMillis());

        if (view == 1) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

            picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }

        picker.show();
    }

    private class setDate implements DatePickerDialog.OnDateSetListener {
        private EditText etView;
        private int view;

        public setDate(int view) {
            etView = view == 0 ? etFromDate : etUntilDate;
            this.view = view;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month++;

            String str = day + "/" + month + "/" + year;

            etView.setText(str);

            if (view == 1) {
                filterAdapterByDate();
            } else {
                etUntilDate.setText("");
                long userId = sp.getLong("id", 0);
                if (spiTransactionsType.getSelectedItemPosition() == 0) {
                    adapter.updateList(db.transactionsDao().getTransactionsByUserId(userId));
                } else if (spiTransactionsType.getSelectedItemPosition() == 1) {
                    adapter.updateList(db.transactionsDao().getPurchasesByUserId(userId));
                } else {
                    adapter.updateList(db.transactionsDao().getDepositsByUserId(userId));
                }
            }
        }
    }

    public void filterAdapterByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String strFrom = etFromDate.getText().toString().trim();
        String strUntil = etUntilDate.getText().toString().trim();

        Date from = null, until = null;
        try {
            from = sdf.parse(strFrom);
            until = sdf.parse(strUntil);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);

        until.setHours(23);
        until.setMinutes(59);
        until.setSeconds(59);

        long userId = sp.getLong("id", 0);
        if (spiTransactionsType.getSelectedItemPosition() == 0) {
            transactions = db.transactionsDao().getTransactionsByDateRange(userId, from, until);
        } else if (spiTransactionsType.getSelectedItemPosition() == 1) {
            transactions = db.transactionsDao().getPurchasesByDateRange(userId, from, until);
        } else {
            transactions = db.transactionsDao().getDepositsByDateRange(userId, from, until);
        }
        TransactionsHistoryActivity.this.adapter.updateList(transactions);
        TransactionsHistoryActivity.this.adapter.notifyDataSetChanged();
    }
}