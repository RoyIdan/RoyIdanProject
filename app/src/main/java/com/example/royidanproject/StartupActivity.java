package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.Utility.CircleThread;
import com.example.royidanproject.Utility.StartupThread;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class StartupActivity extends AppCompatActivity {

    private LinearLayout llInitialLayout, llSecondStage;
    private View viewCircle;
    private TextView tvUserName;
    private EditText etPassword;
    private Button btnLogin, btnLogOut;
    StartupThread st;

    private SharedPreferences sp;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        sp = getSharedPreferences(SP_NAME, 0);
        db = AppDatabase.getInstance(StartupActivity.this);

        llInitialLayout = findViewById(R.id.llInitialLayout);
        llSecondStage = findViewById(R.id.llSecondStage);
        viewCircle = findViewById(R.id.viewCircle);
        tvUserName = findViewById(R.id.tvUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogOut = findViewById(R.id.btnLogOut);

        int[] colors = {
                0xFF00FF00, 0xFFD35400, 0xFF2471A3, 0xFF2E86C1, 0xFF17202A, 0xFF5D6D7E, 0xFF17A589
        };

        //TODO - more cubes

        //float defaultX = viewCircle.getX();


        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            int i = 0, stage = 0, countdown = 12;

            @Override
            public boolean handleMessage(@NonNull Message msg) {

                switch (stage) {
                    case 0:
                        viewCircle.setX(viewCircle.getX() + 160);
                        break;
                    case 1:
                        viewCircle.setY(viewCircle.getY() + 160);
                        break;
                    case 2:
                        viewCircle.setX(viewCircle.getX() - 160);
                        break;
                    case 3:
                        viewCircle.setY(viewCircle.getY() - 160);
                        break;
                }

                i++;

                if (i == 4) {
                    i = 0;
                    stage = stage == 3 ?  0 : stage + 1;
                }

//                if (msg.arg1 < 6) {
//                    viewCircle.setX(viewCircle.getX() + 80);
//                } else {
//                    viewCircle.setX(55);
//                }
                viewCircle.setBackgroundColor(colors[i]);

                if (countdown-- == 0) {
                    st.isActive = false;
                    onTimerStop();
                }
                return true;


            }
        });

        st = new StartupThread(handler);
        st.start();

    }

    private void onTimerStop() {
        if (!sp.contains("id")) {
            startActivity(new Intent(StartupActivity.this, MainActivity.class));
            finish();
            return;
        }

        llInitialLayout.setVisibility(View.GONE);
        llSecondStage.setVisibility(View.VISIBLE);
        tvUserName.setText("שלום " + sp.getString("name", "ERROR"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();

                if (db.usersDao().validatePasswordById(sp.getLong("id", -1), password) != 0) {
                    startActivity(new Intent(StartupActivity.this, MainActivity.class));
                    finish();
                    return;
                } else {
                    Toast.makeText(StartupActivity.this, "סיסמה לא נכונה", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().clear().commit();
                startActivity(new Intent(StartupActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}