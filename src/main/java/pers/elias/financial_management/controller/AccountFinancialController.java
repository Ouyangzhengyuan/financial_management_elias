package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.AccountFinancial;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.impl.*;
import pers.elias.financial_management.utils.KeepTwoDecimals;
import pers.elias.financial_management.utils.PageBean;

import java.util.*;

@Controller
@RequestMapping("/accountFinancial")
public class AccountFinancialController {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private TotalAmountCalculateService totalAmountCalculateService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 查询一级金融账户
     */
    @ResponseBody
    @RequestMapping("/accountFinancialList")
    public Object accountFinancialList(String accountBookName){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级金融账户实体类
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            //执行查询
            List<String> accountFinancialList = accountFinancialService.selectAll(accountFinancial);
            jsonObject.put("success", true);
            jsonObject.put("data", accountFinancialList);
            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询一级金融账户异常");
            return jsonObject;
        }
    }

    /**
     * 查询子账户
     */
    @ResponseBody
    @RequestMapping("/subAccountList")
    public String subAccountList(String accountBookName, String accountFinancialName){
        JSONObject jsonObject = new JSONObject();
        try{
            if(accountBookName != null){
                globalAccountInfo.setAccountBookName(accountBookName);
            }
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级金融账户实体类
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName(accountFinancialName);
            //查询一级金融账户 id
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            accountFinancial.setId(accountFinancialId);
            //一级金融账户实体字段
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userName", globalAccountInfo.getUserName());
            paramMap.put("accountBookId", accountBookId);
            paramMap.put("accountFinancialId", accountFinancialId);
            //查询子账户
            List<String> subAccountList = accountFinancialService.selectSubAccount(paramMap);
            //查询子账户总收支
            Map<String, Object> subAccountMap = new LinkedHashMap<>();
            //封装所有子账户的收支、结余
            for (String accountTypeName : subAccountList) {
                //查询二级金融id
                AccountType accountType = new AccountType();
                accountType.setUserName(globalAccountInfo.getUserName());
                accountType.setAccountBookId(accountBookId);
                accountType.setAccountTypeName(accountTypeName);
                Integer subAccountId = accountTypeService.selectIdByAccountType(accountType);
                //查询二级金融收支
                AccountCurrent accountCurrent = new AccountCurrent();
                accountCurrent.setUserName(globalAccountInfo.getUserName());
                accountCurrent.setAccountBookId(accountBookId);
                accountCurrent.setInExStatus("收");
                accountCurrent.setAccountTypeId(subAccountId);
                Double income;
                Double expense;
                double balance;
                List<Object> inExList = new ArrayList<>();
                //查询二级账户总收入
                income = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                //查询二级账户总支出
                accountCurrent.setInExStatus("支");
                expense = totalAmountCalculateService.calculateSubAccountAllInEx(accountCurrent);
                //计算结余
                balance = KeepTwoDecimals.calculateKeepTwoDeci(income - expense);
                //查询子账户流水总记录数
                Integer subAccountCount = accountCurrentService.selectSubAccountCount(accountCurrent);
                //封装数据
                inExList.add(KeepTwoDecimals.keepTwoDeci(income));
                inExList.add(KeepTwoDecimals.keepTwoDeci(expense));
                inExList.add(KeepTwoDecimals.keepTwoDeci(balance));
                inExList.add(subAccountCount);
                subAccountMap.put(accountTypeName, inExList);
            }
            jsonObject.put("success", true);
            jsonObject.put("subAccountList", subAccountList);
            jsonObject.put("subAccountInEx", subAccountMap);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询子账户异常");
            return jsonObject.toString();
        }
    }

    /**
     * 查询子账户的所有流水记录
     */
    @ResponseBody
    @RequestMapping("/selectSubAccountCurrentList")
    public Object selectSubAccountCurrentList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,@RequestParam(value = "limit", defaultValue = "5") Integer pagesize, String accountFinancialName, String accountTypeName){
        JSONObject jsonObject = new JSONObject();
        try{
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("pageno", pageno);
            paramMap.put("pagesize", pagesize);
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //反向查询一级金融账户id
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName(accountFinancialName);
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            //反向查询二级金融账户id
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //设置实体字段
            paramMap.put("userName", globalAccountInfo.getUserName());
            paramMap.put("accountBookId", accountBookId);
            paramMap.put("accountFinancialId", accountFinancialId);
            paramMap.put("accountTypeId", accountTypeId);
            PageBean<AccountCurrentResult> pageBean = accountFinancialService.selectSubAccountCurrent(paramMap);
            jsonObject.put("code", 0);
            jsonObject.put("msg", "");
            jsonObject.put("count", pageBean.getTotalsize());
            jsonObject.put("data", pageBean.getDatas());
            return jsonObject;
        }catch(Exception e){
            e.printStackTrace();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询子账户流水异常");
            return jsonObject;
        }
    }
}
