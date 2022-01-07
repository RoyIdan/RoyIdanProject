package com.example.royidanproject.Utility;

import android.os.Handler;
import android.os.Message;

public class StartupThread extends Thread {
    private Handler handler;
    public boolean isActive = true;

    public StartupThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        while (isActive) {
            handler.sendMessage(new Message());

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
