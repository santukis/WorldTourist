package com.david.worldtourist.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String DAY_MONTH_YEAR = "dd-MM-yyyy";
    public static final String HOUR_MINUTES = "k:mm";

    public static String getFormattedDate(String stringDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date;

        try {
            date =  dateFormat.parse(stringDate);

        } catch (ParseException exception) {
            return stringDate;
        }

        return dateFormat.format(date);
    }

    public static String getConvertedDate(String stringDate) {
        DateFormat originalFormat = new SimpleDateFormat(YEAR_MONTH_DAY, Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat(DAY_MONTH_YEAR, Locale.getDefault());
        Date date;

        try{
            date = originalFormat.parse(stringDate);

        } catch (ParseException exception) {
            return stringDate;
        }

        return targetFormat.format(date);
    }

    public static String getFormattedDistance(double distance) {
        final String METERS = " m";
        final String KILOMETERS = " km";

        if (distance < 1000) {
            return String.format(Locale.getDefault(), "%.1f", distance).concat(METERS);

        } else {
            return String.format(Locale.getDefault(),"%.1f", distance / 1000).concat(KILOMETERS);
        }
    }

}