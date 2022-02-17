package com.example.royidanproject.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.CartActivity;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.IProductDao;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Rating;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.OrderActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.ReceiptActivity;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.UserImages;

import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class OrderAdapter extends BaseAdapter {

    private Activity context;
    private List<OrderDetails> detailsList;
    private LayoutInflater inflater;
    private AppDatabase db;
    private Filter mFilter;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private double totalPrice;

    public OrderAdapter(Activity context, List<OrderDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        totalPrice = 0;
    }

    public void updateDetailsList(List<OrderDetails> detailsList) {
        this.detailsList = detailsList;
    }

    @Override
    public int getCount() {
        return detailsList.size();
    }

    @Override
    public Object getItem(int i) {
        return detailsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_order_item, null);

        OrderDetails details = detailsList.get(i);

        Product product = null;

        if (details.getTableId() == 1) {
            product = db.smartphonesDao().getSmartphoneById(details.getProductId());
        } else if (details.getTableId() == 2) {
            product = db.watchesDao().getWatchById(details.getProductId());
        } else {
            product = db.accessoriesDao().getAccessoryById(details.getProductId());
        }

        totalPrice += details.getProductOriginalPrice() * details.getProductQuantity();

        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvProductPrice = view.findViewById(R.id.tvProductPrice);
        TextView tvProductManufacturer = view.findViewById(R.id.tvProductManufacturer);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView tvProductQuantity = view.findViewById(R.id.tvProductQuantity);
        ImageView ivProductPhoto = view.findViewById(R.id.ivProductPhoto);
        Button btnDetailedInfo = view.findViewById(R.id.btnDetailedInfo);
        Button btnRate = view.findViewById(R.id.btnRate);
        TextView tvCurrentRating = view.findViewById(R.id.tvCurrentRating);

        tvProductName.setText(product.getProductName());
        tvProductPrice.setText("מחיר: " + CommonMethods.fmt(details.getProductOriginalPrice()));
        tvProductManufacturer.setText("יצרן: " + db.manufacturersDao().getManufacturerById(product.getManufacturerId()));
        ratingBar.setRating((float)product.getProductRating());
        tvProductQuantity.setText("כמות: " + String.valueOf(details.getProductQuantity()));
        ivProductPhoto.setImageURI(ProductImages.getImage(product.getProductPhoto(), context));


        final Product finalProduct = product;
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRateDialog(finalProduct, details, tvCurrentRating, btnRate, ratingBar);

            }
        });

        long userId = sp.getLong("id", 0);
        Rating rating = db.ratingsDao().getByParams(userId, details.getProductId(), details.getTableId());
        if (rating != null) {
            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "לא ניתן לדרג יותר מפעם אחת", Toast.LENGTH_SHORT).show();
                }
            });
            tvCurrentRating.setText("דירוג נוכחי: " + rating.getRating());
        }

        btnDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createProductDialog(context, finalProduct);
            }
        });

        if (i == getCount() - 1) {
            ((OrderActivity)context).onAdapterFinish(totalPrice);
            totalPrice = 0;
        }

        return view;
    }

    private void openRateDialog(Product product, OrderDetails details, TextView tvCurrentRating, Button btnRate, RatingBar ratingBar) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_rate_product_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ((TextView)dialog.findViewById(R.id.tvProductName)).setText(product.getProductName());

        RatingBar rbRatingBar = dialog.findViewById(R.id.rbRatingBar);

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbRatingBar.getRating() == 0) {
                    Toast.makeText(context, "הדירוג חייב להיות בין 1 ל5", Toast.LENGTH_SHORT).show();
                    return;
                }
                IProductDao dao = null;
                if (product instanceof Smartphone)
                    dao = db.smartphonesDao();
                else if(product instanceof Watch)
                    dao = db.watchesDao();
                else
                    dao = db.accessoriesDao();

                int rating = (int)rbRatingBar.getRating();
                dao.addRatingById(product.getProductId(), rating);

                Rating rating1 = new Rating();
                rating1.setUserId(sp.getLong("id", 0));
                rating1.setProductId(product.getProductId());
                rating1.setTableId(details.getTableId());
                rating1.setRating(rating);

                db.ratingsDao().insert(rating1);

                dialog.dismiss();
                tvCurrentRating.setText("דירוג נוכחי: " + rating);
                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "לא ניתן לדרג יותר מפעם אחת", Toast.LENGTH_SHORT).show();
                    }
                });

                double newRating = 0;
                if (product instanceof Smartphone) {
                    newRating = db.smartphonesDao().getSmartphoneById(product.getProductId()).getProductRating();
                } else if (product instanceof Watch) {
                    newRating = db.watchesDao().getWatchById(product.getProductId()).getProductRating();
                } else {
                    newRating = db.accessoriesDao().getAccessoryById(product.getProductId()).getProductRating();
                }

                ratingBar.setRating((float) newRating);

            }
        });
    }
}

