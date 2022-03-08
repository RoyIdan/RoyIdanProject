package com.example.royidanproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.GalleryActivity;
import com.example.royidanproject.ManagerActivity;
import com.example.royidanproject.ManufacturerActivity;
import com.example.royidanproject.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ManufacturerAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Manufacturer> manufacturerList;
    private List<Manufacturer> filteredList;
    private LayoutInflater inflater;
    private ManufacturerFilter mFilter;

    public ManufacturerAdapter(Context context, List<Manufacturer> manufacturerList) {
        this.context = context;
        this.manufacturerList = manufacturerList;
        this.filteredList = manufacturerList;
        this.inflater = LayoutInflater.from(context);
        this.mFilter = new ManufacturerFilter();
    }

    public void updateList(List<Manufacturer> manufacturerList, @Nullable String filter) {
        this.manufacturerList = manufacturerList;
        if (filter != null) {
            mFilter.performFiltering(filter);
        } else {
            mFilter.performFiltering("");
        }
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.custom_manufacturer, null);

        Manufacturer manufacturer = (Manufacturer) getItem(position);

        String name = manufacturer.getManufacturerName();

        TextView tvManufacturerName = view.findViewById(R.id.tvManufacturerName);
        Button btnDelete = view.findViewById(R.id.btnDelete),
                btnViewProducts = view.findViewById(R.id.btnViewProducts);

        tvManufacturerName.setText(name);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_delete_prompt, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.show();

                ((TextView) dialog.findViewById(R.id.tvTitle)).setText("אישור מחיקת יצרן");
                ((TextView) dialog.findViewById(R.id.tvTitle2)).setText("האם אתה בטוח שאתה רוצה למחוק את היצרן?");

                dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manufacturerList.remove(manufacturer);
                        filteredList.remove(manufacturer);
                        notifyDataSetInvalidated();
                        ((ManagerActivity) context).updateManufacturerList(manufacturerList);
                        dialog.dismiss();
                    }
                });
            }
        });

        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "toasty", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ManufacturerActivity.class);
                intent.putExtra("manufacturerId", manufacturer.getManufacturerId());
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();

            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ManufacturerFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filter = constraint.toString().trim().toLowerCase(Locale.ROOT);

            FilterResults results = new FilterResults();

            List<Manufacturer> tempList = new LinkedList<>();

            Manufacturer temp;
            for (int i = 0; i < manufacturerList.size(); i++) {
                temp = manufacturerList.get(i);
                if (temp.getManufacturerName().toLowerCase(Locale.ROOT).contains(filter)) {
                    tempList.add(temp);
                }
            }

            results.values = tempList;
            results.count = tempList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<Manufacturer>) results.values;
            notifyDataSetInvalidated();
        }
    }

}
