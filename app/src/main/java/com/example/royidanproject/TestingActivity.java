package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.royidanproject.Threads.TestingThread;
import com.example.royidanproject.Utility.HorizontalMoving;

import java.util.Date;

public class TestingActivity extends AppCompatActivity {

    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        llRoot = findViewById(R.id.llRoot);

        ll = findViewById(R.id.ll);
        ll2 = findViewById(R.id.ll2);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        float layoutWidth_dp = 200f;
        HorizontalMoving horizontalMoving = new HorizontalMoving((int)dpToPx(layoutWidth_dp), ll, ll2);

    }

    private float dpToPx(float dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

}