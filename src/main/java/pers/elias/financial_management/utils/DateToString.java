package pers.elias.financial_management.utils;

import java.util.Date;

public class DateToString {
    public static String dateToString(Date date){
        return String.format("%tF", date);
    }
}
