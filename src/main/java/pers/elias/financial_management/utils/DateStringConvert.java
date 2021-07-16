package pers.elias.financial_management.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringConvert {
    public static String dateToString(Date date){
        return String.format("%tF", date);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(dateString);
    }
}
