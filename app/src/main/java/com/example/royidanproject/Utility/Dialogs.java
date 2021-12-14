package com.example.royidanproject.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;

import java.text.SimpleDateFormat;

import static com.example.royidanproject.MainActivity.ADMIN_PHONE;
import static com.example.royidanproject.MainActivity.SP_NAME;

public class Dialogs {

    public static void createLoginDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_login, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getInstance(context);

                EditText etEmail = dialog.findViewById(R.id.etEmail);
                EditText etPassword = dialog.findViewById(R.id.etPassword);

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!createLoginDialog_validation(etEmail, etPassword)) {
                    return;
                }

                Users user = db.usersDao().getUserByLogin(email, password);
                if (user == null) {
                    ((TextView)dialog.findViewById(R.id.tvError)).setText("Name or password are incorrect.");
                }
                else {
                    SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME, 0).edit();

                    editor.putString("name", user.getUserName() + " " + user.getUserSurname());
                    editor.putString("image", user.getUserPhoto());
                    editor.putLong("id", user.getUserId());
                    editor.putBoolean("admin", user.getUserPhone().equals(ADMIN_PHONE));
                    editor.commit();

                    context.startActivity(new Intent(context, MainActivity.class));
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private static boolean createLoginDialog_validation(EditText etEmail, EditText etPassword) {
        boolean empty = false;
        if (Validator.isEmpty(etEmail.getText().toString().trim())) {
            empty = true;
            etEmail.setError("Please fill this field.");
        }
        if (Validator.isEmpty(etPassword.getText().toString().trim())) {
            empty = true;
            etPassword.setError("Please fill this field.");
        }
        if (empty) {
            return false;
        }

        boolean valid = true;
        String error = "";
        error = Validator.validateEmail(etEmail.getText().toString().trim());
        if (!error.isEmpty()) {
            etEmail.setError(error);
            valid = false;
        }
        error = Validator.validatePassword(etPassword.getText().toString().trim());
        if (!error.isEmpty()) {
            etPassword.setError(error);
            valid = false;
        }

        return valid;
    }

    public static void createAboutDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_about, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}