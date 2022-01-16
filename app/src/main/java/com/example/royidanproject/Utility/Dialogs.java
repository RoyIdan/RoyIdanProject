package com.example.royidanproject.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static Dialog createGetDescriptionDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_get_description, null);
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

        dialog.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)dialog.findViewById(R.id.etTextBox)).setText("");
            }
        });

        return dialog;
    }

    public static Dialog createProductDialog(Context context, Product product) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_product_detailed, null);
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

        createProductDialog_setData(context, dialog, product);

        return dialog;
    }
    private static void createProductDialog_setData(Context context, Dialog dialog, Product product) {
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvType = dialog.findViewById(R.id.tvType);
        TextView tvManufacturer = dialog.findViewById(R.id.tvManufacturer);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView tvStock = dialog.findViewById(R.id.tvStock);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        ImageView ivProductPhoto = dialog.findViewById(R.id.ivProductPhoto);

        TextView[] extras = null;

        if (product instanceof Smartphone) {
            extras = new TextView[] {dialog.findViewById(R.id.tvColor), dialog.findViewById(R.id.tvScreenSize),
                    dialog.findViewById(R.id.tvStorageSize), dialog.findViewById(R.id.tvRamSize)};
            tvType.setText("סמארטפון");
        }

        long manufacturerId = product.getManufacturerId();
        Manufacturer manufacturer = AppDatabase.getInstance(context).manufacturersDao().getManufacturerById(manufacturerId);
        float rating = (float)product.getProductRating();
        Uri uri = ProductImages.getImage(product.getProductPhoto(), context);

        tvTitle.setText(product.getProductName());
        tvManufacturer.setText(manufacturer.getManufacturerName());
        tvPrice.setText(String.valueOf(product.getProductPrice()));
        ratingBar.setRating(rating);
        tvStock.setText(String.valueOf(product.getProductStock()));
        tvDescription.setText(product.getProductDescription());
        ivProductPhoto.setImageURI(uri);

        if (product instanceof Smartphone) {
            extras[0].setText(((Smartphone) product).getPhoneColor().toString());
            extras[1].setText(String.valueOf(((Smartphone) product).getPhoneScreenSize()));
            extras[2].setText(String.valueOf(((Smartphone) product).getPhoneStorageSize()));
            extras[3].setText(String.valueOf(((Smartphone) product).getPhoneRamSize()));
        }
    }

}






















