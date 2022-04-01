package com.example.royidanproject.Adapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.royidanproject.Custom.Contact;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

public class ContactsAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Contact> contactsList;
    private List<Contact> filteredList;
    private LayoutInflater inflater;
    private ContactsAdapter.ContactsFilter mFilter;

    public ContactsAdapter(Context context, ArrayList<Contact> arrayList) {
        this.context = context;
        this.contactsList = arrayList;
        this.filteredList = arrayList;
        mFilter = new ContactsFilter();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return filteredList.size();
    }

    @Override
    public Contact getItem(int position) {

        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact model = filteredList.get(position);
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

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ContactsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String str = constraint.toString().trim();

            FilterResults results = new FilterResults();

            final List<Contact> list = contactsList;
            int count = list.size();

            String filterableString;

            final List<Contact> newList = new LinkedList<>();

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getContactName();
                if (filterableString.toLowerCase().contains(str)) {
                    newList.add(list.get(i));
                }
            }

            results.values = newList;
            results.count = count;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }
}