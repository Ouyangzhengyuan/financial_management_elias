package pers.elias.financial_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.DateRange;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountTypeService;
import pers.elias.financial_management.service.impl.CategoryFirstService;
import pers.elias.financial_management.service.impl.CategorySecondService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/keepAccounts")
public class KeepAccountsController {
    @Autowired //异步更新
    private AjaxResult ajaxResult;

    @Autowired
    private DateRange dateRange;

    @Autowired //全局账户信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    @Autowired //一级分类服务
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired //金融账户服务
    private AccountTypeService accountTypeService;

    /**
     * 登陆成功后跳转指定页面
     */
    @RequestMapping("/toKeepAccounts")
    public String keepAccounts(HttpServletRequest request, Model model) {
        //获取当前日期和时间
//        String currentDate = DateTimeUtil.getSimpleDateTime();
        //获取当前用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //添加全局用户
        globalAccountInfo.setUserName(userName);
        //设置显示当前用户
        request.setAttribute("userName", userName);
        //设置表单当前日期
//        model.addAttribute("date", currentDate);
        //账目清单日期范围
        model.addAttribute("dateRange", dateRange.getDateRange());
        return "keepAccounts/keepAccounts";
    }

    /**
     * 跳转添加账本对话框
     */
    @RequestMapping("/addAccountBook")
    public String addAccountBook() {
        return "keepAccounts/addAccountBook";
    }

    /**
     * 跳转添加支出分类
     */
    @RequestMapping("/addExpenseCategory")
    public String addFirstCategory() {
        return "keepAccounts/addExpenseCategory";
    }

    /**
     * 跳转到添加金融账户
     */
    @RequestMapping("/addAccountType")
    public String addAccountType(){
        return "keepAccounts/addAccountType";
    }

    /**
     * 跳转添加模板
     */
    @RequestMapping("/addCategoryTemplate")
    public String addCategoryTemplate(){
        return "keepAccounts/addCategoryTemplate";
    }

    /**
     * 跳转编辑本行流水
     */
    @RequestMapping(value = "/editAccountCurrent")
    public String editAccountCurrent(@ModelAttribute("accountCurrentId") Integer accountCurrentId){
        return "keepAccounts/editAccountCurrent";
    }
}
