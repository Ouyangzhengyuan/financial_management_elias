package pers.elias.financial_management.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static Date date;
    public static SimpleDateFormat simpleDateFormat;

    public static String getSimpleDateTime() {
        date = new Date();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String dateSimpleToString(Date date){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String dateTimeSimpleToString(Date date){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(dateString);
    }

    public static String dateStringSubstring(String dateString){
        return dateString.substring(0, 7);
    }
}
