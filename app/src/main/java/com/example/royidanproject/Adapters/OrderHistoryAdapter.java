package com.example.royidanproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.OrderActivity;
import com.example.royidanproject.OrderHistoryActivity;
import com.example.royidanproject.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.royidanproject.MainActivity.SP_NAME;
import static com.example.royidanproject.Utility.CommonMethods.fmt;

public class OrderHistoryAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Order> orderList;
    private List<Order> filteredList;
    private LayoutInflater inflater;
    private OrderHistoryFilter filter;
    AppDatabase db;
    SharedPreferences sp;
    SimpleDateFormat sdf;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        filteredList = orderList;
        inflater = LayoutInflater.from(context);
        filter = new OrderHistoryFilter();
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void updateList(List<Order> orderList) {
        this.orderList = orderList;

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
        return filteredList.get(position).getOrderId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Order order = filteredList.get(position);

        List<OrderDetails> detailsList = db.orderDetailsDao().getByOrderId(order.getOrderId());

        view = inflater.inflate(R.layout.custom_order_history_item, null);

        TextView tvOrderNumber = view.findViewById(R.id.tvOrderNumber),
                tvOrderDate = view.findViewById(R.id.tvOrderDate),
                tvOrderCustomer = view.findViewById(R.id.tvOrderCustomer),
                tvOrderItemsCount = view.findViewById(R.id.tvOrderItemsCount),
                tvOrderTotalCost = view.findViewById(R.id.tvOrderTotalCost);

        double totalCost = 0;

        for (OrderDetails details : detailsList) {
            totalCost += details.getProductOriginalPrice() * details.getProductQuantity();
        }

        Users customer = db.usersDao().getUserById(order.getCustomerId());

        tvOrderNumber.setText(String.valueOf(order.getOrderId()));
        tvOrderDate.setText(sdf.format(order.getOrderDatePurchased()));
        tvOrderCustomer.setText(customer.getUserName() + " " + customer.getUserSurname());
        tvOrderItemsCount.setText(String.valueOf(detailsList.size()));
        tvOrderTotalCost.setText(fmt(totalCost));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class OrderHistoryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Order> list = orderList;

            int count = list.size();
            final List<Order> nlist = new LinkedList<Order>();

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = String.valueOf(list.get(i).getOrderId());
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (LinkedList<Order>) results.values;
            notifyDataSetChanged();
        }

    }
}
