package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountFinancial;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountFinancialService;
import pers.elias.financial_management.service.impl.AccountIndexService;
import pers.elias.financial_management.service.impl.AccountTypeService;

import java.util.List;

@Controller
@RequestMapping("/accountType")
public class AccountTypeController {
    @Autowired
    private AjaxResult ajaxResult;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    @Autowired //金融账户服务
    private AccountTypeService accountTypeService;

    @Autowired //当前用户全局信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 查询金融账户列表
     */
    @RequestMapping("/accountType.json")
    @ResponseBody
    public String accountType(String accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(accountBookName != null){
                globalAccountInfo.setAccountBookName(accountBookName);
            }
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //查询想要限制的负债账户
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName("负债账户");
            Integer limitAccountFinancialId = accountFinancialService.selectId(accountFinancial);
            //二级金融账户实体
            AccountType accountType = new AccountType();
            accountType.setAccountBookId(accountBookId);
            //添加限制条件id
            accountType.setLimitAccountFinancialId(limitAccountFinancialId);
            //查询二级金融账户列表
            List<String> accountTypeList = accountTypeService.selectAccountTypeName(accountType);
            jsonObject.put("success", true);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", accountTypeList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "操作失败");
            return jsonObject.toString();
        }
    }

    /**
     * 添加一二级金融账户
     */
    @RequestMapping("/addAccountFinancial.do")
    @ResponseBody
    public AjaxResult addAccountFinancial(String accountFinancialName, String accountTypeName) {
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级账户实体
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName(accountFinancialName);
            //查询一级账户id
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            //二级账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            accountType.setFinancialAccountId(accountFinancialId);
            //判断账户是否存在
            if (!accountTypeService.isExists(accountType)) {
                accountTypeService.insert(accountType);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("添加成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("该金融账户已存在");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加失败");
            return ajaxResult;
        }
    }

    /**
     * 添加二级金融账户
     */
    @RequestMapping("/addAccountTy.do")
    @ResponseBody
    public AjaxResult addAccountType(String accountTypeName) {
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //反向查询一级金融账户 id
            AccountFinancial accountFinancial = new AccountFinancial();
            accountFinancial.setUserName(globalAccountInfo.getUserName());
            accountFinancial.setAccountBookId(accountBookId);
            accountFinancial.setFinancialAccountName(globalAccountInfo.getAccountFinancialIndex());
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            //设置账户实体对象
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            accountType.setFinancialAccountId(accountFinancialId);
            //判断账户是否存在
            if (!accountTypeService.isExists(accountType)) {
                accountTypeService.insert(accountType);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("添加成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("该金融账户已存在");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加失败");
            return ajaxResult;
        }
    }

    /**
     * 修改二级金融名称
     */
    @ResponseBody
    @RequestMapping("/editAccountType")
    public String editAccountType(String accountBookName, String accountTypeNameOld, String accountTypeNameNew){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeNameOld);
            //查询当前账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            accountType.setId(accountTypeId);
            accountType.setAccountTypeName(accountTypeNameNew);//更新名称
            accountTypeService.updateByPrimaryKeySelective(accountType);//执行更改
            jsonObject.put("success", true);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "更新异常");
            return jsonObject.toString();
        }
    }

    /**
     * 删除二级金融账户
     */
    @ResponseBody
    @RequestMapping("/deleteAccountType")
    public String deleteAccountType(String accountBookName, String accountFinancialName, String accountTypeName){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //子账户实体
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            //反向查询子账户id
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //执行删除
            if( accountTypeService.deleteByPrimaryKey(accountTypeId) == 1){
                jsonObject.put("success", true);
                jsonObject.put("msg", "删除成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "删除失败");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除异常");
            return jsonObject.toString();
        }
    }
}
