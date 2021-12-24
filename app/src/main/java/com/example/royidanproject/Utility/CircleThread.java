package com.example.royidanproject.Utility;

import android.os.Handler;
import android.os.Message;

public class CircleThread extends Thread {
    private Handler handler;

    public CircleThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        int i = 0;

        while (true) {
            if (i < 6) {
                Message message = new Message();
                message.arg1 = i;
                handler.sendMessage(message);
                i++;
            } else {
                Message message = new Message();
                message.arg1 = i;
                handler.sendMessage(message);
                i = 0;
            }

            try {
                sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
