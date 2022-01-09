package com.example.royidanproject.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.royidanproject.MainActivity.PRODUCTS_FOLDER_NAME;
import static com.example.royidanproject.MainActivity.USERS_FOLDER_NAME;

public class ProductImages {
    public static Uri getImage(String _filename, Context context) {
//        File file = new File(Environment.getExternalStorageDirectory() + "/" + PRODUCTS_FOLDER_NAME);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        if (file != null && file.isDirectory()) {
//            File[] files = file.listFiles();
//            if (files != null) {
//                for (File f : files) {
//                    if (f.getName().equals(_filename)) {
//                        return BitmapFactory.decodeFile(f.getPath());
//                    }
//                }
//            }
//        }


        File folder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            folder = new File(context.getExternalFilesDir(null) + "/" + PRODUCTS_FOLDER_NAME);
        }
        else {
            folder = new File(Environment.getExternalStorageDirectory() + "/" + PRODUCTS_FOLDER_NAME);
        }
        Uri uri = Uri.parse("file://" + folder + "/" + _filename);
        return uri == null ? null : uri;
    }

    public static void deletePhoto(String _filename, Context context) {
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(context.getExternalFilesDir(null) + "/" + PRODUCTS_FOLDER_NAME);
        }
        else {
            file = new File(Environment.getExternalStorageDirectory() + "/" + PRODUCTS_FOLDER_NAME);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().equals(_filename)) {
                        f.delete();
                    }
                }
            }
        }
    }

    public static String savePhoto(Bitmap bitmap, Context context) {
        String _file = new SimpleDateFormat("yyMMdd-HH:mm:ss").format(new Date());
        _file += ".jpg";
        File folder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            folder = new File(context.getExternalFilesDir(null) + "/" + PRODUCTS_FOLDER_NAME);
        }
        else {
            folder = new File(Environment.getExternalStorageDirectory() + "/" + PRODUCTS_FOLDER_NAME);
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, _file);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
