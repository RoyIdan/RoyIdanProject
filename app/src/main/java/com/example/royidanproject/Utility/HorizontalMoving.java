package com.example.royidanproject.Utility;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class HorizontalMoving {

    private LinearLayout ll1, ll2;
    private boolean a;

    EngineThread thread;

    int cond;

    public static float dpToPx(Resources r, float dp) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    public HorizontalMoving(int width, LinearLayout ll1, LinearLayout ll2) {
        this.ll1 = ll1;
        this.ll2 = ll2;
        this.ll1.setX(0);
        this.ll2.setX(-width);
        a = true;

        cond = width / 10;

        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            int i = 0;
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                ll1.setX(ll1.getX() + 10);
                ll2.setX(ll2.getX() + 10);

                if (i == cond) {
                    //thread.stopThread();

                    if (a) {
                        ll1.setX(-width);
                    } else {
                        ll2.setX(-width);
                    }
                    i = 0;
                    a = !a;
                }

                i++;

                return true;
            }
        });

        thread = new EngineThread(handler);
        thread.start();
    }

    public void stop() {
        thread.stopThread();
    }



    private class EngineThread extends Thread {

        private Handler handler;
        private boolean isRunning;

        public EngineThread(Handler handler) {
            this.handler = handler;
            isRunning = true;
        }

        public void stopThread() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {

                handler.sendMessage(new Message());

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
