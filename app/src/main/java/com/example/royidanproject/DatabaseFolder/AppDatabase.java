package com.example.royidanproject.DatabaseFolder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class,
        Smartphone.class,
        Watch.class,
        Accessory.class,
        Manufacturer.class,
        Rating.class,
        Order.class,
        OrderDetails.class,
        CartDetails.class,
        CreditCard.class,
        Transaction.class}
        , version = 1)
@androidx.room.TypeConverters({TypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract UsersDao usersDao();
    public abstract SmartphonesDao smartphonesDao();
    public abstract WatchesDao watchesDao();
    public abstract AccessoriesDao accessoriesDao();
    public abstract ManufacturersDao manufacturersDao();
    public abstract RatingsDao ratingsDao();
    public abstract CartDetailsDao cartDetailsDao();
    public abstract OrdersDao ordersDao();
    public abstract OrderDetailsDao orderDetailsDao();
    public abstract CreditCardDao creditCardDao();
    public abstract TransactionsDao transactionsDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
