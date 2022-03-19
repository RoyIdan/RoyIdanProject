package com.example.royidanproject.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class BatteryReceiver extends BroadcastReceiver {

    private TextView tv;

    public BatteryReceiver() {}

    public BatteryReceiver(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);
        tv.setText(level + "%");
    }

}

