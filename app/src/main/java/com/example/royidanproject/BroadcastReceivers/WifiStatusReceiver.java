package com.example.royidanproject.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.royidanproject.Views.WifiView;

public class WifiStatusReceiver extends BroadcastReceiver {

    private WifiView wifiOn;
    private ImageView wifiOff;
    TextView tv;

    public WifiStatusReceiver() {}

    public WifiStatusReceiver(WifiView wifiOn, ImageView wifiOff) {
        this.wifiOn = wifiOn;
        this.wifiOff = wifiOff;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);

        if (isConnected) {
            wifiOff.setVisibility(View.GONE);
            wifiOn.setVisibility(View.VISIBLE);
            wifiOn.startAnim();
        } else {
            wifiOn.setVisibility(View.GONE);
            wifiOn.stopAnim();
            wifiOff.setVisibility(View.VISIBLE);
        }
    }

}