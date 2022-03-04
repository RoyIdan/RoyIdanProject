package com.example.royidanproject.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.royidanproject.Adapters.CreditCardsAdapter;
import com.example.royidanproject.DatabaseFolder.Accessory;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Manufacturer;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.DatabaseFolder.Smartphone;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.DatabaseFolder.Watch;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.OrderActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.ReceiptActivity;
import com.example.royidanproject.Views.CreditCardView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static com.example.royidanproject.MainActivity.ADMIN_PHONE;
import static com.example.royidanproject.MainActivity.SP_NAME;

public class Dialogs {

    public static void createLoginDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_login, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getInstance(context);

                EditText etEmail = dialog.findViewById(R.id.etEmail);
                EditText etPassword = dialog.findViewById(R.id.etPassword);

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!createLoginDialog_validation(etEmail, etPassword)) {
                    return;
                }

                Users user = db.usersDao().getUserByLogin(email, password);
                if (user == null) {
                    ((TextView)dialog.findViewById(R.id.tvError)).setText("Name or password are incorrect.");
                }
                else {
                    SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME, 0).edit();

                    editor.putString("name", user.getUserName() + " " + user.getUserSurname());
                    editor.putString("image", user.getUserPhoto());
                    editor.putLong("id", user.getUserId());
                    editor.putBoolean("admin", user.getUserPhone().equals(ADMIN_PHONE));
                    editor.commit();

                    context.startActivity(new Intent(context, MainActivity.class));
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private static boolean createLoginDialog_validation(EditText etEmail, EditText etPassword) {
        boolean empty = false;
        if (Validator.isEmpty(etEmail.getText().toString().trim())) {
            empty = true;
            etEmail.setError("Please fill this field.");
        }
        if (Validator.isEmpty(etPassword.getText().toString().trim())) {
            empty = true;
            etPassword.setError("Please fill this field.");
        }
        if (empty) {
            return false;
        }

        boolean valid = true;
        String error = "";
        error = Validator.validateEmail(etEmail.getText().toString().trim());
        if (!error.isEmpty()) {
            etEmail.setError(error);
            valid = false;
        }
        error = Validator.validatePassword(etPassword.getText().toString().trim());
        if (!error.isEmpty()) {
            etPassword.setError(error);
            valid = false;
        }

        return valid;
    }

    public static void createAboutDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_about, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static Dialog createGetDescriptionDialog(Context context) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_get_description, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)dialog.findViewById(R.id.etTextBox)).setText("");
            }
        });

        return dialog;
    }

    public static Dialog createProductDialog(Context context, Product product) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_product_detailed, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        createProductDialog_setData(context, dialog, product);

        return dialog;
    }
    private static void createProductDialog_setData(Context context, Dialog dialog, Product product) {
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvType = dialog.findViewById(R.id.tvType);
        TextView tvManufacturer = dialog.findViewById(R.id.tvManufacturer);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView tvStock = dialog.findViewById(R.id.tvStock);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        ImageView ivProductPhoto = dialog.findViewById(R.id.ivProductPhoto);
        LinearLayout llSmartphone = dialog.findViewById(R.id.llSmartphone);
        LinearLayout llWatch = dialog.findViewById(R.id.llWatch);

        TextView[] extras = null;
        long tableId = 0;

        if (product instanceof Smartphone) {
            extras = new TextView[] {dialog.findViewById(R.id.tvColor), dialog.findViewById(R.id.tvScreenSize),
                    dialog.findViewById(R.id.tvStorageSize), dialog.findViewById(R.id.tvRamSize)};
            tvType.setText("סמארטפון");
            llSmartphone.setVisibility(View.VISIBLE);
            tableId = 1;
        } else if (product instanceof Watch) {
            extras = new TextView[] {dialog.findViewById(R.id.tvWatchColor), dialog.findViewById(R.id.tvWatchSize)};
            tvType.setText("שעון");
            llWatch.setVisibility(View.VISIBLE);
            tableId = 2;
        } else if (product instanceof Accessory) {
            tvType.setText("אביזר");
            tableId = 3;
        }

        long manufacturerId = product.getManufacturerId();
        Manufacturer manufacturer = AppDatabase.getInstance(context).manufacturersDao().getManufacturerById(manufacturerId);
        float rating = AppDatabase.getInstance(context).ratingsDao().getAverageByProduct(product.getProductId(), tableId);
        Uri uri = ProductImages.getImage(product.getProductPhoto(), context);

        tvTitle.setText(product.getProductName());
        tvManufacturer.setText(manufacturer.getManufacturerName());
        tvPrice.setText(String.valueOf(product.getProductPrice()));
        ratingBar.setRating(rating);
        tvStock.setText(String.valueOf(product.getProductStock()));
        tvDescription.setText(product.getProductDescription());
        ivProductPhoto.setImageURI(uri);

        if (product instanceof Smartphone) {
            extras[0].setText(((Smartphone) product).getPhoneColor().toString());
            extras[1].setText(String.valueOf(((Smartphone) product).getPhoneScreenSize()));
            extras[2].setText(String.valueOf(((Smartphone) product).getPhoneStorageSize()));
            extras[3].setText(String.valueOf(((Smartphone) product).getPhoneRamSize()));
        } else if (product instanceof Watch) {
            extras[0].setText(((Watch) product).getWatchColor().toString());
            extras[1].setText(String.valueOf(((Watch) product).getWatchSize()));
        }

    }

    public static void createSubmitPurchaseDialog(Context context, List<CartDetails> detailsList) {
        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_submit_purchase, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final long userId = detailsList.get(0).getUserId();

        AppDatabase db = AppDatabase.getInstance(context);
        TextView tvError = dialog.findViewById(R.id.tvError);
        Button btnBuy = dialog.findViewById(R.id.btnBuy);
        Spinner spiCreditCard = dialog.findViewById(R.id.spiCreditCard);

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        double totalPrice = 0;

        Product[] products = new Product[detailsList.size()];
        for (int i = 0; i < detailsList.size(); i++) {
            CartDetails cd = detailsList.get(i);
            products[i] = createSubmitPurchaseDialog_getProduct(db, cd.getProductId(), cd.getTableId());
        }

        for (int i = 0; i < detailsList.size(); i++) {
            totalPrice += products[i].getProductPrice() * detailsList.get(i).getProductQuantity();
        }

        createSubmitPurchaseDialog_setData(context, dialog, totalPrice);


        double finalTotalPrice = totalPrice;
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setVisibility(View.GONE);
                ArrayAdapter<CreditCard> adapter = (ArrayAdapter<CreditCard>) spiCreditCard.getAdapter();
                CreditCard card = (CreditCard) spiCreditCard.getSelectedItem();
                if (adapter.isEmpty()) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("אין לך כרטיסי אשראי");
                    return;
                }
                if (card.getCardExpireDate().before(new Date())) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("הכרטיס פג תוקף");
                    return;
                }
                if (card.getCardBalance() < finalTotalPrice) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("אין מספיק כסף בכרטיס");
                    return;
                }

                double newBalance = card.getCardBalance() - finalTotalPrice;
                db.creditCardDao().updateBalanceById(card.getCardId(), newBalance);

                Order order = new Order();
                order.setCustomerId(userId);
                order.setOrderDatePurchased(new Date());
                order.setCreditCardId(card.getCardId());

                long orderId = db.ordersDao().insert(order);
                order.setOrderId(orderId);

                List<OrderDetails> orderList = new LinkedList<>();
                for (int i = 0; i < detailsList.size(); i++) {
                    CartDetails details = detailsList.get(i);
                    OrderDetails od = new OrderDetails();
                    od.setOrderId(orderId);
                    od.setProductId(details.getProductId());
                    od.setTableId(details.getTableId());
                    od.setProductOriginalPrice(products[i].getProductPrice());
                    od.setProductQuantity(details.getProductQuantity());
                    orderList.add(od);
                }

                db.orderDetailsDao().insertAll(orderList);

                for (CartDetails detail: detailsList) {
                    db.cartDetailsDao().deleteCartDetailsByReference(detail);
                }

                createSubmitPurchaseDialog_sendMessage((Activity) context, orderId, finalTotalPrice);

                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();
            }
        });


    }

    private static void createSubmitPurchaseDialog_sendMessage(Activity activity, long orderId, double totalPrice) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuilder builder = new StringBuilder();

        if (ContextCompat.checkSelfPermission(activity, SEND_SMS) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    SEND_SMS
            }, 1);
            createSubmitPurchaseDialog_sendMessage(activity, orderId, totalPrice);
        }

        builder.append("הזמנה מספר ");
        builder.append(orderId);
        builder.append(" אושרה בהצלחה.");
        builder.append("\n");
        builder.append("סכום כולל ששולם: ");
        builder.append(CommonMethods.fmt(totalPrice));

        String msg = builder.toString();

        smsManager.sendTextMessage("0509254011", null, msg, null, null);
    }

    private static void createSubmitPurchaseDialog_setData(Context context, Dialog dialog, double totalPrice) {
        EditText etName, etPrice;
        Spinner spiCreditCard;

        etName = dialog.findViewById(R.id.etName);
        etPrice = dialog.findViewById(R.id.etPrice);
        spiCreditCard = dialog.findViewById(R.id.spiCreditCard);

        long userId = context.getSharedPreferences(SP_NAME, 0).getLong("id", 0);
        List<CreditCard> cards = AppDatabase.getInstance(context).creditCardDao().getByUserId(userId);

//        BaseAdapter adapter = new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return cards.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return cards.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return 0;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                CreditCard card = cards.get(position);
//                long userId = card.getUserId();
//                Users user = AppDatabase.getInstance(context).usersDao().getUserById(userId);
//
//                CreditCardView ccv = new CreditCardView(context, null);
//
//                ccv.setLayoutParams(params);
//
//                ccv.setCardNumber(card.getCardNumber());
//                ccv.setCardExpireDate(card.getCardExpireDate());
//                ccv.setCardHolder(user.getUserName());
//                ccv.setCardCompany(card.getCardCompany());
//
//                return ccv;
//            }
//        };

        ArrayAdapter<CreditCard> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, cards);
        spiCreditCard.setAdapter(adapter);

        spiCreditCard.setSelection(0);

        etName.setText(context.getSharedPreferences(SP_NAME, 0).getString("name", "ERROR"));
        etPrice.setText(CommonMethods.fmt(totalPrice));

    }

    private static Product createSubmitPurchaseDialog_getProduct(AppDatabase db, long productId, long tableId) {
        Product product;

        if (tableId == 1) {
            product = db.smartphonesDao().getSmartphoneById(productId);
        } else if (tableId == 2) {
            product = db.watchesDao().getWatchById(productId);
        } else {
            product = db.accessoriesDao().getAccessoryById(productId);
        }

        return product;
    }

}






















