package com.example.royidanproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Transaction;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.OrderActivity;
import com.example.royidanproject.OrderHistoryActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.Utility.TransactionManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.Utility.CommonMethods.fmt;

public class TransactionsHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> transactionsList;
    private LayoutInflater inflater;
    AppDatabase db;
    SharedPreferences sp;
    SimpleDateFormat sdf;

    public TransactionsHistoryAdapter(Context context, List<Transaction> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
        inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void updateList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return transactionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return transactionsList.get(position).getTransactionId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Transaction transaction = transactionsList.get(position);

        view = inflater.inflate(R.layout.custom_transactions_history_item, null);

        TextView tvTransactionNumber = view.findViewById(R.id.tvTransactionNumber),
                tvTransactionDate = view.findViewById(R.id.tvTransactionDate),
                tvTransactionType = view.findViewById(R.id.tvTransactionType),
                tvTransactionCardNumber = view.findViewById(R.id.tvTransactionCardNumber),
                tvTransactionAmount = view.findViewById(R.id.tvTransactionAmount),
                tvTransactionDescription = view.findViewById(R.id.tvTransactionDescription);

        String cardNumber = db.creditCardDao().getById(transaction.getCreditCardId()).getCardNumber();
        
        tvTransactionNumber.setText(transaction.getTransactionId() + "");
        tvTransactionDate.setText(sdf.format(transaction.getTransactionDate()));
        tvTransactionType.setText(transaction.getTransactionType() == TransactionManager.TransactionType.Purchase ? "רכישה" : "הפקדה");
        tvTransactionCardNumber.setText("xxxx-xxxx-xxxx-" + cardNumber.substring(12));
        tvTransactionAmount.setText(fmt(transaction.getTransactionAmount()));
        tvTransactionDescription.setText(transaction.getTransactionDescription());

        return view;
    }
}
