package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
        ll2 = null;
        TextView tv = findViewById(R.id.tv);
        inflater = LayoutInflater.from(TestingActivity.this);
        a = true;


        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            int i = 0;
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                ll.setX(ll.getX() + 10);

                tv.setText("" + ++i);

                if (i == 76) {
                    if (a) {
                        ll2 = createLL();
                    } else {

                    }
                }

                return true;
            }
        });

        thread = new TestingThread(handler);
        thread.start();

    }

    private LinearLayout createLL() {
        return (LinearLayout) inflater.inflate(R.layout.custom_title, llRoot);
    }
}