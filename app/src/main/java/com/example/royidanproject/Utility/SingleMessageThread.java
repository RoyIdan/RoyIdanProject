package com.example.royidanproject.Utility;

import android.os.Handler;
import android.os.Message;

public class SingleMessageThread extends Thread {
    private Handler handler;
    private int delay;

    public SingleMessageThread(Handler handler, int delay) {
        this.handler = handler;
        this.delay = delay;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        handler.sendMessage(new Message());
    }
}
