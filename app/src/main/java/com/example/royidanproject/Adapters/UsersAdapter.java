package com.example.royidanproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.RegisterActivity;
import com.example.royidanproject.UsersActivity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.MainActivity.FOLDER_NAME;

public class UsersAdapter extends BaseAdapter {

    private Context context;
    private List<Users> usersList;
    private LayoutInflater inflater;
    private AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public UsersAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
    }

    private void deleteUserFromList(Users user) {
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).equals(user)) {
                usersList.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void updateUsersList(List<Users> newList) {
        usersList = newList;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int i) {
        return usersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return usersList.get(i).getUserId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Users user = usersList.get(i);
        String photoName = user.getUserPhoto();
        view = inflater.inflate(R.layout.custom_users, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp.getBoolean("admin", false) && sp.getLong("id", 0) == user.getUserId()) {
                    Toast.makeText(context, "The admin cannot be deleted.", Toast.LENGTH_LONG).show();
                    return;
                }
                View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_delete_prompt, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((UsersActivity)context).deletePhoto(photoName);
                        deleteUserFromList(user);
                        db.usersDao().deleteUserByReference(user);
                        if (sp.getLong("id", 0l) == user.getUserId()) {
                            editor.clear();
                            editor.commit();
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                        ((UsersActivity) context).invalidateOptionsMenu();

                        dialog.dismiss();
                    }
                });

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("user", user);

                context.startActivity(intent);
                return true;
            }
        });
        TextView tvUserBirthday = view.findViewById(R.id.tvUserBirthday);
        TextView tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvUserPhone = view.findViewById(R.id.tvUserPhone);
        ImageView ivPhoto = view.findViewById(R.id.ivPhoto);

//        long ageInDays = TimeUnit.MILLISECONDS.toDays(new Date().getTime() - user.getUserBirthdate().getTime());
//        double ageInYears =  ageInDays * 0.00273790926;
//        double ageInMonths =  ageInDays / 30.436875;
//
//        String age = Math.round(ageInYears) + ", " + Math.round(ageInMonths / 12);

        Date age = new Date();
        Date birthdate = user.getUserBirthdate();
        age.setYear(age.getYear() - birthdate.getYear());
        age.setMonth(age.getMonth() - birthdate.getMonth());
        if (age.getMonth() < 0) {
            age.setYear(age.getYear() - 1);
            age.setMonth(age.getMonth() + 12);
        }

        String strDate = age.getYear() + " | " + age.getMonth();

        tvUserBirthday.setText(new SimpleDateFormat("dd/MM/yyyy").format(user.getUserBirthdate()) + " | " + strDate);
        tvUserName.setText(user.getUserName()+ " " + user.getUserSurname());
        tvUserPhone.setText(user.getUserPhone());
        tvUserEmail.setText(user.getUserEmail());

        File folder = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
        Uri uri = Uri.parse("file://" + folder + "/" + photoName);
        ivPhoto.setImageURI(uri);
        return view;
    }

}
