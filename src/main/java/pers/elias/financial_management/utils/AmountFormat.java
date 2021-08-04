package pers.elias.financial_management.utils;


/**
 * 金额明细格式转化： 10000000.00 ~ 10,000,000.00
 */
public class AmountFormat {
    public static String getAmountFormatString(Double amountValue) {
        StringBuilder amount = new StringBuilder(Double.toString(amountValue));
        if (amount.length() > 3) {
            int a = amount.length() - 3;
            if (a < 7 && a > 3) {
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
