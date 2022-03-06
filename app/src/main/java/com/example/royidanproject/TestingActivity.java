package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.royidanproject.Threads.TestingThread;

import java.util.Date;

public class TestingActivity extends AppCompatActivity {

    TestingThread thread;
    LayoutInflater inflater;
    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;
    boolean a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        llRoot = findViewById(R.id.llRoot);

        ll = findViewById(R.id.ll);
        ll2 = findViewById(R.id.ll2);
        TextView tv = findViewById(R.id.tv);
        inflater = LayoutInflater.from(TestingActivity.this);
        a = true;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ll2.setX(-width);


        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            int i = 0;
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                ll.setX(ll.getX() + 10);
                ll2.setX(ll2.getX() + 10);

                tv.setText("" + ++i);

                if (i == 107) {
                    thread.stopThread();
                    if (1 > 0) {
                        return true;
                    }
                    if (a) {
                        ll.setX(-width);
                    } else {
                        ll2.setX(-width);
                    }
                    i = 0;
                    a = !a;
                }


                return true;
            }
        });

        thread = new TestingThread(handler);
        thread.start();

    }

    private void resetLL(LinearLayout ll) {
        ll.setX(-200);
    }
}