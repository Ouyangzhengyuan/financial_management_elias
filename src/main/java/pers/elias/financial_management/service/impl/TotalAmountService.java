package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.TotalAmountMapper;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.MonthDayInEx;
import pers.elias.financial_management.service.ITotalAmountService;

import java.util.List;
import java.util.Map;

@Service
public class TotalAmountService implements ITotalAmountService {
    @Autowired
    private TotalAmountMapper totalAmountMapper;

    @Override
    public List<Double> selectDailyInEx(AccountCurrent accountCurrent) {
        return totalAmountMapper.selectDailyInEx(accountCurrent);
    }

    @Override
    public List<Double> selectMonthlyInEx(AccountCurrent accountCurrent) {
        return totalAmountMapper.selectMonthlyInEx(accountCurrent);
    }

    @Override
    public List<Double> selectYearlyInEx(AccountCurrent accountCurrent) {
        return totalAmountMapper.selectYearlyInEx(accountCurrent);
    }

    @Override
    public List<Double> selectAllInEx(Map<String, Object> map) {
        return totalAmountMapper.selectAllInEx(map);
    }

    @Override
    public List<Double> selectAccountAllInEx(AccountCurrent accountCurrent) {
        return totalAmountMapper.selectAccountAllInEx(accountCurrent);
    }

    @Override
    public List<Double> selectSubAccountAllInEx(AccountCurrent accountCurrent) {
        return totalAmountMapper.selectSubAccountAllInEx(accountCurrent);
    }

    @Override
    public List<Double> selectSecondCategoryAllInEx(Map<String, Object> map) {
        return totalAmountMapper.selectSecondCategoryAllInEx(map);
    }

    @Override
    public List<Double> selectFirstCategoryAllInEx(Map<String, Object> map) {
        return totalAmountMapper.selectFirstCategoryAllInEx(map);
    }

    @Override
    public List<AccountCurrentResult> selectByConditions(Map<String, Object> map) {
        return totalAmountMapper.selectByConditions(map);
    }

    @Override
    public Double selectYearInExByConditions(Map<String, Object> map) {
        return totalAmountMapper.selectYearInExByConditions(map);
    }

    @Override
    public Double selectAllInExByConditions(Map<String, Object> map) {
        return totalAmountMapper.selectAllInExByConditions(map);
    }

    @Override
    public List<MonthDayInEx> selectMonthInExByConditions(Map<String, Object> map) {
        return totalAmountMapper.selectMonthInExByConditions(map);
    }

    @Override
    public Double selectAllInExByReconciliation(Map<String, Object> map) {
        return totalAmountMapper.selectAllInExByReconciliation(map);
    }

    @Override
    public List<AccountCurrentResult> selectDailyInExByReconciliation(Map<String, Object> map) {
        return totalAmountMapper.selectDailyInExByReconciliation(map);
    }
}
