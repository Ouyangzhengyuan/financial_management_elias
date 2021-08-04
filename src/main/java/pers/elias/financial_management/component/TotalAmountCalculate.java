package pers.elias.financial_management.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.service.impl.AccountCurrentService;
import pers.elias.financial_management.utils.KeepTwoDecimals;

import java.util.List;

/**
 * 支出、收入、结余计算
 */
@Component
public class TotalAmountCalculate {
    @Autowired
    private AccountCurrentService accountCurrentService;

    /**
     * 日常支出总额
     */
    public Double getDailyExpense(AccountCurrent accountCurrent) {
        double sum = 0;
        List<Double> dailyExpenseList = accountCurrentService.selectDailyExpense(accountCurrent);
        for (Double dailyExpense : dailyExpenseList) {
            sum = sum + dailyExpense;
        }
        return KeepTwoDecimals.calculateKeepTwoDeci(sum);
    }

    /**
     * 月支出总额
     */
    public Double getMonthlyExpense(AccountCurrent accountCurrent) {
        double sum = 0;
        List<Double> monthlyExpenseList = accountCurrentService.selectMonthlyExpense(accountCurrent);
        for (Double monthlyExpense : monthlyExpenseList) {
            sum = sum + monthlyExpense;
        }
        return KeepTwoDecimals.calculateKeepTwoDeci(sum);
    }

    /**
     * 月收入总额
     */
    public Double getMonthlyIncome(AccountCurrent accountCurrent) {
        double sum = 0;
        List<Double> monthlyExpenseList = accountCurrentService.selectMonthlyIncome(accountCurrent);
        for (Double monthlyExpense : monthlyExpenseList) {
            sum = sum + monthlyExpense;
        }
        return KeepTwoDecimals.calculateKeepTwoDeci(sum);
    }

    /**
     * 月结余
     */
    public Double getMonthlyBalance(AccountCurrent accountCurrentIncome, AccountCurrent accountCurrentExpense){
        return KeepTwoDecimals.calculateKeepTwoDeci(getMonthlyIncome(accountCurrentIncome) - getMonthlyExpense(accountCurrentExpense));
    }
}
