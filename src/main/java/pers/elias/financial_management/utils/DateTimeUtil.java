package pers.elias.financial_management.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static Date date;
    public static SimpleDateFormat simpleDateFormat;

    /**
     * 获取简单的日期时间格式
     */
    public static String getSimpleDateTime() {
        date = new Date();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * Date 类型日期转 String 类型日期
     */
    public static String dateSimpleToString(Date date){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * Date 日期时间类型转 String 日期时间类型
     */
    public static String dateTimeSimpleToString(Date date){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * String 日期时间类型转 Date 日期时间类型
     */
    public static Date stringToDate(String dateTimeString) throws ParseException {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(dateTimeString);
    }
}
