package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.ITotalAmountCalculateService;
import pers.elias.financial_management.utils.AmountSumUtil;
import pers.elias.financial_management.utils.KeepTwoDecimals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TotalAmountCalculateService implements ITotalAmountCalculateService {
    @Autowired
    private TotalAmountService totalAmountService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private TotalAmountCalculateService totalAmountCalculateService;

    /**
     * 计算日收支总额
     */
    @Override
    public Double calculateDailyInEx(AccountCurrent accountCurrent) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectDailyInEx(accountCurrent));
    }

    /**
     * 计算月收支总额
     */
    @Override
    public Double calculateMonthlyInEx(AccountCurrent accountCurrent) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectMonthlyInEx(accountCurrent));
    }

    /**
     * 计算年收支总额
     */
    @Override
    public Double calculateYearlyInEx(AccountCurrent accountCurrent) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectYearlyInEx(accountCurrent));
    }

    /**
     * 计算账本收支总额
     */
    @Override
    public Double calculateAllInEx(Map<String, Object> map) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectAllInEx(map));

    }

    /**
     * 计算总负债
     */
    @Override
    public Double calculateAllDebts(List<AccountCurrent> accountCurrentList) {
        double allDebts = 0;
        for (AccountCurrent accountCurrent : accountCurrentList) {
            //二级账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(accountCurrent.getUserName());
            accountType.setAccountBookId(accountCurrent.getAccountBookId());
            accountType.setFinancialAccountId(accountCurrent.getAccountFinancialId());
            //查询一级账户对应的所有二级账户
            List<String> subAccountType = accountTypeService.selectAccountTypeName(accountType);
            //计算每个二级账户的实际负债
            for (String accountTypeName : subAccountType) {
                accountType.setAccountTypeName(accountTypeName);
                //查询二级账户id
                Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
                accountCurrent.setInExStatus("支");
                accountCurrent.setAccountTypeId(accountTypeId);
                Double debtsEx = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                accountCurrent.setInExStatus("收");
                Double debtsIn = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                //计算单个二级账户的实际负债
                double debts = KeepTwoDecimals.calculateKeepTwoDeci(debtsIn - debtsEx);
                //汇总二级账户实际负债
                allDebts = KeepTwoDecimals.calculateKeepTwoDeci((allDebts + (-debts)));
            }
        }
        return allDebts;
    }

    /**
     * 计算一级金融账户收支总额
     */
    @Override
    public Double calculateAccountAllInEx(AccountCurrent accountCurrent) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectAccountAllInEx(accountCurrent));
    }

    /**
     * 计算二级账户收支总额
     */
    @Override
    public Double calculateSubAccountAllInEx(AccountCurrent accountCurrent) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectSubAccountAllInEx(accountCurrent));
    }

    /**
     * 计算二级分类收支总额
     */
    @Override
    public Double calculateSecondCategoryAllInEx(Map<String, Object> map) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectSecondCategoryAllInEx(map));
    }

    @Override
    public Double calculateFirstCategoryAllInEx(Map<String, Object> map) {
        return AmountSumUtil.getAmountSum(totalAmountService.selectFirstCategoryAllInEx(map));
    }
}
