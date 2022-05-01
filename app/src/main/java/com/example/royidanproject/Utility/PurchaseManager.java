package com.example.royidanproject.Utility;

import static android.Manifest.permission.SEND_SMS;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;
import static com.example.royidanproject.MainActivity.SMS_PHONE;
import static com.example.royidanproject.MainActivity.SP_NAME;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.CartDetails;
import com.example.royidanproject.DatabaseFolder.CreditCard;
import com.example.royidanproject.DatabaseFolder.Order;
import com.example.royidanproject.DatabaseFolder.OrderDetails;
import com.example.royidanproject.DatabaseFolder.Product;
import com.example.royidanproject.OrderActivity;
import com.example.royidanproject.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PurchaseManager {

    private List<CartDetails> detailsList;
    private List<Product> products;
    private Dialog dialog;
    private Context context;
    AppDatabase db;

    private double totalPrice;
    private CreditCard card;

    private TextView tvError;

    private Dialog processDialog;

    private static class ErrorCode {
        private static int NO_CARD_SELECTED = 1;
        private static int CARD_EXPIRED = 2;
        private static int INSUFFICIENT_FUNDS = 3;
    }


    public PurchaseManager(Context context, List<CartDetails> detailsList) {
        this.context = context;

        db = AppDatabase.getInstance(context);

        this.detailsList = detailsList;
        products = getProducts(db, detailsList);

        View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_submit_purchase, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptDialog);
        dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button btnBuy = dialog.findViewById(R.id.btnBuy);
        Spinner spiCreditCard = dialog.findViewById(R.id.spiCreditCard);
        tvError = dialog.findViewById(R.id.tvError);

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        totalPrice = 0;

        for (int i = 0; i < detailsList.size(); i++) {
            totalPrice += products.get(i).getProductPrice() * detailsList.get(i).getProductQuantity();
        }

        setUiData();

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvError.setVisibility(View.GONE);
                ArrayAdapter<CreditCard> adapter = (ArrayAdapter<CreditCard>) spiCreditCard.getAdapter();
                card = (CreditCard) spiCreditCard.getSelectedItem();
                if (adapter.isEmpty()) {
                    setError(ErrorCode.NO_CARD_SELECTED);
                    return;
                }
                if (card.getCardExpireDate().before(new Date())) {
                    setError(ErrorCode.CARD_EXPIRED);
                    return;
                }

                View promptDialog = LayoutInflater.from(context).inflate(R.layout.custom_purchase_completed, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(promptDialog);
                processDialog = alert.create();
                processDialog.setCanceledOnTouchOutside(false);
                processDialog.show();

                LinearLayout ll1 = processDialog.findViewById(R.id.ll1),
                        ll2 = processDialog.findViewById(R.id.ll2);
                SingleMessageThread thread = new SingleMessageThread(null, 3000);
                Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                        proceedToLL2(processDialog);
                        return true;
                    }
                });
                thread.setHandler(handler);
                thread.start();

            }
        });
    }

    private void setError(int errorCode) {

        tvError.setVisibility(View.VISIBLE);

        switch (errorCode) {
            case 1:
                tvError.setText("אין לך כרטיסי אשראי");
                break;
            case 2:
                tvError.setText("הכרטיס פג תוקף");
                break;
            case 3:
                tvError.setText("אין מספיק כסף בכרטיס");
                break;
        }
    }

    private void proceedToLL2(Dialog processDialog) {
        Random random = new Random();
        int number = random.nextInt(999999);

        // this will convert any number sequence into 6 character.
        String pinCode = String.format("%06d", number);

        sendPinCodeSms(pinCode);

        EditText etCode = processDialog.findViewById(R.id.etCode);
        processDialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = etCode.getText().toString().trim();
                if (enteredCode.length() != 6) {
                    etCode.setError("הקוד צריך להכיל 6 ספרות");
                    return;
                }

                if (enteredCode.equals(pinCode)) {
                    LinearLayout ll1 = processDialog.findViewById(R.id.ll1),
                            ll2 = processDialog.findViewById(R.id.ll2),
                            ll3 = processDialog.findViewById(R.id.ll3);

                    ll2.setVisibility(View.GONE);
                    ll1.setVisibility(View.VISIBLE);

                    SingleMessageThread thread = new SingleMessageThread(null, 3000);
                    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            ll1.setVisibility(View.GONE);
                            if (card.getCardBalance() < totalPrice) {
                                processDialog.dismiss();
                                setError(ErrorCode.INSUFFICIENT_FUNDS);
                                return true;
                            }
                            ll3.setVisibility(View.VISIBLE);
                            proceedToLL3(processDialog);
                            return true;
                        }
                    });
                    thread.setHandler(handler);
                    thread.start();
                }
            }
        });
    }

    private void proceedToLL3(Dialog processDialog) {
        Order order = completePurchase();

        String displayMessage = "";
        displayMessage += "הזמנה מספר ";
        displayMessage += order.getOrderId();
        displayMessage += " בוצעה בהצלחה והמוצרים יגיעו אליך לבית בתוך 7 ימי עסקים.";

        ((TextView) processDialog.findViewById(R.id.tvOrderConfirmed)).setText(displayMessage);
        processDialog.findViewById(R.id.btnOrderDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                processDialog.dismiss();

                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();
            }
        });

    }

    private Order completePurchase() {
//        double newBalance = card.getCardBalance() - totalPrice;
//        db.creditCardDao().updateBalanceById(card.getCardId(), newBalance);

        Order order = new Order();
        order.setCustomerId(card.getUserId());
        order.setOrderDatePurchased(new Date());
        order.setCreditCardId(card.getCardId());

        long orderId = db.ordersDao().insert(order);
        order.setOrderId(orderId);

        TransactionManager.makeTransaction(context, TransactionManager.TransactionType.Purchase, card.getUserId(),
                card.getCardId(), totalPrice, "רכישה ברועי סלולר (הזמנה מספר " + orderId + ")");

        List<OrderDetails> orderList = new LinkedList<>();
        for (int i = 0; i < detailsList.size(); i++) {
            CartDetails details = detailsList.get(i);
            OrderDetails od = new OrderDetails();
            od.setOrderId(orderId);
            od.setProductId(details.getProductId());
            od.setTableId(details.getTableId());
            od.setProductOriginalPrice(products.get(i).getProductPrice());
            od.setProductQuantity(details.getProductQuantity());
            orderList.add(od);
        }

        db.orderDetailsDao().insertAll(orderList);

        for (CartDetails detail: detailsList) {
            db.cartDetailsDao().deleteCartDetailsByReference(detail);
        }

        sendMessage(orderId);

        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("order", order);

        return order;
    }

    private void sendPinCodeSms(String pinCode) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuilder builder = new StringBuilder();

        if (ContextCompat.checkSelfPermission((AppCompatActivity) context, SEND_SMS) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                    SEND_SMS
            }, 1);
            sendPinCodeSms(pinCode);
        }

        builder.append("קוד האימות שלך לשימוש ברועי סלולר הינו ");
        builder.append(pinCode);
        builder.append(".");
        builder.append("\n");
        builder.append("קוד זה הינו חד פעמי.");
        builder.append("\n");
        builder.append("בברכה, ");
        switch (card.getCardCompany()) {
            case אמריקן:
                builder.append("אמריקן אקספרס.");
                break;
            case מאסטרכארד:
                builder.append("מאסטרכרד.");
                break;
            case ישראכרט:
                builder.append("ישראכרט.");
                break;
            case לאומי:
                builder.append("לאומי.");
                break;
        }

        String msg = builder.toString();

        ArrayList<String> parts = smsManager.divideMessage(msg);

        smsManager.sendMultipartTextMessage(SMS_PHONE, null, parts, null, null);
    }

    private void setUiData() {
        EditText etName, etPrice;
        Spinner spiCreditCard;

        etName = dialog.findViewById(R.id.etName);
        etPrice = dialog.findViewById(R.id.etPrice);
        spiCreditCard = dialog.findViewById(R.id.spiCreditCard);

        long userId = context.getSharedPreferences(SP_NAME, 0).getLong("id", 0);
        List<CreditCard> cards = AppDatabase.getInstance(context).creditCardDao().getByUserId(userId);

        ArrayAdapter<CreditCard> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, cards);
        spiCreditCard.setAdapter(adapter);

        spiCreditCard.setSelection(0);

        etName.setText(context.getSharedPreferences(SP_NAME, 0).getString("name", "ERROR"));
        etPrice.setText(CommonMethods.fmt(totalPrice));

    }

    private void sendMessage(long orderId) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuilder builder = new StringBuilder();

        if (ContextCompat.checkSelfPermission((AppCompatActivity) context, SEND_SMS) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                    SEND_SMS
            }, 1);
            sendMessage(orderId);
        }

        builder.append("הזמנה מספר ");
        builder.append(orderId);
        builder.append(" אושרה בהצלחה.");
        builder.append("\n");
        builder.append("סכום כולל ששולם: ");
        builder.append(CommonMethods.fmt(totalPrice));

        String msg = builder.toString();

        smsManager.sendTextMessage(SMS_PHONE, null, msg, null, null);
    }

    private List<Product> getProducts(AppDatabase db, List<CartDetails> detailsList) {
        List<Product> products = new ArrayList<>();
        CartDetails cd;
        for (int i = 0; i < detailsList.size(); i++) {
            cd = detailsList.get(i);
            if (cd.getTableId() == 1) {
                products.add(i, db.smartphonesDao().getSmartphoneById(cd.getProductId()));
            } else if (cd.getTableId() == 2) {
                products.add(i, db.watchesDao().getWatchById(cd.getProductId()));
            } else {
                products.add(i, db.accessoriesDao().getAccessoryById(cd.getProductId()));
            }
        }

        return products;
    }
}
