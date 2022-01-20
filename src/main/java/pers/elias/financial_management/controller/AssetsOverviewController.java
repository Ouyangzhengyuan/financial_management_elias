package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountFinancial;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountFinancialService;
import pers.elias.financial_management.service.impl.AccountIndexService;
import pers.elias.financial_management.service.impl.TotalAmountCalculateService;
import pers.elias.financial_management.utils.KeepTwoDecimals;

import java.util.*;

@Controller
@RequestMapping("/assetsOverview")
public class AssetsOverviewController {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private TotalAmountCalculateService totalAmountCalculate;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 账本资产概况：总资产、总负债、净资产
     */
    @ResponseBody
    @RequestMapping("/financialOverview")
    public String getAllIncome(String accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //过滤所有信用账户和负债账户
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName("信用账户");
            Integer creditAccountId = accountFinancialService.selectId(accountFinancial);
            accountFinancial.setFinancialAccountName("负债账户");
            Integer liabilityAccountId = accountFinancialService.selectId(accountFinancial);
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
            Double allAssets = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateAllInEx(map));
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
            //查询总负债
            Double allDebts = totalAmountCalculate.calculateAllDebts(accountCurrentList);
            //计算净资产
            Double netAssets = KeepTwoDecimals.calculateKeepTwoDeci(allAssets - allDebts);
            jsonObject.put("success", true);
            jsonObject.put("allAssets", KeepTwoDecimals.keepTwoDeci(allAssets));//总资产
            jsonObject.put("allDebts", KeepTwoDecimals.keepTwoDeci(allDebts));//总负债
            jsonObject.put("netAssets", KeepTwoDecimals.keepTwoDeci(netAssets));//净资产
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "获取资产概况异常");
            return jsonObject.toString();
        }
    }

    /**
     * 日收入总额
     */
    @ResponseBody
    @RequestMapping("/getDailyInEx")
    public String getDailyIncome(String accountBookName, String date) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //账本流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            accountCurrent.setDateConverted(date);
            accountCurrent.setInExStatus("收");
            //获取日收入
            Double dailyIn = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateDailyInEx(accountCurrent));
            //获取日支出
            accountCurrent.setInExStatus("支");
            Double dailyEx = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateDailyInEx(accountCurrent));
            jsonObject.put("success", true);
            jsonObject.put("dailyIn", KeepTwoDecimals.keepTwoDeci(dailyIn));
            jsonObject.put("dailyEx", KeepTwoDecimals.keepTwoDeci(dailyEx));
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询日收入异常");
            return jsonObject.toString();
        }
    }

    /**
     * 月收入、月支出、月结余
     */
    @ResponseBody
    @RequestMapping("/getMonthlyInEx")
    public String getMonthlyExpense(String accountBookName, String dateSubstring) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            accountCurrent.setInExStatus("收");
            accountCurrent.setDateConverted(dateSubstring);
            //月收入
            Double monthlyIn = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateMonthlyInEx(accountCurrent));
            //月支出
            accountCurrent.setInExStatus("支");
            Double monthlyEx = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateMonthlyInEx(accountCurrent));
            Double monthlyBa = KeepTwoDecimals.calculateKeepTwoDeci(monthlyIn - monthlyEx);
            jsonObject.put("success", true);
            jsonObject.put("monthlyIn", KeepTwoDecimals.keepTwoDeci(monthlyIn));
            jsonObject.put("monthlyEx", KeepTwoDecimals.keepTwoDeci(monthlyEx));
            jsonObject.put("monthlyBa", KeepTwoDecimals.keepTwoDeci(monthlyBa));
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询月支出异常");
            return jsonObject.toString();
        }
    }

    /**
     * 年收支
     */
    @ResponseBody
    @RequestMapping("/getYearlyInEx")
    public String getYearlyIncome(String accountBookName, String year) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            accountCurrent.setDateConverted(year);
            accountCurrent.setInExStatus("收");
            //年收入
            Double yearlyIn = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateYearlyInEx(accountCurrent));
            //年支出
            accountCurrent.setInExStatus("支");
            Double yearlyEx = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculate.calculateYearlyInEx(accountCurrent));
            jsonObject.put("success", true);
            jsonObject.put("yearlyIn", KeepTwoDecimals.keepTwoDeci(yearlyIn));
            jsonObject.put("yearlyEx", KeepTwoDecimals.keepTwoDeci(yearlyEx));
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询年收入异常");
            return jsonObject.toString();
        }
    }

    /**
     * 一级账户资产概况：总收入、总支出、结余
     */
    @ResponseBody
    @RequestMapping("/getAccountOverview")
    public String getAccountOverview(String accountBookName, String accountFinancialName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级账户实体
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName(accountFinancialName);
            //查询一级账户 id
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            //流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(accountBookId);
            accountCurrent.setAccountFinancialId(accountFinancialId);
            //查询一级账户总收入
            accountCurrent.setInExStatus("收");
            Double accountIn = totalAmountCalculate.calculateAccountAllInEx(accountCurrent);
            //查询一级账户总支出
            accountCurrent.setInExStatus("支");
            Double accountEx = totalAmountCalculate.calculateAccountAllInEx(accountCurrent);
            //计算结余
            Double accountBa = KeepTwoDecimals.calculateKeepTwoDeci(accountIn - accountEx);
            jsonObject.put("success", true);
            jsonObject.put("accountIn", KeepTwoDecimals.keepTwoDeci(accountIn));
            jsonObject.put("accountEx", KeepTwoDecimals.keepTwoDeci(accountEx));
            jsonObject.put("accountBa", KeepTwoDecimals.keepTwoDeci(accountBa));
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询一级账户资产异常");
            return jsonObject.toString();
        }
    }
}
