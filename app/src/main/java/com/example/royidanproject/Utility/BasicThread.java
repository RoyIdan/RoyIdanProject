package com.example.royidanproject.Utility;

import android.os.Handler;
import android.os.Message;

public class BasicThread extends Thread {

    private Handler handler;
    private int delay;
    private boolean isRunning;

    public BasicThread(Handler handler, int delay) {
        this.handler = handler;
        this.delay = delay;
    }

    public void stopThread() {
        isRunning = false;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        isRunning = true;

        while (isRunning) {
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendMessage(new Message());
        }
    }
}
