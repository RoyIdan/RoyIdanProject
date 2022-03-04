package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.example.royidanproject.Threads.CircleThread;

public class ThreadActivity extends AppCompatActivity {

    private View viewCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        viewCircle = findViewById(R.id.viewCircle);

        int[] colors = {
                0xFF00FF00, 0xFFD35400, 0xFF2471A3, 0xFF2E86C1, 0xFF17202A, 0xFF5D6D7E, 0xFF17A589
        };


        //float defaultX = viewCircle.getX();

        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.arg1 < 6) {
                    viewCircle.setX(viewCircle.getX() + 150);
                } else {
                    viewCircle.setX(20);
                }
                viewCircle.setBackgroundColor(colors[msg.arg1]);
                return true;
            }
        });

        CircleThread ct = new CircleThread(handler);
        ct.start();
    }


}