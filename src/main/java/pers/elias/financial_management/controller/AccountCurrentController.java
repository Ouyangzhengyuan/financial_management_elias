package pers.elias.financial_management.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import javafx.util.converter.DateStringConverter;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.DateRange;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountCurrentService;
import pers.elias.financial_management.service.impl.AccountTypeService;
import pers.elias.financial_management.service.impl.CategorySecondService;
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

    /**
     * 查询所有流水
     */
    @RequestMapping("/accountCurrentList.json")
    @ResponseBody
    public Object accountCurrentList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                     @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                                     String dateSubstring) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", pageno);
        paramMap.put("pagesize", pagesize);
        //反向查询账本id
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        //设置实体字段
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
     * id 查询
     */
    @ResponseBody
    @RequestMapping("/selectById")
    public String selectById(@RequestParam("accountCurrentId") Integer id){
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
     * 添加流水
     */
    @ResponseBody
    @RequestMapping("/addAccountCurrent")
    public AjaxResult addAccountCurrent(AccountCurrentResult accountCurrentResult) {
        try {
            //二级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(globalAccountInfo.getAccountBookId());
            categorySecond.setInExStatus(accountCurrentResult.getIn_ex_status());
            categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
            //查询当前二级分类id
            Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
            //金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
            //查询当前金融账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //创建账本流水实体对象
            AccountCurrent accountCurrent = new AccountCurrent();
            //设置用户
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            //设置账本
            accountCurrent.setAccountBookId(globalAccountInfo.getAccountBookId());
            //设置日期
            accountCurrent.setDate(DateTimeUtil.stringToDate(accountCurrentResult.getDateConverted()));
            //设置二级分类id
            accountCurrent.setSecondCategoryId(secondCategoryId);
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
    public AjaxResult update(AccountCurrentResult accountCurrentResult){
        try{
            //设置流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            //设置日期时间
            accountCurrent.setDate(DateTimeUtil.stringToDate(accountCurrentResult.getDateConverted()));
            //金融账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountType.setAccountTypeName(accountCurrentResult.getAccount_type_name());
            //查询当前金融账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //设置金融账户
            accountCurrent.setAccountTypeId(accountTypeId);
            //二级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(globalAccountInfo.getAccountBookId());
            categorySecond.setInExStatus(accountCurrentResult.getIn_ex_status());
            categorySecond.setSecondCategoryName(accountCurrentResult.getSecond_category_name());
            //查询当前二级分类id
            Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
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
            if(success == 1){
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("修改成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
            return ajaxResult;
        }catch (Exception e){
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
    public AjaxResult deleteAccountCurrent(@RequestParam("accountCurrentId") Integer id) {
        try {
            int success = accountCurrentService.deleteByPrimaryKey(id);
            if (success == 1) {
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("删除成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除异常，请联系管理员");
            return ajaxResult;
        }
    }
}
