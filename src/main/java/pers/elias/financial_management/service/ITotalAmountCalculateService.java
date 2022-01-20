package pers.elias.financial_management.service;

import pers.elias.financial_management.model.AccountCurrent;

import java.util.List;
import java.util.Map;

public interface ITotalAmountCalculateService {
    /**
     * 计算日收支
     */
     Double calculateDailyInEx(AccountCurrent accountCurrent);

    /**
     * 计算月收支
     */
    Double calculateMonthlyInEx(AccountCurrent accountCurrent);

    /**
     * 计算年收支
     */
    Double calculateYearlyInEx(AccountCurrent accountCurrent);

    /**
     * 计算所有收支
     */
    Double calculateAllInEx(Map<String, Object> map);

    /**
     * 计算信用账户、负债账户的所有负债
     */
    Double calculateAllDebts(List<AccountCurrent> accountCurrentList);

    /**
     * 计算一级账户收支
     */
    Double calculateAccountAllInEx(AccountCurrent accountCurrent);

    /**
     * 计算二级账户收支
     */
    Double calculateSubAccountAllInEx(AccountCurrent accountCurrent);

    /**
     * 计算二级分类收支总额
     */
    Double calculateSecondCategoryAllInEx(Map<String, Object> map);

    /**
     * 计算一级分类收支总额
     */
    Double calculateFirstCategoryAllInEx(Map<String, Object> map);
}
