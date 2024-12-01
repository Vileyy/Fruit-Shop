package com.example.fruit_shop.Utils;

import java.text.DecimalFormat;

public class FormatValues {
    // Method to format price as currency in VND
    public static String formatMoney(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price) + "đ";  // Append "đ" to denote Vietnamese Dong
    }
}
