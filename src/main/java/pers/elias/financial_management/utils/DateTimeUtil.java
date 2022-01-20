package pers.elias.financial_management.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public static String dateSimpleToString(Date date) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * Date 日期时间类型转 String 日期时间类型
     */
    public static String dateTimeSimpleToString(Date date) {
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

    /**
     * 将前端传到后端的日期范围进行分割，然后封装
     */
    public static Map<String, String> dateSeparate(String dataRange) {
        String endDate = dataRange.substring(13);
        String startDate = dataRange.substring(0, 10);
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return map;
    }

    /**
     * 获取当前日期的月份
     */
    public static String getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH)+1+"月";
    }
}
