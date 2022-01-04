package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.example.royidanproject.Utility.CircleThread;
import com.example.royidanproject.Utility.StartupThread;

public class StartupActivity extends AppCompatActivity {

    private View viewCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        viewCircle = findViewById(R.id.viewCircle);

        int[] colors = {
                0xFF00FF00, 0xFFD35400, 0xFF2471A3, 0xFF2E86C1, 0xFF17202A, 0xFF5D6D7E, 0xFF17A589
        };

        //TODO - more cubes

        //float defaultX = viewCircle.getX();

        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            int i = 0, stage = 0;

            @Override
            public boolean handleMessage(@NonNull Message msg) {

                switch (stage) {
                    case 0:
                        viewCircle.setX(viewCircle.getX() + 160);
                        break;
                    case 1:
                        viewCircle.setY(viewCircle.getY() + 160);
                        break;
                    case 2:
                        viewCircle.setX(viewCircle.getX() - 160);
                        break;
                    case 3:
                        viewCircle.setY(viewCircle.getY() - 160);
                        break;
                }

                i++;

                if (i == 4) {
                    i = 0;
                    stage = stage == 3 ?  0 : stage + 1;
                }

//                if (msg.arg1 < 6) {
//                    viewCircle.setX(viewCircle.getX() + 80);
//                } else {
//                    viewCircle.setX(55);
//                }
                viewCircle.setBackgroundColor(colors[i]);
                return true;
            }
        });

        StartupThread st = new StartupThread(handler);
        st.start();

    }
}