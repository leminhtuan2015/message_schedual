package com.leminhtuan.myapplication.message_schedual.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leminhtuan on 3/16/16.
 */
public class DateConverter {
    public static String FORMAT_1 = "EEE MMM dd hh:mm:ss Z yyyy";

    public static Date parse(String dateString, String format) {
        Date date = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_1);
            date = dateFormat.parse(dateString);
//            Log.d("Parse date OK", dateFormat.parse(dateString) + "");
        } catch (Exception e) {
            Log.d("Error", "Date parse error!'");
        }

        return date;
    }

    public static String parse(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy - HH:mm");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
