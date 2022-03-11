package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Threads.TestingThread;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.HorizontalMoving;
import com.example.royidanproject.Utility.UserImages;

import java.util.Date;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class TestingActivity extends AppCompatActivity {

    AppDatabase db;
    SharedPreferences sp;


    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        db = AppDatabase.getInstance(this);
        sp = getSharedPreferences(SP_NAME, 0);


        llRoot = findViewById(R.id.llRoot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView ivPhoto = findViewById(R.id.ivPhoto);
        TextView tvCartItems = findViewById(R.id.tvCartItems);
        long userId = sp.getLong("id", 0);
        if (userId != 0) {
            Users user = db.usersDao().getUserById(userId);
            ivPhoto.setImageURI(UserImages.getImage(user.getUserPhoto(), this));
            tvCartItems.setText("" + db.cartDetailsDao().countByUserId(userId));
        }
        setSupportActionBar(toolbar);



    }

}