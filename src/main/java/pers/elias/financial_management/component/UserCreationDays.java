package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取用户注册日期到现在的天数
 */
@Component
public class UserCreationDays {
    public static int getUserCreationDays(String nowDate, String createdDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long timeNow = 0;
        long timeCreated = 0;
        try {
            cal.setTime(simpleDateFormat.parse(nowDate));
            timeNow = cal.getTimeInMillis();
            cal.setTime(simpleDateFormat.parse(createdDate));
            timeCreated = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_days = (timeNow - timeCreated) / (1000 * 3600 * 24);
        if(between_days == 0){
            between_days = 1;
            return Integer.parseInt(String.valueOf(between_days));
        }
            return Integer.parseInt(String.valueOf(between_days));
    }
}
