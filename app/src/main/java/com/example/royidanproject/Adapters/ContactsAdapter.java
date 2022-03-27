package com.example.royidanproject.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.royidanproject.Custom.Contact;
import com.example.royidanproject.R;

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> arrayList;

    public ContactsAdapter(Context context, ArrayList<Contact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {

        return arrayList.size();
    }

    @Override
    public Contact getItem(int position) {

        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Contact model = arrayList.get(position);
        ImageView contactImage;
        TextView contactName, contactNumber, contactEmail, contactOtherDetails;
        convertView = inflater.inflate(R.layout.custom_contact, null);
        contactImage = convertView.findViewById(R.id.contactImage);
        contactName = convertView.findViewById(R.id.contactName);
        contactEmail = convertView.findViewById(R.id.contactEmail);
        contactNumber = convertView.findViewById(R.id.contactNumber);
        contactOtherDetails = convertView.findViewById(R.id.contactOtherDetails);

        // Set items to all view
        contactName.setText(model.getContactName());
        contactEmail.setText(model.getContactEmail());
        contactNumber.setText(model.getContactNumber());
        contactOtherDetails.setText(model.getContactOtherDetails());

        // Bitmap for imageview
        Bitmap image = null;
        if (!model.getContactPhoto().equals("")
                && model.getContactPhoto() != null) {
            image = BitmapFactory.decodeFile(model.getContactPhoto());// decode
            // the
            // image
            // into
            // bitmap
            if (image != null)
                contactImage.setImageBitmap(image);// Set image if bitmap
                // is not null
            else {
                image = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.default_user);// if bitmap is null then set
                // default bitmap image to
                // contact image
                contactImage.setImageBitmap(image);
            }
        } else {
            image = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_user);
            contactImage.setImageBitmap(image);
        }
        return convertView;
    }
}