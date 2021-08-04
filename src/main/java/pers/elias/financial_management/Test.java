package pers.elias.financial_management;

import pers.elias.financial_management.utils.KeepTwoDecimals;

public class Test {
    public static void main(String[] args){
        System.out.println(getAmountFormatString(7000.00));
    }

    public static String getAmountFormatString(Double amountValue) {
        StringBuilder amount = new StringBuilder(KeepTwoDecimals.keepTwoDeci(amountValue));
        if (amount.length() > 3) {
            int a = amount.length() - 3;
            if(a < 4){
                return amount.toString();
            }
            if (a == 4){
                amount.insert(1, ",");
                return amount.toString();
            }
            if (a < 7) {
                amount.insert(3, ",");
                return amount.toString();
            }
            if (a == 7) {
                amount.insert(1, ",");
                amount.insert(4 + 1, ",");
                return amount.toString();
            }
            if (a > 7) {
                amount.insert(1 + 1, ",");
                amount.insert(4 + 2, ",");
                return amount.toString();
            }
        }
        return amount.toString();
    }
}
