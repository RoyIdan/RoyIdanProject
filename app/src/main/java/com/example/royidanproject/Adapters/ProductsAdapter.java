package com.example.royidanproject.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class ProductsAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productsList;
    private LayoutInflater inflater;
    private AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public ProductsAdapter(Context context, List<Product> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
    }

    private void deleteUserFromList(Users user) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).equals(user)) {
                productsList.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void updateProductsList(List<Product> newList) {
        productsList = newList;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return productsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return productsList.get(i).getProductId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_users, null);



        return view;
    }

}

