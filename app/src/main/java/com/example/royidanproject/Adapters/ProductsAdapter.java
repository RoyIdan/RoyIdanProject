package com.example.royidanproject.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.CartActivity;
import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.ManagerActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.ProductImages;
import com.example.royidanproject.Utility.UserImages;

import java.util.LinkedList;
import java.util.List;

import androidx.sqlite.db.SimpleSQLiteQuery;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class ProductsAdapter extends BaseAdapter implements Filterable {

    private Activity context;
    private List<Product> productsList;
    private List<Product> filteredList;
    private LayoutInflater inflater;
    private ItemFilter mFilter = new ItemFilter();
    private static final char star_filled = '⭐';
    private static final char star_unfilled = '★';
    private AppDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public ProductsAdapter(Activity context, List<Product> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filteredList = productsList;
        this.inflater = LayoutInflater.from(context);
        db = AppDatabase.getInstance(context);
        sp = context.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
    }

    public void updateProductsList(List<Product> newList) {
        productsList = newList;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return filteredList.get(i).getProductId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_product, null);

        Product product = filteredList.get(i);
        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvProductPrice = view.findViewById(R.id.tvProductPrice);
        TextView tvProductManufacturer = view.findViewById(R.id.tvProductManufacturer);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView tvProductStock = view.findViewById(R.id.tvProductStock);
        ImageView ivProductPhoto = view.findViewById(R.id.ivProductPhoto);
        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);
        Button btnDetailedInfo = view.findViewById(R.id.btnDetailedInfo);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        TextView tvCount = view.findViewById(R.id.tvCount);

        tvProductName.setText(product.getProductName());
        tvProductPrice.setText("מחיר: " + String.valueOf(product.getProductPrice()));
        tvProductManufacturer.setText("יצרן: " + db.manufacturersDao().getManufacturerById(product.getManufacturerId()));
//        String rating = "";
//        int j = 0, max = (int)(product.getProductRating());
//        while (j++ < max) {
//            rating += star_filled;
//        }
//        while (max++ < 5) {
//            rating += star_unfilled;
//        }
        ratingBar.setRating((float)product.getProductRating());
        tvProductStock.setText("במלאי: " + String.valueOf(product.getProductStock()));
        ivProductPhoto.setImageURI(ProductImages.getImage(product.getProductPhoto(), context));

        LinearLayout llAddToCartControls = view.findViewById(R.id.llAddToCartControls);
        TextView tvNotLoggedInAlert = view.findViewById(R.id.tvNotLoggedInAlert);
        if (sp.contains("id")) {
            if (product.getProductStock() == 0) {
                tvNotLoggedInAlert.setVisibility(View.VISIBLE);
                llAddToCartControls.setVisibility(View.GONE);
                tvNotLoggedInAlert.setText("המוצר חסר במלאי");
            } else {
                tvNotLoggedInAlert.setVisibility(View.GONE);
                llAddToCartControls.setVisibility(View.VISIBLE);
            }
        } else {
            tvNotLoggedInAlert.setVisibility(View.VISIBLE);
            llAddToCartControls.setVisibility(View.GONE);
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Add the item to the cart
                long userId = sp.getLong("id", 0);
                int quantity = Integer.parseInt(tvCount.getText().toString());
                addToCart(userId, product, quantity);
                context.startActivity(new Intent(context, CartActivity.class));
                Toast.makeText(context, "Added " + product.getProductName(), Toast.LENGTH_LONG).show();
                context.finish();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvCount.getText().toString());
                if (currentVal == 0) {
                    btnMinus.setClickable(true);
                    btnAddToCart.setClickable(true);
                }
                if (currentVal == product.getProductStock() - 1) {
                    btnPlus.setClickable(false);
                }
                tvCount.setText("" + (currentVal + 1));

            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVal = Integer.parseInt(tvCount.getText().toString());
                if (currentVal == product.getProductStock()) {
                    btnPlus.setClickable(true);
                }
                if (currentVal == 1) {
                    btnMinus.setClickable(false);
                    btnAddToCart.setClickable(false);
                }
                tvCount.setText("" + (currentVal - 1));

            }
        });

        // open details when clicking on the product
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // manager dialog
        View finalView = view;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!sp.getBoolean("admin", false)) {
                    return true;
                }

                View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_manager_controls_for_product, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                long tableId = 0;
                if (product instanceof Smartphone) {
                    tableId = 1;
                } else if (product instanceof Watch) {
                    tableId = 2;
                } else {
                    tableId = 3;
                }
                long finalTableId = tableId;

                dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ((TextView) dialog.findViewById(R.id.tvProductName)).setText(product.getProductName());

                dialog.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkIfPurchased(product, finalTableId)) {
                            Toast.makeText(context, "המוצר כבר קיים בסל כלשהו או בהזמנה", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (finalTableId == 1) {
                            db.smartphonesDao().delete((Smartphone) product);
                        } else if (finalTableId == 2) {
                            db.watchesDao().delete((Watch) product);
                        } else {
                            db.accessoriesDao().delete((Accessory) product);
                        }

                        productsList.remove(product);
                        filteredList.remove(product);
                        notifyDataSetInvalidated();

                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ManagerActivity.class);
                        intent.putExtra("productToUpdate", product);
                        intent.putExtra("isProductPurchased", checkIfPurchased(product, finalTableId));
                        context.startActivity(intent);
                    }
                });

                return true;
            }

            private boolean checkIfPurchased(Product product, long tableId) {
                List<Long> list1 = db.orderDetailsDao().isProductExist(product.getProductId(), tableId);
                List<Long> list2 = db.cartDetailsDao().isProductExist(product.getProductId(), tableId);

                return !list1.isEmpty() || !list2.isEmpty();
            }
        });

        btnDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createProductDialog(context, product);
            }
        });

        if (product.getProductStock() == 1) {
            btnPlus.setClickable(false);
        }

        return view;
    }

    private void addToCart(long userId, Product product, int quantity) {
        long productId = product.getProductId();
        CartDetails cd = new CartDetails();
        cd.setUserId(userId);
        cd.setProductId(productId);
        cd.setProductQuantity(quantity);

        if (product instanceof Smartphone) {
            cd.setTableId(1);
            db.smartphonesDao().updateStockById(productId, product.getProductStock() - quantity);
        } else if (product instanceof Watch) {
            cd.setTableId(2);
            db.watchesDao().updateStockById(productId, product.getProductStock() - quantity);
        } else {
            cd.setTableId(3);
            db.accessoriesDao().updateStockById(productId, product.getProductStock() - quantity);
        }

        long tableId = cd.getTableId();
        CartDetails existingDetails = db.cartDetailsDao().getCartDetailsByKeys(userId, productId, tableId);
        if (existingDetails != null) {
            existingDetails.setProductQuantity(existingDetails.getProductQuantity() + quantity);
            db.cartDetailsDao().updateCartDetails(existingDetails);
        } else {
            db.cartDetailsDao().addCartItem(cd);
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Product> list = productsList;

            int count = list.size();
            final List<Product> nlist = new LinkedList<Product>();

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getProductName();
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
            filteredList = (LinkedList<Product>) results.values;
            notifyDataSetChanged();
        }


    }

}

