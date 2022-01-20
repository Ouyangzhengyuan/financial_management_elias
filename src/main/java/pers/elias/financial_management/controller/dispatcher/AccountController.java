package pers.elias.financial_management.controller.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.component.DateRange;
import pers.elias.financial_management.service.impl.AccountUserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {
    @Autowired
    private DateRange dateRange;

    @Autowired
    private AjaxResult ajaxResult;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    /**
     * 跳转到登录页面
     */
    @RequestMapping("/login.html")
    public String login(HttpServletRequest request, Model model) {
        //实现记住我自动登录跳转到记账页面
        if(!"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return "redirect:/keepAccounts";
        }
        return "login";
    }

    /**
     * 跳转注册页面
     */
    @RequestMapping("/register")
    public String toRegister() {
        return "register";
    }

    /**
     * 跳转资产概况页面
     */
    @RequestMapping("/overView")
    public String overView(Model model) {
        //获取当前用户
        model.addAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
        return "overView/overView";
    }

    /**
     * 登录成功跳转到记账页面
     */
    @RequestMapping("/keepAccounts")
    public String keepAccounts(HttpServletRequest request, Model model) {
        //获取当前用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置显示当前用户
        model.addAttribute("userName", userName);
        request.setAttribute("userName", userName);
        //设置账目清单日期范围
        model.addAttribute("dateRange", dateRange.getDateRange());
        return "keepAccounts/keepAccounts";
    }

    /**
     * 跳转账户页面
     */
    @RequestMapping("/account")
    public String account(Model model) {
        //获取当前用户
        model.addAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
        return "account/account";
    }

    /**
     * 跳转到设置页面
     */
    @RequestMapping("/setting")
    public String toSet(Model model) {
        //当前用户名
        model.addAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
        return "set/setting";
    }

    /**
     * 跳转账本初始化
     */
    @RequestMapping("/initAccountBook")
    public String initAccountBook(){
        return "initAccountBook";
    }

    /**
     * 跳转添加账本
     */
    @RequestMapping("/addAccountBook")
    public String addAccountBook(String inExStatus) {
        return "keepAccounts/addAccountBook";
    }

    /**
     * 跳转编辑账本
     */
    @RequestMapping("/editAccountBook")
    public String editAccountBook(@ModelAttribute("accountBookName") String accountBookName) {
        return "set/editAccountBook";
    }

    /**
     * 跳转添加分类
     */
    @RequestMapping("/addCategory")
    public String addCategory(@ModelAttribute("inExStatus") String inExStatus) {
        return "keepAccounts/addCategory";
    }

    /**
     * 跳转编辑分类
     */
    @RequestMapping("/editCategory")
    public String editCategory(@ModelAttribute("categoryName") String categoryName, @ModelAttribute("inExStatus") String inExStatus, @ModelAttribute("categoryType") String categoryType) {
        return "set/editCategory";
    }

    /**
     * 跳转添加金融账户
     */
    @RequestMapping("/addAccountFinancial")
    public String addAccountFinancial() {
        return "keepAccounts/addAccountFinancial";
    }

    /**
     * 跳转添加二级金融账户
     */
    @RequestMapping("/addAccountType")
    public String addAccountType() {
        return "account/addAccountType";
    }

    /**
     * 跳转添加模板
     */
    @RequestMapping("/addCategoryTemplate")
    public String addCategoryTemplate(@ModelAttribute("inExStatus") String inExStatus) {
        return "keepAccounts/addCategoryTemplate";
    }

    /**
     * 跳转编辑模板
     */
    @RequestMapping("/editCategoryTemplate")
    public String editCategoryTemplate(
            @ModelAttribute("templateName") String templateName,
            @ModelAttribute("accountTypeName") String accountTypeName,
            @ModelAttribute("firstCategoryName") String firstCategoryName,
            @ModelAttribute("secondCategoryName") String secondCategoryName,
            @ModelAttribute("inExStatus") String inExStatus) {
        return "set/editCategoryTemplate";
    }

    /**
     * 跳转编辑本行流水
     */
    @RequestMapping(value = "/editAccountCurrent")
    public String editAccountCurrent(@ModelAttribute("accountCurrentId") Integer accountCurrentId) {
        return "keepAccounts/editAccountCurrent";
    }

    /**
     * 跳转到报表
     */
    @RequestMapping("/statement")
    public String statement(Model model){
        //获取当前用户
        model.addAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
        return "statement/statement";
    }
}
