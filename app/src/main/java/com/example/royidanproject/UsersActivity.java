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
import android.widget.Toast;

import com.example.royidanproject.Adapters.UsersAdapter;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.FOLDER_NAME;
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
            }
        });

        db = AppDatabase.getInstance(UsersActivity.this);
        if (sp.getBoolean("admin", false)) {
            usersList = db.usersDao().getAll();
        }
        else {
            usersList = new LinkedList<Users>();
            usersList.add(db.usersDao().getUserById(sp.getLong("id", 0l)));
        }
        adapter = new UsersAdapter(UsersActivity.this, usersList);
        lvUsers = findViewById(R.id.lvUsers);
        lvUsers.setAdapter(adapter);;

        if (sp.getBoolean("admin", false)) {
            findViewById(R.id.llSearchBar).setVisibility(View.VISIBLE);

            EditText etSearchBox = findViewById(R.id.etSearchBox);

            etSearchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        adapter.updateUsersList(usersList);
                    } else {
                        adapter.updateUsersList(db.usersDao().searchByNameOrSurname(s.toString()));
                    }
                    adapter.notifyDataSetInvalidated();
                }
            });

            findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = etSearchBox.getText().toString().trim();

                    adapter.updateUsersList(db.usersDao().searchByNameOrSurname(input));

                    adapter.notifyDataSetInvalidated();
                }
            });
        }

    }

    public void deletePhoto(String photoName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem miLogin = menu.findItem(R.id.menu_login);
        MenuItem miLogout = menu.findItem(R.id.menu_logout);
        MenuItem miRegister = menu.findItem(R.id.menu_register);

        if (sp.contains("name")) {
            miLogout.setVisible(true);
            miLogin.setVisible(false);
            miRegister.setVisible(false);
        } else {
            miLogout.setVisible(false);
            miLogin.setVisible(true);
            miRegister.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_register:
                startActivity(new Intent(UsersActivity.this, RegisterActivity.class));
                toast("reg");
                break;
            case R.id.menu_login:
                createLoginDialog(UsersActivity.this);
                toast("in");
                break;
            case R.id.menu_logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(UsersActivity.this, MainActivity.class));
                toast("out");
                break;
            default:
                int a = 1/0;
        }

        return true;
    }

    private void toast(String message) {
        Toast.makeText(UsersActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}