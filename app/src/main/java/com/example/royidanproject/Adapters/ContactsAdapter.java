package com.example.royidanproject.Adapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.SmsManager;
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

import com.example.royidanproject.Custom.Contact;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.SEND_SMS;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;
import static com.example.royidanproject.MainActivity.SMS_PHONE;
import static com.example.royidanproject.MainActivity.SP_NAME;

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern pattern = Pattern.compile("(\\d{10}|\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\) \\d{3}-\\d{4})");
                Matcher matcher = pattern.matcher(model.getContactNumber());
                if (matcher.find()) {
                    System.out.println(matcher.group(0));
                }
                String number = "";
                try {
                    number = matcher.group(0);
                } catch (Exception e) {
                    number = "ERROR";
                }
                Toast.makeText(context, number, Toast.LENGTH_SHORT).show();
                if (number.equals("ERROR")) {
                    Toast.makeText(context, "לא ניתן לקרוא את המספר", Toast.LENGTH_SHORT).show();
                    return;
                }

                SmsManager smsManager = SmsManager.getDefault();

                if (ContextCompat.checkSelfPermission((AppCompatActivity) context, SEND_SMS) == PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                            SEND_SMS
                    }, 1);
                }

                String senderName = context.getSharedPreferences(SP_NAME, 0).getString("name", "");

                String msg = senderName + " הזמין אותך לנסות את רועי סלולר!";

                smsManager.sendTextMessage(number, null, msg, null, null);
            }
        });

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