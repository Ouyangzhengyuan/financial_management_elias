package pers.elias.financial_management.utils;

import java.util.List;

public class AmountSumUtil {
    public static Double getAmountSum(List<Double> amountList){
        double sum = 0.00;
        for(Double amount: amountList){
            sum += amount;
        }
        return KeepTwoDecimals.calculateKeepTwoDeci(sum);
    }
}
