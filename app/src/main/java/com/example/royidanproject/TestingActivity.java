package com.example.royidanproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.example.royidanproject.Adapters.ContactsAdapter;
import com.example.royidanproject.Custom.Contact;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;
import com.example.royidanproject.Views.WifiView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
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

        ((WifiView) findViewById(R.id.wifi)).startAnim();


        llRoot = findViewById(R.id.llRoot);

        toolbar = findViewById(R.id.toolbar);

        ToolbarManager toolbarManager = new ToolbarManager(TestingActivity.this, toolbar);

        ActivityCompat.requestPermissions(TestingActivity.this, new String[]{READ_CONTACTS}, 0);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<Contact> contacts = readContacts();
        ContactsAdapter adapter = new ContactsAdapter(TestingActivity.this, contacts);
        ((ListView) findViewById(R.id.contact_listview)).setAdapter(adapter);
    }

    @SuppressLint("Range")
    private ArrayList<Contact> readContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return
        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID"));
                Uri dataUri = ContactsContract.Data.CONTENT_URI;
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);

                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String photoPath = "" + R.drawable.default_user; // Photo path
                byte[] photoByte = null;// Byte to get photo since it will come
                // in BLOB
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                String contactNumbers = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";

                // Now start the cusrsor
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get
                    // the
                    // contact
                    // name
                    do {
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor
                                    .getColumnIndex("data1")); // Get Nick Name
                            contactOtherDetails += "NickName : " + nickName
                                    + "n";// Add the nick name to string

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers += "Home Phone : " + homePhone
                                            + "n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers += "Work Phone : " + workPhone
                                            + "n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers += "Mobile Phone : "
                                            + mobilePhone + "n";
                                    break;

                            }
                        }
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactEmailAddresses += "Home Email : "
                                            + homeEmail + "n";
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactEmailAddresses += "Work Email : "
                                            + workEmail + "n";
                                    break;

                            }
                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                            companyName = dataCursor.getString(dataCursor
                                    .getColumnIndex("data1"));// get company
                            // name
                            contactOtherDetails += "Coompany Name : "
                                    + companyName + "n";
                            title = dataCursor.getString(dataCursor
                                    .getColumnIndex("data4"));// get Company
                            // title
                            contactOtherDetails += "Title : " + title + "n";

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                            photoByte = dataCursor.getBlob(dataCursor
                                    .getColumnIndex("data15")); // get photo in
                            // byte

                            if (photoByte != null) {

                                // Now make a cache folder in file manager to
                                // make cache of contacts images and save them
                                // in .png
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        photoByte, 0, photoByte.length);
                                File cacheDirectory = getBaseContext()
                                        .getCacheDir();
                                File tmp = new File(cacheDirectory.getPath()
                                        + "/_androhub" + contctId + ".png");
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(
                                            tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG,
                                            100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                photoPath = tmp.getPath();// finally get the
                                // saved path of
                                // image
                            }

                        }

                    } while (dataCursor.moveToNext()); // Now move to next
                    // cursor

                    contactList.add(new Contact(Long.toString(contctId),
                            displayName, contactNumbers, contactEmailAddresses,
                            photoPath, contactOtherDetails));// Finally add
                    // items to
                    // array list
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }

}