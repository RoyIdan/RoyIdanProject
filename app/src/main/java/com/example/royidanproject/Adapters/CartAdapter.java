package com.example.royidanproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.CartActivity;
import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.IProductDao;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.RegisterActivity;
import com.example.royidanproject.UsersActivity;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.TransactionManager;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.zip.Inflater;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.USERS_FOLDER_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CartDetails> detailsList;
    private LayoutInflater inflater;
    private AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private double totalPrice;

    public double getTotalPrice() {return totalPrice;}

    public CartAdapter(Context context, List<CartDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        totalPrice = 0;
    }

    public void updateDetailsList(List<CartDetails> newList) {
        detailsList = newList;
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
        return detailsList.get(i).getUserId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CartDetails details = detailsList.get(i);
        Product product = null;
        IProductDao productDao = null;

        // [Use if tables shares the increment id]
//        Smartphone sp = db.smartphonesDao().getSmartphoneById(details.getProductId());
//        if (sp != null) {
//            product = sp;
//            productDao = db.smartphonesDao();
//        } else {
//            Watch watch = db.watchesDao().getWatchById(details.getProductId());
//            if (watch != null) {
//                product = watch;
//                productDao = db.watchesDao();
//            } else {
//                Accessory accessory = db.accessoriesDao().getAccessoryById(details.getProductId());
//                if (accessory != null) {
//                    product = accessory;
//                } else {
//                    String.valueOf(1 / 0); // crash if item is not found
//                }
//            }
//        }

        if (details.getTableId() == 1) {
            product = db.smartphonesDao().getSmartphoneById(details.getProductId());
            productDao = db.smartphonesDao();
        } else if (details.getTableId() == 2) {
            product = db.watchesDao().getWatchById(details.getProductId());
            productDao = db.watchesDao();
        } else {
            product = db.accessoriesDao().getAccessoryById(details.getProductId());
            productDao = db.accessoriesDao();
        }

        totalPrice += product.getProductPrice() * details.getProductQuantity();

        view = inflater.inflate(R.layout.custom_cart_item, null);


        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvProductQuantity = view.findViewById(R.id.tvProductQuantity);
        TextView tvProductPrice = view.findViewById(R.id.tvProductPrice);
        ImageView ivProductPhoto = view.findViewById(R.id.ivProductPhoto);

        //TextView tvDiscountedPrice = view.findViewById(R.id.tvDiscountedPrice);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        Button btnRemove = view.findViewById(R.id.btnRemove);
        Button btnBuy = view.findViewById(R.id.btnBuy);

        tvProductName.setText(product.getProductName());
        tvProductQuantity.setText(String.valueOf(details.getProductQuantity()));
        //tvProductPrice.setText(String.valueOf(product.getProductPrice()));
        tvProductPrice.setText(fmt(product.getProductPrice()));
        ivProductPhoto.setImageURI(ProductImages.getImage(product.getProductPhoto(), context));

        final Product p1 = product;
        IProductDao finalProductDao = productDao;
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvProductQuantity.getText().toString());
                if (p1.getProductStock() > 0) {
                    tvProductQuantity.setText(String.valueOf(currentVal + 1));
                    p1.setProductStock(p1.getProductStock() - 1);
                    finalProductDao.updateStockById(p1.getProductId(), p1.getProductStock());
                    details.setProductQuantity(currentVal + 1);
                    db.cartDetailsDao().updateCartDetails(details);

                    totalPrice += p1.getProductPrice();
                    ((CartActivity) context).setTotalPrice(totalPrice);
                    //((CartActivity) context).addToTotalPrice(p1.getProductPrice());
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvProductQuantity.getText().toString());
                if (currentVal > 0) {
                    tvProductQuantity.setText(String.valueOf(currentVal - 1));
                    p1.setProductStock(p1.getProductStock() + 1);
                    finalProductDao.updateStockById(p1.getProductId(), p1.getProductStock());
                    details.setProductQuantity(currentVal - 1);
                    db.cartDetailsDao().updateCartDetails(details);

                    totalPrice -= p1.getProductPrice();
                    ((CartActivity) context).setTotalPrice(totalPrice);

                    if (currentVal == 1) {
                        db.cartDetailsDao().deleteCartDetailsByReference(details);
                        detailsList.remove(details);
                        totalPrice = 0;
                        notifyDataSetInvalidated();
                    }


//                    ((CartActivity) context).removeFromTotalPrice(p1.getProductPrice());
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentVal = Integer.parseInt(tvProductQuantity.getText().toString());
                p1.setProductStock(p1.getProductStock() + currentVal);
                finalProductDao.updateStockById(p1.getProductId(), p1.getProductStock());
                db.cartDetailsDao().deleteCartDetailsByReference(details);
                detailsList.remove(details);

                totalPrice -= p1.getProductPrice() * details.getProductQuantity();
                ((CartActivity) context).setTotalPrice(totalPrice);

                totalPrice = 0;
                notifyDataSetInvalidated();
                //((CartActivity) context).removeFromTotalPrice(p1.getProductPrice() * details.getProductQuantity());
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CartDetails> list = new LinkedList<>();
                list.add(details);
                //Dialogs.createSubmitPurchaseDialog(context, list);
                new TransactionManager(context, list);
            }
        });

        if (i == getCount() - 1) {
            if (context instanceof CartActivity) {
                ((CartActivity) context).onAdapterFinish(totalPrice);
            }
        }

        return view;
    }

    @NonNull
    public static String fmt(double d)
    {
        if(d == (long) d)
            return "₪" +  String.format("%d",(long)d);
        else
            return "₪" + new DecimalFormat("#.##").format(d);
    }

}
