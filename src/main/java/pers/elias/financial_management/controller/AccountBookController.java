package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.bean.GlobalAccountInfoTemp;
import pers.elias.financial_management.model.AccountBook;
import pers.elias.financial_management.model.template_account_book.impl.BusinessTemplate;
import pers.elias.financial_management.model.template_account_book.impl.DailyTemplate;
import pers.elias.financial_management.model.template_account_book.impl.TravelTemplate;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountFinancialService;
import pers.elias.financial_management.service.impl.AccountIndexService;
import pers.elias.financial_management.service.impl.CategoryFirstService;

import javax.crypto.SecretKey;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 账本管理
 */
@Controller
@RequestMapping("/accountBook")
@Api(tags = "账本管理接口：AccountBookController", description = "账本管理")
public class AccountBookController {
    @Autowired //异步更新
    private AjaxResult ajaxResult;

    @Autowired //账户信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private GlobalAccountInfoTemp globalAccountInfoTemp;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountIndexService accountIndexService;

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountBookController.class);

    /**
     * 查询账本
     */
    @ResponseBody
    @ApiOperation("获取当前用户所有账本列表")
    @RequestMapping(value = "/accountBookList", method = RequestMethod.POST)
    public String accountBook(HttpServletRequest request) {
        //设置当前登录的用户
        globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        //查询当前用户的所有账本列表
        List<String> accountBookList = accountBookService.selectAllByUserName(globalAccountInfo.getUserName());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "操作成功");
        jsonObject.put("data", accountBookList);
        return jsonObject.toString();
    }

    /**
     * 账本初始化
     */
    @ResponseBody
    @ApiOperation("账本初始化")
    @RequestMapping(value = "/initAccountBook.do", method = RequestMethod.POST)
    public String initAccountBook(String daily, String travel, String business) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //账本实体
            AccountBook accountBook = new AccountBook();
            accountBook.setUserName(globalAccountInfo.getUserName());
            //判断用户需要初始化的账本
            if (daily != null) {
                accountBook.setName(daily);
                accountBookService.initAccountBook(accountBook, new DailyTemplate());
            }
            if (travel != null) {
                accountBook.setName(travel);
                accountBookService.initAccountBook(accountBook, new TravelTemplate());
            }
            if (business != null) {
                accountBook.setName(business);
                accountBookService.initAccountBook(accountBook, new BusinessTemplate());
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "创建成功");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "账本创建失败");
            return jsonObject.toString();
        }
    }

    /**
     * 自定义添加账本
     */
    @ResponseBody
    @ApiOperation("添加账本")
    @RequestMapping(value = "/addAccountBook.do", method = RequestMethod.POST)
    public String addAccountBook(@Validated String accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //判断账本是否存在
            if (!accountBookService.isExists(globalAccountInfo.getUserName(), accountBookName)) {
                //如果账本不存在就执行添加
                AccountBook accountBook = new AccountBook();
                accountBook.setUserName(globalAccountInfo.getUserName());
                accountBook.setName(accountBookName);
                accountBookService.insert(accountBook, new DailyTemplate());
                jsonObject.put("success", true);
                jsonObject.put("msg", "添加成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "该账本已存在同名");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "添加异常");
            return jsonObject.toString();
        }
    }

    /**
     * 编辑账本
     */
    @ResponseBody
    @ApiOperation("编辑账本")
    @RequestMapping(value = "/editAccountBook.do", method = RequestMethod.POST)
    public String editAccountBook(String oldAccountBookName, String newAccountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //查询账本 id
            globalAccountInfo.setAccountBookName(oldAccountBookName);
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //账本实体
            AccountBook accountBook = new AccountBook();
            accountBook.setId(accountBookId);
            accountBook.setUserName(globalAccountInfo.getUserName());
            accountBook.setName(newAccountBookName);
            //判断账本
            if (oldAccountBookName.equals(newAccountBookName)) {
                //执行修改
                accountBookService.updateByPrimaryKey(accountBook);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            } else if (!accountBookService.isExists(globalAccountInfo.getUserName(), newAccountBookName)) {
                //执行修改
                accountBookService.updateByPrimaryKey(accountBook);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "输入的账本已存在同名");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }
    }

    /**
     * 删除账本
     */
    @ResponseBody
    @ApiOperation("删除账本")
    @RequestMapping(value = "/deleteAccountBook.do", method = RequestMethod.POST)
    public String deleteAccountBook(String[] accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            for (int i = 0; i < accountBookName.length; i++) {
                //查询账本 id
                globalAccountInfo.setUserName(globalAccountInfo.getUserName());
                globalAccountInfo.setAccountBookName(accountBookName[i]);
                Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
                //执行删除
                accountBookService.deleteByPrimaryKey(accountBookId);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除成功");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }
    }
}
