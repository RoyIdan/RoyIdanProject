package com.example.royidanproject.Utility;

import android.content.res.Resources;
import android.util.TypedValue;

import java.text.DecimalFormat;

public class CommonMethods {


    public static String fmt(double d) {
//        if(d == (long) d)
//            return "₪" + String.format("%d",(long)d);
//        else
//            return "₪" + new DecimalFormat("#.##").format(d);

        return CurrencyManager.formatPrice(d);
    }

    public static float dpToPx(Resources r, float dp) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

}
