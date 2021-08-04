package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.impl.AccountBookService;
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
            //反向查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //查询金融账户
            List<String> accountTypeList = accountTypeService.selectAccountTypeName(accountBookId);
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
     * 添加金融账户
     */
    @RequestMapping("/addAccountTy.do")
    @ResponseBody
    public AjaxResult addAccountType(String accountTypeName) {
        try {
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //设置账户实体对象
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
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
}
