package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.Adapters.UsersAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.USERS_FOLDER_NAME;
import static com.example.royidanproject.Utility.Dialogs.createLoginDialog;

public class UsersActivity extends AppCompatActivity {

    private ListView lvUsers;
    private List<Users> usersList;
    private UsersAdapter adapter;
    Button btnMainActivity;
    AppDatabase db;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();

        btnMainActivity = findViewById(R.id.btnMainActivity);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersActivity.this, MainActivity.class));
                finish();
            }
        });

        db = AppDatabase.getInstance(UsersActivity.this);
        if (sp.getBoolean("admin", false)) {
            usersList = db.usersDao().getAll();
            ((TextView) findViewById(R.id.tvTitle)).setText("דף משתמשים");
        }
        else {
            usersList = new LinkedList<Users>();
            usersList.add(db.usersDao().getUserById(sp.getLong("id", 0l)));
        }
        adapter = new UsersAdapter(UsersActivity.this, usersList);
        lvUsers = findViewById(R.id.lvUsers);
        lvUsers.setAdapter(adapter);

        if (sp.getBoolean("admin", false)) {
            findViewById(R.id.llSearchBar).setVisibility(View.VISIBLE);

            EditText etSearchBox = findViewById(R.id.etSearchBox);

            etSearchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

//            findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String input = etSearchBox.getText().toString().trim();
//
//                    adapter.updateUsersList(db.usersDao().searchByNameOrSurname(input));
//
//                    adapter.notifyDataSetInvalidated();
//                }
//            });
        }

    }

    public void deletePhoto(String photoName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + USERS_FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (photoName.equals(f.getName())) {
                        f.delete();
                    }
                }
            }
        }
    }

    private void toast(String message) {
        Toast.makeText(UsersActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}