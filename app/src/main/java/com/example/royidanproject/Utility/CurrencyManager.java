package com.example.royidanproject.Utility;

import java.text.DecimalFormat;

public class CurrencyManager {

    private class Currency {
        //private String name;
        private char symbol;
        private double exchangeRate; // shekel to it's value exchange rate.

        private double getExchangeRate() {
            return exchangeRate;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    private static Currency currentCurrency;

    public static Currency getCurrentCurrency() {
        return currentCurrency;
    }

    public static void setCurrentCurrency(Currency currentCurrency) {
        CurrencyManager.currentCurrency = currentCurrency;
    }

    public static String formatPrice(double priceInShekel) {
        double priceInCurr = priceInShekel * currentCurrency.getExchangeRate();
        char symbol = currentCurrency.getSymbol();

        if(priceInCurr == (long) priceInCurr)
            return symbol + String.format("%d",(long)priceInCurr);
        else
            return symbol + new DecimalFormat("#.##").format(priceInCurr);

    }
}
