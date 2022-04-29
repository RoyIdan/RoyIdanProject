package com.example.royidanproject;

import static com.example.royidanproject.MainActivity.SMS_PHONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.royidanproject.Application.RoyIdanProject;
import com.example.royidanproject.Custom.Currency;
import com.example.royidanproject.Utility.CurrencyManager;
import com.example.royidanproject.Utility.ToolbarManager;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

        toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        toolbarManager.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(SettingsActivity.this, toolbar);

        SharedPreferences sp = getSharedPreferences("SP_SETTINGS", 0);

        SharedPreferences.Editor editor = sp.edit();
        List<RoyIdanProject.Song> songs = Arrays.asList(RoyIdanProject.Song.values());
        ArrayAdapter<RoyIdanProject.Song> songsAdapter = new ArrayAdapter<RoyIdanProject.Song>(SettingsActivity.this,
                R.layout.support_simple_spinner_dropdown_item, songs);
        Spinner spiSongs = findViewById(R.id.spiMusic);
        RoyIdanProject app1 = RoyIdanProject.getInstance();
        spiSongs.setAdapter(songsAdapter);
        spiSongs.setSelection(app1.currentSong.ordinal(), false);
        spiSongs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RoyIdanProject.Song song = RoyIdanProject.Song.values()[position];
                app1.currentSong = song;
                editor.putInt("musicId", song.ordinal()).commit();
                if (app1.isMusicServiceRunning) {
                    app1.restartMusicService();
                    toolbar.findViewById(R.id.ivMusicOff).setVisibility(View.GONE);
                    toolbar.findViewById(R.id.ivMusicOn).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText etSmsPhone = findViewById(R.id.etSmsPhone);
        etSmsPhone.setText(SMS_PHONE);
        etSmsPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SMS_PHONE = s.toString().trim();
            }
        });

        Currency currency = new Currency();

        List<Currency> currencies = Currency.getCurrencies();
        ArrayAdapter<Currency> currencyAdapter = new ArrayAdapter<Currency>(SettingsActivity.this,
                R.layout.support_simple_spinner_dropdown_item, currencies);
        Spinner spiCurrency = findViewById(R.id.spiCurrency);
        spiCurrency.setAdapter(currencyAdapter);
        spiCurrency.setSelection(sp.getInt("currencyId", 0), false);
        spiCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyManager.setCurrentCurrency(currencies.get(position));
                editor.putInt("currencyId", position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // https://www.codespeedy.com/create-real-time-currency-converter-app-in-android-studio/

    }
}