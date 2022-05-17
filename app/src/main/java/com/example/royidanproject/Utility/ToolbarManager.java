package com.example.royidanproject.Utility;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.royidanproject.Application.RoyIdanProject;
import com.example.royidanproject.BroadcastReceivers.BatteryReceiver;
import com.example.royidanproject.BroadcastReceivers.WifiStatusReceiver;
import com.example.royidanproject.CartActivity;
import com.example.royidanproject.CreditCardsActivity;
import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.MainActivity;
import com.example.royidanproject.OrderHistoryActivity;
import com.example.royidanproject.R;
import com.example.royidanproject.RegisterActivity;
import com.example.royidanproject.Services.MusicService;
import com.example.royidanproject.TransactionsHistoryActivity;
import com.example.royidanproject.UsersActivity;
import com.example.royidanproject.Views.WifiView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.royidanproject.MainActivity.SP_NAME;

public class ToolbarManager {

    private AppCompatActivity mActivity;
    private Toolbar toolbar;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppDatabase db;

    private ImageView ivPhoto, ivHome;

    private TextView tvCartItems;

    private WifiView wifiView;
    private ImageView ivWifiOff;
    private WifiStatusReceiver wifiStatusReceiver;
    private IntentFilter intentFilter;

    private ImageView ivMusicOn, ivMusicOff;


    private boolean isGuest;

    public ToolbarManager(AppCompatActivity activity, Toolbar toolbar) {
        mActivity = activity;
        this.toolbar = toolbar;

        mActivity.setSupportActionBar(toolbar);

        sp = mActivity.getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        db = AppDatabase.getInstance(mActivity);

        ivPhoto = toolbar.findViewById(R.id.ivPhoto);
        ivHome = toolbar.findViewById(R.id.ivHome);

        tvCartItems = toolbar.findViewById(R.id.tvCartItems);

        wifiView = toolbar.findViewById(R.id.wifi);
        ivWifiOff = toolbar.findViewById(R.id.ivWifiOff);

        ivMusicOn = toolbar.findViewById(R.id.ivMusicOn);
        ivMusicOff = toolbar.findViewById(R.id.ivMusicOff);

        long userId = sp.getLong("id", 0);
        isGuest = userId == 0;

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class);
            }
        });

        if (!isGuest) {
            Users user = db.usersDao().getUserById(userId);

            ivPhoto.setImageURI(UserImages.getImage(user.getUserPhoto(), mActivity));

            int itemsCount = db.cartDetailsDao().countByUserId(userId);
            tvCartItems.setText(String.valueOf(itemsCount));
            toolbar.findViewById(R.id.ivCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemsCount > 0) {
                        startActivity(CartActivity.class);
                    } else {
                        toast("הסל הזמני שלך ריק");
                    }
                }
            });
        } else {
            toolbar.findViewById(R.id.rlCartAndCountHolder).setVisibility(View.GONE);
        }
        ivPhoto.setOnClickListener(this::showPopup);

        if (RoyIdanProject.getInstance().isMusicServiceRunning) {
            ivMusicOn.setVisibility(View.VISIBLE);
        } else {
            ivMusicOff.setVisibility(View.VISIBLE);
        }
        ivMusicOn.setOnClickListener(v -> changeMusicState(false));
        ivMusicOff.setOnClickListener(v -> changeMusicState(true));
    }

    public void onResume() {
        wifiStatusReceiver = new WifiStatusReceiver(wifiView, ivWifiOff);
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mActivity.registerReceiver(wifiStatusReceiver, intentFilter);
    }

    public void onDestroy() {
        mActivity.unregisterReceiver(wifiStatusReceiver);
    }

    private void changeMusicState(boolean newState) {
        RoyIdanProject.getInstance().isMusicServiceRunning = newState;
        if (newState) {
            ivMusicOff.setVisibility(View.GONE);
            ivMusicOn.setVisibility(View.VISIBLE);
            Intent intent = new Intent(mActivity, MusicService.class);
            intent.putExtra("isRunning", true);
            mActivity.startService(intent);
        } else {
            ivMusicOn.setVisibility(View.GONE);
            ivMusicOff.setVisibility(View.VISIBLE);
            Intent intent = new Intent(mActivity, MusicService.class);
            mActivity.stopService(intent);
        }
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(mActivity, v);
        MenuInflater inflater = popup.getMenuInflater();
        if (v == ivPhoto) {
            if (isGuest) {
                inflater.inflate(R.menu.menu_guest, popup.getMenu());
                popup.setOnMenuItemClickListener(new GuestMenuListener());
            } else {
                inflater.inflate(R.menu.menu_user, popup.getMenu());
                popup.setOnMenuItemClickListener(new UserMenuListener());
            }
        }
        popup.show();


    }

    class GuestMenuListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_register:
                    startActivity(RegisterActivity.class);
                    return true;
                case R.id.menu_login:
                    Dialogs.createLoginDialog(mActivity);
                    return true;
                default:
                    return false;
            }
        }
    }

    class UserMenuListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_user_screen:
                    startActivity(UsersActivity.class);
                    return true;
                case R.id.menu_logout:
                    editor.clear().commit();
                    startActivity(MainActivity.class);
                    return true;
                case R.id.menu_order_history:
                    if (hasOrderHistory()) {
                        startActivity(OrderHistoryActivity.class);
                    } else {
                        toast("היסטוריית ההזמנות שלך ריקה");
                    }
                    return true;
                case R.id.menu_cards:
                    startActivity(CreditCardsActivity.class);
                    return true;
                case R.id.menu_transactions_history:
                    startActivity(TransactionsHistoryActivity.class);
                    return true;
                default:
                    return false;
            }
        }

        boolean hasOrderHistory() {
            if (sp.getBoolean("admin", false)) {
                return db.ordersDao().hasAny();
            } else {
                long userId = sp.getLong("id", 0);
                return db.ordersDao().hasAny(userId) != null;
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(mActivity, activity);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}
