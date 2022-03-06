package com.example.royidanproject.Threads;

import android.os.Handler;
import android.os.Message;

import com.example.royidanproject.TestingActivity;

public class TestingThread extends Thread {

    private Handler handler;
    private boolean isRunning;

    public TestingThread(Handler handler) {
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
