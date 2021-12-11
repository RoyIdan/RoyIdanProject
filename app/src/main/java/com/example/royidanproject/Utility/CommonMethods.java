package com.example.royidanproject.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.royidanproject.MainActivity.FOLDER_NAME;

public class CommonMethods {
    public static Bitmap getImage(String _filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().equals(_filename)) {
                        return BitmapFactory.decodeFile(f.getPath());
                    }
                }
            }
        }
        return null;
    }

    public static void deletePhoto(String _filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
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

    public static String savePhoto(Bitmap bitmap) {
            String _file = new SimpleDateFormat("yyMMdd-HH:mm:ss").format(new Date());
            _file += ".jpg";
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
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
