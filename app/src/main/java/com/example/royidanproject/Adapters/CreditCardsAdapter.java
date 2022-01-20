package com.example.royidanproject.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;

public class CreditCardsAdapter extends BaseAdapter {

    private Context context;
    private List<CreditCard> cardList;
    private LayoutInflater inflater;
    private AppDatabase db;
    private SharedPreferences sp;

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






        return view;
    }
}
