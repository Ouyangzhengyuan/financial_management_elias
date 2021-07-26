package pers.elias.financial_management.utils;

import java.math.BigDecimal;

public class KeepTwoDecimals {
    public static Double keepTwoDecimals(Double doubleValue){
        return new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
