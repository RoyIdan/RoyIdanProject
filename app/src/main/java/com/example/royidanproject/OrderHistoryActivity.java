package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.royidanproject.Adapters.OrderHistoryAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class OrderHistoryActivity extends AppCompatActivity {
    Button btnMainActivity;
    RadioGroup rgOrderHistory;
    RadioButton rbManagerOnly, rbEveryone;
    EditText etFromDate, etUntilDate, etFilter;
    ListView lvOrderHistory;
    AppDatabase db;
    SharedPreferences sp;

    List<Order> orders;
    OrderHistoryAdapter adapter;

    private void setViewPointers() {
        btnMainActivity = findViewById(R.id.btnMainActivity);
        rgOrderHistory = findViewById(R.id.rgOrderHistory);
        rbManagerOnly = findViewById(R.id.rbManagerOnly);
        rbEveryone = findViewById(R.id.rbEveryone);
        etFromDate = findViewById(R.id.etFromDate);
        etUntilDate = findViewById(R.id.etUntilDate);
        etFilter = findViewById(R.id.etFilter);
        lvOrderHistory = findViewById(R.id.lvOrderHistory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        db = AppDatabase.getInstance(OrderHistoryActivity.this);
        sp = getSharedPreferences(SP_NAME, 0);

        orders = new LinkedList<>();

        setViewPointers();

        if (sp.getBoolean("admin", false)) {
            orders = db.ordersDao().getAll();
        } else {
            long userId = sp.getLong("id", 0);
            orders = db.ordersDao().getByCustomerId(userId);
            rgOrderHistory.setVisibility(View.GONE);
        }

        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orders);
        lvOrderHistory.setAdapter(adapter);


        rgOrderHistory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbManagerOnly.getId()) {
                    long userId = sp.getLong("id", 0);
                    orders = db.ordersDao().getByCustomerId(userId);
                } else {
                    orders = db.ordersDao().getAll();
                }
                adapter.updateList(orders);
                adapter.getFilter().filter(etFilter.getText().toString().trim());
                adapter.notifyDataSetChanged();
            }
        });

        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
            }
        });

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
                    Toast.makeText(OrderHistoryActivity.this, "יש לבחור תאריך התחלה לפני תאריך סיום", Toast.LENGTH_SHORT).show();
                    return;
                }
                buildDateDialog(1);
            }
        });

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = etFilter.getText().toString().trim();
                adapter.getFilter().filter(val);
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        DatePickerDialog picker = new DatePickerDialog(OrderHistoryActivity.this, new OrderHistoryActivity.setDate(view), year, month, day);

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
                adapter.updateList(db.ordersDao().getAll());
                adapter.getFilter().filter(etFilter.getText().toString().trim());
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

        orders = db.ordersDao().getByDateRange(from, until);
        adapter.updateList(orders);
        adapter.getFilter().filter(etFilter.getText().toString().trim());
    }
}