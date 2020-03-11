package br.com.calcard.android.app.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyHelper {
    private static Locale ptBR = new Locale("pt", "BR");

    private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(ptBR);

    private CurrencyHelper() {
    }

    public static String format(double value) {
        return currencyFormat.format(value);
    }
}
