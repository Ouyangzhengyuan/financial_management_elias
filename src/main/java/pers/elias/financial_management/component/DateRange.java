package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;
import java.util.Calendar;

@Component
public class DateRange {
    private Calendar calendar;

    public DateRange() {
        this.calendar = Calendar.getInstance();
    }

    private String getStartDate(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int days = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        return year+".0"+month+".0"+days;
    }

    private String getEndDate(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return year+".0"+month+"."+days;
    }

    public String getDateRange(){
        return getStartDate() + " ~ " + getEndDate();
    }

    public String getDateRange(Integer year, Integer month){
        calendar.set(year, month, 0);
        return getStartDate() + " ~ " + getEndDate();
    }
}
