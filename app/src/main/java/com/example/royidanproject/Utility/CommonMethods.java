package com.example.royidanproject.Utility;

import java.text.DecimalFormat;

public class CommonMethods {


    public static String fmt(double d)
    {
        if(d == (long) d)
            return "₪" + String.format("%d",(long)d);
        else
            return "₪" + new DecimalFormat("#.##").format(d);
    }

}
