package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.*;
import pers.elias.financial_management.service.impl.*;
import pers.elias.financial_management.utils.AmountSumUtil;
import pers.elias.financial_management.utils.DateTimeUtil;
import pers.elias.financial_management.utils.KeepTwoDecimals;
import pers.elias.financial_management.utils.PageBean;

import javax.swing.*;
import java.util.*;

@Controller
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountIndexService accountIndexService;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private TotalAmountService totalAmountService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private TotalAmountCalculateService totalAmountCalculateService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    /**
     * 日常收支表查询
     */
    @ResponseBody
    @RequestMapping("/dailyInExGraphByConditions")
    public Object dailyInExGraphByConditions(AccountCurrentResult accountCurrentResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //获取、设置当前用户账本索引
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //查询记录的实体字段
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userName", globalAccountInfo.getUserName());//用户名
            paramMap.put("accountBookId", accountBookId);//账本id
            paramMap.put("inExStatus", accountCurrentResult.getIn_ex_status());//收支属性
            //判断一级分类
            Integer firstCategoryId = null;
            if (!"所有分类".equals(accountCurrentResult.getFirst_category_name())) {
                //一级分类实体
                CategoryFirst categoryFirst = new CategoryFirst();
                categoryFirst.setUserName(globalAccountInfo.getUserName());
                categoryFirst.setAccountBookId(accountBookId);
                categoryFirst.setFirstCategoryName(accountCurrentResult.getFirst_category_name());
                //查询一级分类id
                firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            }
            //判断二级分类
            Integer secondCategoryId = null;
            if (!"所有分类".equals(accountCurrentResult.getSecond_category_name())) {
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
                secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
            }
            //判断金融账户
            Integer accountTypeId = null;
            if (!"所有账户".equals(accountCurrentResult.getAccount_type_name())) {
                AccountType accountType = new AccountType();
                accountType.setUserName(globalAccountInfo.getUserName());
                accountType.setAccountBookId(accountBookId);
                accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
                accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            }
            //判断日期选择范围
            String startDate = null;
            String endDate = null;
            if (accountCurrentResult.getDateRange() != null && !"".equals(accountCurrentResult.getDateRange().trim())) {
                //分割日期
                startDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("startDate");
                endDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("endDate");
            }
            //设置实体字段
            paramMap.put("firstCategoryId", firstCategoryId);//一级分类id
            paramMap.put("secondCategoryId", secondCategoryId);//二级分类id
            paramMap.put("accountTypeId", accountTypeId);//金融账户id
            paramMap.put("startDate", startDate);//起始日期
            paramMap.put("endDate", endDate);//结束日期
            paramMap.put("remarks", accountCurrentResult.getRemarks());
            //查询流水记录
            List<AccountCurrentResult> accountCurrentResultList = totalAmountService.selectByConditions(paramMap);
            /*
             处理结果集
             */
            //临时变量
            List<String> firstCategoryListTemp = new ArrayList<>();
            List<String> secondCategoryListTemp = new ArrayList<>();
            //抽取一二级分类
            for (AccountCurrentResult currentResult : accountCurrentResultList) {
                //抽取一级分类
                if (!firstCategoryListTemp.contains(currentResult.getFirst_category_name())) {
                    firstCategoryListTemp.add(currentResult.getFirst_category_name());
                }
                //抽取二级分类
                if (!secondCategoryListTemp.contains(currentResult.getSecond_category_name())) {
                    secondCategoryListTemp.add(currentResult.getSecond_category_name());
                }
            }
            //封装数据：一二级分类、收支总额
            List<Object> resultList = new ArrayList<>();
            List<Object> firstCategoryData = new ArrayList<>();
            for (String firstCategoryName : firstCategoryListTemp) {
                List<Double> allAmountFList = new ArrayList<>();
                //抽取一级分类收支
                for (AccountCurrentResult currentResult : accountCurrentResultList) {
                    if (firstCategoryName.equals(currentResult.getFirst_category_name())) {
                        allAmountFList.add(currentResult.getAmount());
                    }
                }
                //计算每个一级分类收支总额
                Double allAmountF = AmountSumUtil.getAmountSum(allAmountFList);
                //封装一级分类数据
                Map<String, Object> mapF = new HashMap<>();
                mapF.put("firstCategoryName", firstCategoryName);
                mapF.put("allAmount", KeepTwoDecimals.keepTwoDeci(allAmountF));
                resultList.add(mapF);
                firstCategoryData.add(mapF);
                //抽取二级分类收支
                for (String secondCategoryName : secondCategoryListTemp) {
                    List<Double> allAmountSList = new ArrayList<>();
                    for (AccountCurrentResult currentResult : accountCurrentResultList) {
                        if (firstCategoryName.equals(currentResult.getFirst_category_name()) && secondCategoryName.equals(currentResult.getSecond_category_name())) {
                            allAmountSList.add(currentResult.getAmount());
                        }
                    }
                    //计算每个一级分类对应的二级分类收支总额
                    Double allAmountS = AmountSumUtil.getAmountSum(allAmountSList);
                    //封装二级分类数据
                    if (allAmountS != 0) {
                        Map<String, Object> mapS = new HashMap<>();
                        mapS.put("secondCategoryName", secondCategoryName);
                        mapS.put("allAmount", KeepTwoDecimals.keepTwoDeci(allAmountS));
                        resultList.add(mapS);
                    }
                }
                //隔行空数据
                resultList.add(new HashMap<>());
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询成功");
            jsonObject.put("count", resultList.size());
            jsonObject.put("data", resultList);
            jsonObject.put("firstCategoryData", firstCategoryData);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }
    }


    /**
     * 收支趋势
     */
    @ResponseBody
    @RequestMapping("/inExTrendGraph")
    public String inExTrendGraph(AccountCurrentResult accountCurrentResult) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //获取、设置当前用户账本索引
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //判断一级分类
            Integer firstCategoryId;
            if (!"所有分类".equals(accountCurrentResult.getFirst_category_name())) {
                //一级分类实体
                CategoryFirst categoryFirst = new CategoryFirst();
                categoryFirst.setUserName(globalAccountInfo.getUserName());
                categoryFirst.setAccountBookId(accountBookId);
                categoryFirst.setFirstCategoryName(accountCurrentResult.getFirst_category_name());
                //查询一级分类id
                firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            } else {
                firstCategoryId = null;
            }
            //判断二级分类
            Integer secondCategoryId;
            if (!"所有分类".equals(accountCurrentResult.getSecond_category_name())) {
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
                secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
            } else {
                secondCategoryId = null;
            }
            //判断日期选择范围
            String startDate;
            String endDate;
            if (accountCurrentResult.getDateRange() != null && !"".equals(accountCurrentResult.getDateRange().trim())) {
                //分割日期
                startDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("startDate");
                endDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("endDate");
            } else {
                startDate = null;
                endDate = null;
            }
            //实体字段
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userName", globalAccountInfo.getUserName());//用户名
            paramMap.put("accountBookId", accountBookId);//账本id
            paramMap.put("firstCategoryId", firstCategoryId);//一级分类id
            paramMap.put("secondCategoryId", secondCategoryId);//二级分类id
            paramMap.put("startDate", startDate);//起始日期
            paramMap.put("endDate", endDate);//结束日期
            paramMap.put("inExStatus", "收");//收支属性
            //表格数据对象
            List<Object> resultList = new ArrayList<>();
            //总收支封装对象
            Map<String, Object> allMap = new HashMap<>();
            //图表数据对象
            List<Object> inGraphDataList = new ArrayList<>();
            List<Object> exGraphDataList = new ArrayList<>();
            List<Object> baGraphDataList = new ArrayList<>();
            Map<String, Object> graphDataMap = new HashMap<>();
            //查询日期范围内总收入
            Double allIn;
            if (totalAmountService.selectAllInExByConditions(paramMap) != null) {
                allIn = totalAmountService.selectAllInExByConditions(paramMap);
            } else {
                allIn = 0.00;
            }
            //查询日期范围内总支出
            paramMap.put("inExStatus", "支");//收支属性
            Double allEx;
            if (totalAmountService.selectAllInExByConditions(paramMap) != null) {
                allEx = totalAmountService.selectAllInExByConditions(paramMap);
            } else {
                allEx = 0.00;
            }
            //计算日期范围内的余额
            Double allBalance = KeepTwoDecimals.calculateKeepTwoDeci(allIn - allEx);
            allMap.put("year", "总计");
            allMap.put("in", KeepTwoDecimals.keepTwoDeci(allIn));
            allMap.put("ex", KeepTwoDecimals.keepTwoDeci(allEx));
            allMap.put("balance", KeepTwoDecimals.keepTwoDeci(allBalance));
            resultList.add(allMap);//封装总收支
            resultList.add(new HashMap<>());//开启隔行
            //查询年份、月份/日收支
            assert startDate != null;
            int startYear = Integer.parseInt(startDate.substring(0, 4));//截取开始年份
            int endYear = Integer.parseInt(endDate.substring(0, 4));//截取结束年份
            int startMonth = Integer.parseInt(startDate.substring(5, 7));//截取开始月份
            int endMonth = Integer.parseInt(endDate.substring(5, 7));//截取结束月份
            for (int i = startYear; i <= endYear; i++) {//遍历查询
                //查询年收入、年支出、年结余
                paramMap.put("year", i);//设置年份
                paramMap.put("inExStatus", "收");//收支属性
                Double yearIn = 0.00;
                if (totalAmountService.selectYearInExByConditions(paramMap) != null) {
                    yearIn = totalAmountService.selectYearInExByConditions(paramMap);//年收入
                }
                paramMap.put("inExStatus", "支");//收支属性
                Double yearEx = 0.00;
                if (totalAmountService.selectYearInExByConditions(paramMap) != null) {
                    yearEx = totalAmountService.selectYearInExByConditions(paramMap);//年支出
                }
                Double yearBa = KeepTwoDecimals.calculateKeepTwoDeci(yearIn - yearEx);//结余
                //封装年收支数据
                Map<String, Object> yearMap = new HashMap<>();
                yearMap.put("year", i + "年");
                yearMap.put("in", KeepTwoDecimals.keepTwoDeci(yearIn));
                yearMap.put("ex", KeepTwoDecimals.keepTwoDeci(yearEx));
                yearMap.put("balance", KeepTwoDecimals.keepTwoDeci(yearBa));
                resultList.add(yearMap);
                //查询月/日收支
                List<MonthDayInEx> monthDayInList;
                List<MonthDayInEx> monthDayExList;
                List<String> dateTemp = new ArrayList<>();//日期集合临时变量
                //如果日期范围是在同一年同一月份，那么就只查询每日收支情况，否则就查询日期范围内的月份情况
                if (startYear == endYear && startMonth == endMonth) {
                    //构建日查询条件
                    paramMap.put("startYear", startYear);
                    paramMap.put("endYear", endYear);
                    paramMap.put("startMonth", startMonth);
                    paramMap.put("endMonth", endMonth);
                    //查询、抽取每日收支日期
                    paramMap.put("inExStatus", "收");//收支属性
                    monthDayInList = totalAmountService.selectMonthInExByConditions(paramMap);
                    paramMap.put("inExStatus", "支");//收支属性
                    monthDayExList = totalAmountService.selectMonthInExByConditions(paramMap);
                    //抽取日期
                    for (MonthDayInEx monthDayInEx : monthDayInList) {
                        if (!dateTemp.contains(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()))) {
                            dateTemp.add(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()));
                        }
                    }
                    for (MonthDayInEx monthDayInEx : monthDayExList) {
                        if (!dateTemp.contains(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()))) {
                            dateTemp.add(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()));
                        }
                    }
                } else {
                    //查询、抽取每月收支日期
                    paramMap.put("inExStatus", "收");//收支属性
                    monthDayInList = totalAmountService.selectMonthInExByConditions(paramMap);
                    paramMap.put("inExStatus", "支");//收支属性
                    monthDayExList = totalAmountService.selectMonthInExByConditions(paramMap);
                    //抽取日期
                    for (MonthDayInEx monthDayInEx : monthDayInList) {
                        if (!dateTemp.contains(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()).substring(0, 7))) {
                            dateTemp.add(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()).substring(0, 7));
                        }
                    }
                    for (MonthDayInEx monthDayInEx : monthDayExList) {
                        if (!dateTemp.contains(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()).substring(0, 7))) {
                            dateTemp.add(DateTimeUtil.dateSimpleToString(monthDayInEx.getDate()).substring(0, 7));
                        }
                    }
                }
                //对日期列表进行排序
                Collections.sort(dateTemp);
                //抽取并且封装月份、月收支总额、月结余
                for (String date : dateTemp) {
                    Double in = 0.00;
                    Double ex = 0.00;
                    if (startYear == endYear && startMonth == endMonth) {
                        for (MonthDayInEx monthInEx : monthDayInList) {
                            if (date.equals(DateTimeUtil.dateSimpleToString(monthInEx.getDate()))) {
                                in = monthInEx.getAmount();
                            }
                        }
                        for (MonthDayInEx monthInEx : monthDayExList) {
                            if (date.equals(DateTimeUtil.dateSimpleToString(monthInEx.getDate()))) {
                                ex = monthInEx.getAmount();
                            }
                        }
                    }else{
                        for (MonthDayInEx monthInEx : monthDayInList) {
                            if (date.equals(DateTimeUtil.dateSimpleToString(monthInEx.getDate()).substring(0, 7))) {
                                in = monthInEx.getAmount();
                            }
                        }
                        for (MonthDayInEx monthInEx : monthDayExList) {
                            if (date.equals(DateTimeUtil.dateSimpleToString(monthInEx.getDate()).substring(0, 7))) {
                                ex = monthInEx.getAmount();
                            }
                        }
                    }
                    //计算结余
                    Double balance = KeepTwoDecimals.calculateKeepTwoDeci(in - ex);
                    //判断日期格式
                    String dateTable;
                    String dateGraph;
                    if (startYear == endYear && startMonth == endMonth) {
                        String d = Integer.parseInt(date.substring(8, 10)) + "日";
                        dateTable = d;
                        dateGraph = d;
                    } else {
                        dateTable = Integer.parseInt(date.substring(5, 7)) + "月";
                        dateGraph = date.substring(0, 7);
                    }
                    //封装表格数据
                    Map<String, Object> monthInExMap = new HashMap<>();
                    monthInExMap.put("time", dateTable);
                    monthInExMap.put("in", KeepTwoDecimals.keepTwoDeci(in));
                    monthInExMap.put("ex", KeepTwoDecimals.keepTwoDeci(ex));
                    monthInExMap.put("balance", KeepTwoDecimals.keepTwoDeci(balance));
                    resultList.add(monthInExMap);
                    //封装图表数据
                    Map<String, Object> inMap = new HashMap<>();//收入数据
                    inMap.put("time", dateGraph);
                    inMap.put("in", KeepTwoDecimals.calculateKeepTwoDeci(in));
                    inGraphDataList.add(inMap);
                    Map<String, Object> exMap = new HashMap<>();//收入数据
                    exMap.put("time", dateGraph);
                    exMap.put("ex", KeepTwoDecimals.calculateKeepTwoDeci(ex));
                    exGraphDataList.add(exMap);
                    Map<String, Object> baMap = new HashMap<>();//收入数据
                    baMap.put("time", dateGraph);
                    baMap.put("balance", KeepTwoDecimals.calculateKeepTwoDeci(balance));
                    baGraphDataList.add(baMap);
                }
                //开启隔行
                if (i < endYear) {
                    resultList.add(new HashMap<>());
                }
            }
            //封装图表数据
            graphDataMap.put("inData", inGraphDataList);
            graphDataMap.put("exData", exGraphDataList);
            graphDataMap.put("baData", baGraphDataList);
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询成功");
            jsonObject.put("count", resultList.size());
            jsonObject.put("data", resultList);
            jsonObject.put("graphDataMap", graphDataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 资产表
     */
    @ResponseBody
    @RequestMapping("/totalAssets")
    public String totalAssets() {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //获取、设置当前用户账本索引
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级账户实体
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName("信用账户");
            Integer limitAccountFinancialId1 = accountFinancialService.selectId(accountFinancial);
            accountFinancial.setFinancialAccountName("负债账户");
            Integer limitAccountFinancialId2 = accountFinancialService.selectId(accountFinancial);
            //封装限制的一级账户id
            List<Integer> limitAccountType = new ArrayList<>();
            limitAccountType.add(limitAccountFinancialId1);
            limitAccountType.add(limitAccountFinancialId2);
            //二级金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setLimitAccountTypeList(limitAccountType);//限制一级账户id list
            //查询所有二级金融账户列表
            List<String> accountTypeList = accountTypeService.selectAccountTypeName(accountType);
            //流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            accountCurrent.setInExStatus("收");
            //查询每个账户的资产总额
            List<Object> resultList = new ArrayList<>();
            //过滤所有信用账户和负债账户
            AccountFinancial aF = new AccountFinancial();
            aF.setUserName(globalAccountInfo.getUserName());
            aF.setAccountBookId(accountBookId);
            aF.setFinancialAccountName("信用账户");
            Integer creditAccountId = accountFinancialService.selectId(aF);
            aF.setFinancialAccountName("负债账户");
            Integer liabilityAccountId = accountFinancialService.selectId(aF);
            List<Integer> limitAccountFinancialIdList = new ArrayList<>();
            limitAccountFinancialIdList.add(creditAccountId);
            limitAccountFinancialIdList.add(liabilityAccountId);
            //实体类字段
            Map<String, Object> map = new HashMap<>();
            map.put("userName", globalAccountInfo.getUserName());
            map.put("accountBookId", accountBookId);
            map.put("inExStatus", "收");
            map.put("accountFinancialId", limitAccountFinancialIdList);
            //执行查询总收入
            Double allAssets = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculateService.calculateAllInEx(map));
            for (String accountTypeName : accountTypeList) {
                accountType.setAccountTypeName(accountTypeName);
                //查询二级账户id
                Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
                //查询二级账户资产总额
                accountCurrent.setAccountTypeId(accountTypeId);
                Double accountTypeTotalAssets = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                //计算百分比
                double percent;
                if (accountTypeTotalAssets > 0) {
                    percent = (accountTypeTotalAssets / allAssets) * 100;
                } else {
                    percent = 0.00;
                }
                //封装数据
                Map<String, Object> accountTypeMap = new HashMap<>();
                accountTypeMap.put("accountTypeName", accountTypeName);
                accountTypeMap.put("accountTypeTotalAssets", KeepTwoDecimals.keepTwoDeci(accountTypeTotalAssets));
                accountTypeMap.put("percent", KeepTwoDecimals.keepTwoDeci(percent));
                resultList.add(accountTypeMap);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询成功");
            jsonObject.put("count", resultList.size());
            jsonObject.put("data", resultList);
            jsonObject.put("totalAssets", allAssets);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 负债表
     */
    @ResponseBody
    @RequestMapping("/totalDebts")
    public String totalDebts() {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //获取、设置当前用户账本索引
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级账户实体
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName("信用账户");
            Integer creditAccountId = accountFinancialService.selectId(accountFinancial);
            accountFinancial.setFinancialAccountName("负债账户");
            Integer liabilityAccountId = accountFinancialService.selectId(accountFinancial);
            //封装负债账户id
            List<Integer> accountFinancialIdList = new ArrayList<>();
            accountFinancialIdList.add(creditAccountId);
            accountFinancialIdList.add(liabilityAccountId);
            //二级金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountFinancialIdList(accountFinancialIdList);
            //信用账户实体
            List<AccountCurrent> accountCurrentList = new ArrayList<>();
            AccountCurrent creditAccount = new AccountCurrent();
            creditAccount.setUserName(globalAccountInfo.getUserName());
            creditAccount.setAccountBookId(accountBookId);
            creditAccount.setAccountFinancialId(creditAccountId);//信用账户id
            accountCurrentList.add(creditAccount);
            //负债账户实体
            AccountCurrent liabilityAccount = new AccountCurrent();
            liabilityAccount.setUserName(globalAccountInfo.getUserName());
            liabilityAccount.setAccountBookId(accountBookId);
            liabilityAccount.setAccountFinancialId(liabilityAccountId);//负债账户id
            accountCurrentList.add(liabilityAccount);
            //查询总负债（所有负债子账户收入+支出，正负相加得到的结果）
            //负值：收入-支出 > 0
            //正值：收入-支出 < 0
            Double allDebts = totalAmountCalculateService.calculateAllDebts(accountCurrentList);
            //流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            //查询所有二级金融账户列表
            List<String> accountTypeList = accountTypeService.selectAccountTypeName(accountType);
            //数据对象
            List<Object> resultList = new ArrayList<>();
            //查询子账户的所有负负债
            double allDebtsSubAccount = 0.00;
            for (String accountTypeName : accountTypeList) {
                accountType.setAccountTypeName(accountTypeName);
                //查询二级账户id
                Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
                accountCurrent.setInExStatus("支");
                accountCurrent.setAccountTypeId(accountTypeId);
                Double debtsEx = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                accountCurrent.setInExStatus("收");
                Double debtsIn = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                double debts = KeepTwoDecimals.calculateKeepTwoDeci(debtsIn - debtsEx);
                //如果收入-支出 < 0，那么就纳入负债百分比总额
                if (debts < 0) {
                    allDebtsSubAccount = KeepTwoDecimals.calculateKeepTwoDeci(allDebtsSubAccount + debts);
                }
            }
            //封装每个负债子账户的名称、金额、百分比
            for (String accountTypeName : accountTypeList) {
                accountType.setAccountTypeName(accountTypeName);
                //查询二级账户id
                Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
                //查询单个二级账户负债总额：负债支出-负债收入=负债总额
                accountCurrent.setInExStatus("支");
                accountCurrent.setAccountTypeId(accountTypeId);
                Double debtsEx = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                accountCurrent.setInExStatus("收");
                Double debtsIn = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                double debts = KeepTwoDecimals.calculateKeepTwoDeci(debtsEx - debtsIn);
                double percent;
                if (debts > 0) {
                    percent = Math.abs((debts / allDebtsSubAccount) * 100);
                } else {
                    percent = 0.00;
                }
                //封装数据
                Map<String, Object> accountTypeMap = new HashMap<>();
                accountTypeMap.put("accountTypeName", accountTypeName);
                accountTypeMap.put("accountTypeTotalDebts", KeepTwoDecimals.keepTwoDeci(debts));
                accountTypeMap.put("percent", KeepTwoDecimals.keepTwoDeci(percent));
                resultList.add(accountTypeMap);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询成功");
            jsonObject.put("count", resultList.size());
            jsonObject.put("data", resultList);
            jsonObject.put("totalDebts", KeepTwoDecimals.keepTwoDeci(allDebts));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 对账报表概况
     */
    @ResponseBody
    @RequestMapping("/reconciliation")
    public Object reconciliation(String accountTypeName, String dateRange) {
        //获取当前登录的用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置当前登录的用户
        globalAccountInfo.setUserName(userName);
        //设置当前用户账本
        globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
        //反向查询账本id
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        //解析日期
        String startDate;
        String endDate;
        if (dateRange != null && !"".equals(dateRange.trim())) {
            //分割日期
            startDate = DateTimeUtil.dateSeparate(dateRange).get("startDate");
            endDate = DateTimeUtil.dateSeparate(dateRange).get("endDate");
        } else {
            startDate = null;
            endDate = null;
        }
        //查询二级金融账户id
        AccountType accountType = new AccountType();
        accountType.setUserName(globalAccountInfo.getUserName());
        accountType.setAccountBookId(accountBookId);
        accountType.setAccountTypeName(accountTypeName);
        Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
        //实体类字段
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", globalAccountInfo.getUserName());
        paramMap.put("accountBookId", accountBookId);
        paramMap.put("inExStatus", "收");
        paramMap.put("accountTypeId", accountTypeId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        //查询日收入
        List<AccountCurrentResult> accountCurrentResultListIn = totalAmountService.selectDailyInExByReconciliation(paramMap);
        //查询日支出
        paramMap.put("inExStatus", "支");
        paramMap.remove("accountFinancialId");
        List<AccountCurrentResult> accountCurrentResultListEx = totalAmountService.selectDailyInExByReconciliation(paramMap);
        //抽取查询到的所有日期
        List<String> allDate = new ArrayList<>();
        //抽取收入日期
        for (AccountCurrentResult accountCurrentResult : accountCurrentResultListIn) {
            if (!allDate.contains(accountCurrentResult.getDateConverted())) {
                allDate.add(accountCurrentResult.getDateConverted());
            }
        }
        //抽取支出日期
        for (AccountCurrentResult accountCurrentResult : accountCurrentResultListEx) {
            if (!allDate.contains(accountCurrentResult.getDateConverted())) {
                allDate.add(accountCurrentResult.getDateConverted());
            }
        }
        //对日期进行降序排列
        allDate.sort(Comparator.reverseOrder());
        //数据对象
        List<Object> resultList = new ArrayList<>();
        //封装日期、收支总额、余额
        for (String s : allDate) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", s);//封装日期
            for (AccountCurrentResult accountCurrentResultIn : accountCurrentResultListIn) {
                if (s.equals(accountCurrentResultIn.getDateConverted())) {
                    map.put("in", KeepTwoDecimals.keepTwoDeci(accountCurrentResultIn.getAmount()));//封装收入总额
                }
            }
            for (AccountCurrentResult accountCurrentResultEx : accountCurrentResultListEx) {
                if (s.equals(accountCurrentResultEx.getDateConverted())) {
                    map.put("ex", KeepTwoDecimals.keepTwoDeci(accountCurrentResultEx.getAmount()));//封装支出总额
                }
            }
            if (!map.containsKey("in")) {
                map.put("in", KeepTwoDecimals.keepTwoDeci(0.00));//如果当天没有任何收入，那么就将收入设置为 0
            }
            if (!map.containsKey("ex")) {
                map.put("ex", KeepTwoDecimals.keepTwoDeci(0.00));//如果当天没有任何支出，那么就将支出设置为 0
            }
            //查询从注册日期到之后的指定日期的收支总额
            //获取用户注册日期
            String registerDate = DateTimeUtil.dateSimpleToString(accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getDateCreated());
            //收支实体类
            Map<String, Object> allInExMap = new HashMap<>();
            allInExMap.put("userName", globalAccountInfo.getUserName());
            allInExMap.put("accountBookId", accountBookId);
            allInExMap.put("inExStatus", "收");
            allInExMap.put("accountTypeId", accountTypeId);
            allInExMap.put("registerDate", registerDate);
            allInExMap.put("endDate", s);
            //查询总收入
            Double allIn = totalAmountService.selectAllInExByReconciliation(allInExMap);
            //查询总支出
            allInExMap.put("inExStatus", "支");
            Double allEx = totalAmountService.selectAllInExByReconciliation(allInExMap);
            //计算余额
            if (allIn == null) {
                allIn = 0.00;
            }
            if (allEx == null) {
                allEx = 0.00;
            }
            //余额 = 账户收入 - 账户支出
            Double balance = KeepTwoDecimals.calculateKeepTwoDeci(allIn - allEx);
            map.put("balance", balance);
            resultList.add(map);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("data", resultList);
        return jsonObject.toString();
    }

    /**
     * 对账报表流水表格
     */
    @RequestMapping("/reconciliationTable")
    @ResponseBody
    public Object accountCurrentList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                                     String date, String accountTypeName) {
        //获取当前登录的用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置当前登录的用户
        globalAccountInfo.setUserName(userName);
        //设置当前用户账本
        globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
        //反向查询账本id
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        //查询二级金融账户id
        AccountType accountType = new AccountType();
        accountType.setUserName(globalAccountInfo.getUserName());
        accountType.setAccountBookId(accountBookId);
        accountType.setAccountTypeName(accountTypeName);
        Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
        //设置实体字段
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", pageno);
        paramMap.put("pagesize", pagesize);
        paramMap.put("userName", globalAccountInfo.getUserName());
        paramMap.put("accountBookId", accountBookId);
        paramMap.put("accountTypeId", accountTypeId);
        paramMap.put("date", date);
        //查询流水
        PageBean<AccountCurrentResult> pageBean = accountCurrentService.selectByConditions(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", pageBean.getTotalsize());
        resultMap.put("data", pageBean.getDatas());
        return resultMap;
    }
}
