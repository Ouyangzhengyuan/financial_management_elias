package pers.elias.financial_management.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 双精度保留两位有效小数
 */
public class KeepTwoDecimals {
    public static Double calculateKeepTwoDeci(Double doubleValue){
        return new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String keepTwoDeci(Double doubleValue){
       return new DecimalFormat("#.00").format(doubleValue);
    }
}
