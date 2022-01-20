package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.component.DateRange;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.*;
import pers.elias.financial_management.service.impl.*;
import pers.elias.financial_management.utils.DateTimeUtil;
import pers.elias.financial_management.utils.PageBean;

import java.util.*;

@Controller
@RequestMapping("/accountCurrent")
public class AccountCurrentController {
    @Autowired
    private DateRange dateRange;

    @Autowired
    private AjaxResult ajaxResult;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountIndexService accountIndexService;

    @Autowired
    private CategoryFirstService categoryFirstService;

    /**
     * 查询所有流水
     */
    @RequestMapping("/accountCurrentList.json")
    @ResponseBody
    public Object accountCurrentList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                                     String dateSubstring) {
        //获取当前登录的用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置当前登录的用户
        globalAccountInfo.setUserName(userName);
        //设置当前用户账本
        globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
        //反向查询账本id
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        //设置实体字段
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", pageno);
        paramMap.put("pagesize", pagesize);
        paramMap.put("userName", globalAccountInfo.getUserName());
        paramMap.put("accountBookId", accountBookId);
        paramMap.put("dateConverted", dateSubstring);
        //查询流水
        PageBean<AccountCurrentResult> pageBean = accountCurrentService.selectAllByAccountCurrent(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", pageBean.getTotalsize());
        resultMap.put("data", pageBean.getDatas());
        resultMap.put("dateRange", dateRange.getDateRange(dateSubstring));
        return resultMap;
    }

    /**
     * 条件搜索
     */
    @RequestMapping("/selectByConditions")
    @ResponseBody
    public Object selectByConditions(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer pagesize, AccountCurrentResult accountCurrentResult) {
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
        //判断金融账户
        Integer accountTypeId;
        if (!"所有账户".equals(accountCurrentResult.getAccount_type_name())) {
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
            accountTypeId = accountTypeService.selectIdByAccountType(accountType);
        } else {
            accountTypeId = null;
        }
        //判断日期选择范围
        String startDate;
        String endDate;
        if (!"".equals(accountCurrentResult.getDateRange().trim())) {
            //分割日期
            startDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("startDate");
            endDate = DateTimeUtil.dateSeparate(accountCurrentResult.getDateRange()).get("endDate");
        } else {
            startDate = null;
            endDate = null;
        }
        //设置实体字段
        paramMap.put("userName", globalAccountInfo.getUserName());//用户名
        paramMap.put("accountBookId", accountBookId);//账本id
        paramMap.put("firstCategoryId", firstCategoryId);//一级分类id
        paramMap.put("secondCategoryId", secondCategoryId);//二级分类id
        paramMap.put("accountTypeId", accountTypeId);//金融账户id
        paramMap.put("startDate", startDate);//起始日期
        paramMap.put("endDate", endDate);//结束日期
        paramMap.put("remarks", accountCurrentResult.getRemarks());
        //查询流水
        PageBean<AccountCurrentResult> pageBean = accountCurrentService.selectByConditions(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", pageBean.getTotalsize());
        resultMap.put("data", pageBean.getDatas());
        return resultMap;
    }

    /**
     * 通过 id 查询
     */
    @ResponseBody
    @RequestMapping("/selectById")
    public String selectById(@RequestParam("accountCurrentId") Integer id) {
        JSONObject jsonObject = new JSONObject();
        //查询指定 id 编号的流水数据
        AccountCurrentResult accountCurrentResult = accountCurrentService.selectById(id);
        //转换日期格式
        accountCurrentResult.setDateConverted(DateTimeUtil.dateTimeSimpleToString(accountCurrentResult.getDate()));
        jsonObject.put("success", true);
        jsonObject.put("data", accountCurrentResult);
        return jsonObject.toString();
    }

    /**
     * 查询记录总数
     */
    @ResponseBody
    @RequestMapping("/getCount")
    public String getCount(String accountBookName) {
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
            //流水实体映射
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userName", globalAccountInfo.getUserName());
            paramMap.put("accountBookId", accountBookId);
            //查询记录总数
            Integer count = accountCurrentService.selectCount(paramMap);
            jsonObject.put("success", true);
            jsonObject.put("data", count);
            jsonObject.put("dateConverted", null);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询记录总数异常");
            return jsonObject.toString();
        }
    }

    /**
     * 添加流水
     */
    @ResponseBody
    @RequestMapping("/addAccountCurrent")
    public AjaxResult addAccountCurrent(AccountCurrentResult accountCurrentResult) {
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(accountCurrentResult.getIn_ex_status());
            categoryFirst.setFirstCategoryName(accountCurrentResult.getFirst_category_name());
            //查询一级分类id
            Integer firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            //二级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setInExStatus(accountCurrentResult.getIn_ex_status());
            categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
            //二级分类id
            Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
            //金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
            //查询当前金融账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //创建账本流水实体对象
            AccountCurrent accountCurrent = new AccountCurrent();
            //设置用户
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            //设置账本
            accountCurrent.setAccountBookId(accountBookId);
            //设置日期
            accountCurrent.setDate(DateTimeUtil.stringToDate(accountCurrentResult.getDateConverted()));
            //设置二级分类id
            accountCurrent.setSecondCategoryId(secondCategoryId);
            //设置一级分类id
            accountCurrent.setFirstCategoryId(firstCategoryId);
            //设置收支属性
            accountCurrent.setInExStatus(accountCurrentResult.getIn_ex_status());
            //设置金额
            accountCurrent.setAmount(accountCurrentResult.getAmount());
            //设置金融账户id
            accountCurrent.setAccountTypeId(accountTypeId);
            //设置备注
            accountCurrent.setRemarks(accountCurrentResult.getRemarks());
            //添加流水
            accountCurrentService.insert(accountCurrent);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加成功");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("出现异常，请联系管理员");
            return ajaxResult;
        }
    }

    /**
     * 更新流水
     */
    @ResponseBody
    @RequestMapping("/update")
    public AjaxResult update(AccountCurrentResult accountCurrentResult) {
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //设置流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            //设置日期时间
            accountCurrent.setDate(DateTimeUtil.stringToDate(accountCurrentResult.getDateConverted()));
            //金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
            //查询当前金融账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //设置金融账户
            accountCurrent.setAccountTypeId(accountTypeId);
            //一级分类实体
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(accountCurrentResult.getIn_ex_status());
            categoryFirst.setFirstCategoryName(accountCurrentResult.getFirst_category_name());
            //查询一级分类id
            Integer firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            //二级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setInExStatus(accountCurrentResult.getIn_ex_status());
            categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
            //查询当前二级分类id
            Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
            //设置一级分类
            accountCurrent.setFirstCategoryId(firstCategoryId);
            //设置二级分类
            accountCurrent.setSecondCategoryId(secondCategoryId);
            //设置金额明细
            accountCurrent.setAmount(accountCurrentResult.getAmount());
            //设置备注
            accountCurrent.setRemarks(accountCurrentResult.getRemarks());
            //设置 id
            accountCurrent.setId(accountCurrentResult.getId());
            //提交更新
            int success = accountCurrentService.updateByPrimaryKeySelective(accountCurrent);
            if (success == 1) {
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("修改成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改异常，请联系管理员");
            return ajaxResult;
        }
    }

    /**
     * 删除流水
     */
    @ResponseBody
    @RequestMapping("/delete")
    public AjaxResult deleteAccountCurrent(@RequestParam("idList") Integer[] idList) {
        try {
            for (Integer integer : idList) {
                accountCurrentService.deleteByPrimaryKey(integer);
            }
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除成功");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除异常，请联系管理员");
            return ajaxResult;
        }
    }
}
