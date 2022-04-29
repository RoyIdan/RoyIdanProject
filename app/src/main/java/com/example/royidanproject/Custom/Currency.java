package com.example.royidanproject.Custom;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class Currency {
    private String name;
    private char symbol;
    private double exchangeRate; // shekel to it's value exchange rate.

    public Currency() {}

    public Currency(String name, char symbol, double exchangeRate) {
        this.name = name;
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public char getSymbol() {
        return symbol;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + symbol + ") " + name;
    }


    public static List<Currency> getCurrencies() {
        List<Currency> currencies = new LinkedList<>();
        currencies.add(new Currency("שקל חדש", '₪', 1));
        currencies.add(new Currency("דולר אמריקאי", '$', 0.3));
        currencies.add(new Currency("אירו", '€', 0.29));
        return currencies;
    }

    public static Currency getCurrency(int index) {
        return getCurrencies().get(index);
    }
}
