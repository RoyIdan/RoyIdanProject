package com.example.royidanproject.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class OrderAdapter extends BaseAdapter {

    private Activity context;
    private List<OrderDetails> detailsList;
    private LayoutInflater inflater;
    private AppDatabase db;
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

        Product product;

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

        float averageRating = db.ratingsDao().getAverageByProduct(details.getProductId(), details.getTableId());

        tvProductName.setText(product.getProductName());
        tvProductPrice.setText("????????: " + CommonMethods.fmt(details.getProductOriginalPrice()));
        tvProductManufacturer.setText("????????: " + db.manufacturersDao().getManufacturerById(product.getManufacturerId()));
        ratingBar.setRating(averageRating);
        tvProductQuantity.setText("????????: " + String.valueOf(details.getProductQuantity()));
        ivProductPhoto.setImageURI(ProductImages.getImage(product.getProductPhoto(), context));


        final Product finalProduct = product;
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRateDialog(finalProduct, details, tvCurrentRating, btnRate, ratingBar);
            }
        });

        long userId = db.ordersDao().getCustomerIdByOrderId(details.getOrderId());
        Rating rating = db.ratingsDao().getByParams(userId, details.getProductId(), details.getTableId());
        if (rating != null) {
            btnRate.setText("?????? ????????????");
            btnRate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openReviewDialog(product, rating, details, tvCurrentRating, btnRate, ratingBar);
                }
            });
            tvCurrentRating.setText("?????????? ??????????: " + rating.getRating());
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

        EditText etReview = dialog.findViewById(R.id.etReview);

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = (int)rbRatingBar.getRating();
                String review = etReview.getText().toString().trim();

                if (rating == 0) {
                    Toast.makeText(context, "???????????? ???????? ?????????? ?????? 1 ??5", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (review.split("\r\n|\r|\n").length > 4) {
                    Toast.makeText(context, "???????????? ?????????? ???? ?????????? ?????????? ???????? ??4 ??????????", Toast.LENGTH_SHORT).show();
                    return;
                }

                Rating rating1 = new Rating();
                rating1.setUserId(sp.getLong("id", 0));
                rating1.setProductId(product.getProductId());
                rating1.setTableId(details.getTableId());
                rating1.setRating(rating);
                rating1.setDateSubmitted(new Date());
                if (!review.isEmpty()) {
                    rating1.setReview(review);
                }

                db.ratingsDao().insert(rating1);

                dialog.dismiss();
                tvCurrentRating.setText("?????????? ??????????: " + rating);
                btnRate.setText("?????? ????????????");
                btnRate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openReviewDialog(product, rating1, details, tvCurrentRating, btnRate, ratingBar);
                    }
                });

                double newRating = db.ratingsDao().getAverageByProduct(details.getProductId(), details.getTableId());

                ratingBar.setRating((float) newRating);

            }
        });
    }

    private void openReviewDialog(@NonNull Product product, @NonNull Rating rating, OrderDetails details,
                                  TextView tvCurrentRating, Button btnRate, RatingBar ratingBar) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_rate_product_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        ((TextView)dialog.findViewById(R.id.tvProductName)).setText(product.getProductName());

        ((TextView)dialog.findViewById(R.id.etReview)).setHint("");

        RatingBar rbRatingBar = dialog.findViewById(R.id.rbRatingBar);

        EditText etReview = dialog.findViewById(R.id.etReview);

        rbRatingBar.setIsIndicator(true);
        etReview.setFocusable(false);

        rbRatingBar.setRating((float) rating.getRating());
        etReview.setText(rating.getReview());

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setText("?????? ??????????");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.ratingsDao().delete(rating);
                dialog.dismiss();

                tvCurrentRating.setText("?????????? ??????????: ??????");
                btnRate.setText("??????");
                btnRate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                btnRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openRateDialog(product, details, tvCurrentRating, btnRate, ratingBar);
                    }
                });

                double newRating = db.ratingsDao().getAverageByProduct(details.getProductId(), details.getTableId());

                ratingBar.setRating((float) newRating);
            }
        });
    }
}

