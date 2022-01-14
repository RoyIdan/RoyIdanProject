package com.example.royidanproject.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.UserImages;

import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class ProductsAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productsList;
    private LayoutInflater inflater;
    private static final char star_filled = '⭐';
    private static final char star_unfilled = '★';
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
        view = inflater.inflate(R.layout.custom_product, null);

        Product product = productsList.get(i);
        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvProductPrice = view.findViewById(R.id.tvProductPrice);
        TextView tvProductManufacturer = view.findViewById(R.id.tvProductManufacturer);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView tvProductStock = view.findViewById(R.id.tvProductStock);
        ImageView ivProductPhoto = view.findViewById(R.id.ivProductPhoto);
        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);
        Button btnDetailedInfo = view.findViewById(R.id.btnDetailedInfo);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        TextView tvCount = view.findViewById(R.id.tvCount);

        tvProductName.setText(product.getProductName());
        tvProductPrice.setText("מחיר: " + String.valueOf(product.getProductPrice()));
        tvProductManufacturer.setText("יצרן: " + db.manufacturersDao().getManufacturerById(product.getManufacturerId()));
        String rating = "";
        int j = 0, max = (int)(product.getProductRating());
        while (j++ < max) {
            rating += star_filled;
        }
        while (max++ < 5) {
            rating += star_unfilled;
        }
        ratingBar.setRating((float)product.getProductRating());
        tvProductStock.setText("במלאי: " + String.valueOf(product.getProductStock()));
        ivProductPhoto.setImageURI(ProductImages.getImage(product.getProductPhoto(), context));

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Add the item to the cart
                Toast.makeText(context, "Added " + product.getProductName(), Toast.LENGTH_LONG).show();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvCount.getText().toString());
                if (currentVal < product.getProductStock()) {
                    tvCount.setText("" + (currentVal + 1));
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvCount.getText().toString());
                if (currentVal > 0) {
                    tvCount.setText("" + (currentVal - 1));
                }
            }
        });

        // open details when clicking on the product
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createProductDialog(context, product);
            }
        });

        return view;
    }

}

