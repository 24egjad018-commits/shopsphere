package com.shopsphere.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nDemo {

    public static void main(String[] args) {
        Locale[] locales = { Locale.of("en", "IN"), Locale.of("hi", "IN") };

        for (Locale locale : locales) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

            System.out.println("Locale: " + locale);
            System.out.println("  Text:     " + bundle.getString("nav.cart") + " | " + bundle.getString("product.add"));
            System.out.println("  Price:    " + NumberFormat.getCurrencyInstance(locale).format(1234567.5));
            System.out.println("  Date:     " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(new Date()));
        }
    }
}