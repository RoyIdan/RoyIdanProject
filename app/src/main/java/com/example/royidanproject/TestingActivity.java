package com.example.royidanproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class TestingActivity extends AppCompatActivity {

    AppDatabase db;
    SharedPreferences sp;


    LinearLayout ll;
    LinearLayout ll2;
    LinearLayout llRoot;
    Toolbar toolbar;
    ImageView ivPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        db = AppDatabase.getInstance(this);
        sp = getSharedPreferences(SP_NAME, 0);


        llRoot = findViewById(R.id.llRoot);

        toolbar = findViewById(R.id.toolbar);

        ToolbarManager toolbarManager = new ToolbarManager(TestingActivity.this, toolbar);
    }

}