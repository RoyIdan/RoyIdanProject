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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.IProductDao;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Rating;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.ReceiptActivity;
import com.example.royidanproject.RegisterActivity;
import com.example.royidanproject.UsersActivity;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.UserImages;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.zip.Inflater;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.USERS_FOLDER_NAME;

import androidx.annotation.RequiresApi;

public class ReviewsAdapter extends BaseAdapter {

    private Context context;
    private List<Rating> ratingList;
    private LayoutInflater inflater;
    private AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    SimpleDateFormat sdf;

    public ReviewsAdapter(Context context, List<Rating> ratingList) {
        this.context = context;
        this.ratingList = ratingList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();

        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public int getCount() {
        return ratingList.size();
    }

    @Override
    public Object getItem(int i) {
        return ratingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Rating rating = ratingList.get(i);
        Users user = db.usersDao().getUserById(rating.getUserId());

        view = inflater.inflate(R.layout.custom_review, null);


        TextView tvUserName = view.findViewById(R.id.tvUserName),
                tvDate = view.findViewById(R.id.tvDate),
                tvReview = view.findViewById(R.id.tvReview);

        ImageView ivUserImage = view.findViewById(R.id.ivUserImage);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        tvUserName.setText(user.getUserName() + " " + user.getUserSurname());
        ivUserImage.setImageURI(UserImages.getImage(user.getUserPhoto(), context));

        tvDate.setText(sdf.format(rating.getDateSubmitted()));
        ratingBar.setRating(rating.getRating());

        tvReview.setText(rating.getReview());

        return view;
    }

}
