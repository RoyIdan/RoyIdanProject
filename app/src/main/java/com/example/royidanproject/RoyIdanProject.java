package com.example.royidanproject;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.StartupThread;

public class RoyIdanProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
    }
}
