package com.example.royidanproject.BroadcastReceivers;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.royidanproject.R;
import com.example.royidanproject.Views.WifiView;

public class WifiStatusReceiver extends BroadcastReceiver {

    private WifiView wifiOn;
    private ImageView wifiOff;
    Dialog dialog;

    public WifiStatusReceiver() {}

    public WifiStatusReceiver(WifiView wifiOn, ImageView wifiOff) {
        this.wifiOn = wifiOn;
        this.wifiOff = wifiOff;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (dialog == null) {
            dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.custom_no_internet);
        }
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi.isAvailable() || mobile.isAvailable()) && isOnline(context)) {
            wifiOff.setVisibility(View.GONE);
            wifiOn.setVisibility(View.VISIBLE);
            wifiOn.startAnim();
            if (dialog != null) {
                dialog.dismiss();
            }
        } else {
            wifiOn.setVisibility(View.GONE);
            wifiOn.stopAnim();
            wifiOff.setVisibility(View.VISIBLE);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}