package com.example.royidanproject.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowMetrics;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class HorizontalMoving {

    private View view1, view2;
    private boolean a;

    private EngineThread thread;

    private int delay;
    private int jump;

    private int cond;

    public static float dpToPx(Resources r, float dp) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    private static int getMaxWidth(AppCompatActivity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }
    
    public static HorizontalMoving[] createHorizontalMoving(int width, View view1, View view2) {
        final HorizontalMoving[] hm = {null};

        view1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hm[0] = new HorizontalMoving(width, view1, view2);
            }
        });
        return hm;
    }

    public static HorizontalMoving[] createHorizontalMoving(AppCompatActivity activity, View view1, View view2) {
        return createHorizontalMoving(getMaxWidth(activity), view1, view2);
    }

    public HorizontalMoving(int width, View view1, View view2) {
        this.view1 = view1;
        this.view2 = view2;
        this.view1.setX(0);
        this.view2.setX(-width);
        a = true;

        delay = 10;
        jump = 5;

        cond = width / jump;

        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            int i = 0;
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                view1.setX(view1.getX() + jump);
                view2.setX(view2.getX() + jump);
                if (i == cond) {
                    //thread.stopThread();

                    if (a) {
                        view1.setX(-width);
                    } else {
                        view2.setX(-width);
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
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
