package com.example.royidanproject.Utility;

import android.app.Application;

import com.example.royidanproject.Application.RoyIdanProject;
import com.example.royidanproject.Custom.Currency;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

public class CurrencyManager {

    private static Currency currentCurrency;

    public static Currency getCurrentCurrency() {
        return currentCurrency;
    }

    public static void setCurrentCurrency(Currency currentCurrency) {
        CurrencyManager.currentCurrency = currentCurrency;
    }

    public static String formatPrice(double priceInShekel) {
        if (currentCurrency == null) {
            currentCurrency = new Currency("שקל חדש", '₪', 1);
        }

        double priceInCurr = priceInShekel * currentCurrency.getExchangeRate();
        char symbol = currentCurrency.getSymbol();

        if(priceInCurr == (long) priceInCurr)
            return symbol + String.format("%d",(long)priceInCurr);
        else
            return symbol + new DecimalFormat("#.##").format(priceInCurr);

    }


}
