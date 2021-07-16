package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountCurrentService;
import pers.elias.financial_management.service.impl.AccountTypeService;
import pers.elias.financial_management.service.impl.CategorySecondService;
import pers.elias.financial_management.utils.DateStringConvert;
import pers.elias.financial_management.utils.PageBean;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/accountCurrent")
public class AccountCurrentController {
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
    public String accountCurrentList(String accountBookName){
        //反向查询账本id
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        //查询流水
        AccountCurrent accountCurrent = new AccountCurrent();
        accountCurrent.setUserName(globalAccountInfo.getUserName());
        accountCurrent.setAccountBookId(accountBookId);
        accountCurrent.setInExStatus("支");
        List<AccountCurrentResult> accountCurrentResultList = accountCurrentService.selectAllByAccountCurrent(accountCurrent);
        PageBean<AccountCurrentResult> pageBean = new PageBean<AccountCurrentResult>(1, 5);
        pageBean.setDatas(accountCurrentResultList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "code", 0);
        jsonObject.put("msg", "");
        jsonObject.put("count",pageBean.getTotalsize());
        jsonObject.put("data", pageBean.getDatas());
        return jsonObject.toString();
    }

    /**
     * 添加流水
     */
    @ResponseBody
    @RequestMapping("/addAccountCurrent")
    public AjaxResult addAccountCurrent(AccountCurrentResult accountCurrentResult) {
        try{
            //二级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(globalAccountInfo.getAccountBookId());
            categorySecond.setInExStatus("支");
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
            accountCurrent.setDate(DateStringConvert.stringToDate(accountCurrentResult.getDateConverted()));
            //设置二级分类id
            accountCurrent.setSecondCategoryId(secondCategoryId);
            //设置支出
            accountCurrent.setInExStatus("支");
            //设置金额
            accountCurrent.setAmount(accountCurrentResult.getAmount());
            //设置金融账户id
            accountCurrent.setAccountTypeId(accountTypeId);
            //设置备注
            accountCurrentService.insert(accountCurrent);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加成功！");
            return ajaxResult;
        } catch (ParseException e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("添加失败！");
            return ajaxResult;
        }
    }
}
