package pers.elias.financial_management.mapper;

import org.springframework.stereotype.Repository;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.MonthDayInEx;

import java.util.List;
import java.util.Map;

@Repository
public interface TotalAmountMapper {
    /**
     * 查询日收支
     */
    List<Double> selectDailyInEx(AccountCurrent accountCurrent);

    /**
     * 查询月收支
     */
    List<Double> selectMonthlyInEx(AccountCurrent accountCurrent);

    /**
     * 查询年收支
     */
    List<Double> selectYearlyInEx(AccountCurrent accountCurrent);

    /**
     * 查询所有收支
     */
    List<Double> selectAllInEx(Map<String, Object> map);

    /**
     * 查询一级账户收支
     */
    List<Double> selectAccountAllInEx(AccountCurrent accountCurrent);

    /**
     * 查询二级账户收支
     */
    List<Double> selectSubAccountAllInEx(AccountCurrent accountCurrent);

    /**
     * 查询二级分类收支
     */
    List<Double> selectSecondCategoryAllInEx(Map<String, Object> map);

    /**
     * 查询一级分类收支
     */
    List<Double> selectFirstCategoryAllInEx(Map<String, Object> map);

    /**
     * 条件查询
     */
    List<AccountCurrentResult> selectByConditions(Map<String, Object> map);

    /**
     * 查询年收支 数据库直接查询计算
     */
    Double selectYearInExByConditions(Map<String, Object> map);

    /**
     * 查询时间范围内的收支总额 数据库直接查询计算
     */
    Double selectAllInExByConditions(Map<String, Object> map);

    /**
     * 查询指定年份的所有月份数据
     */
    List<MonthDayInEx> selectMonthInExByConditions(Map<String, Object> map);

    /**
     * 查询所有收支总额 数据库直接查询计算返回结果
     */
    Double selectAllInExByReconciliation(Map<String, Object> map);

    List<AccountCurrentResult> selectDailyInExByReconciliation(Map<String, Object> map);
}
