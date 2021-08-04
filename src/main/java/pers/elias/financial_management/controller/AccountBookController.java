package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountBook;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.RedisService;

import java.util.List;

/**
 * 账本控制器
 */
@Controller
@RequestMapping("/accountBook")
public class AccountBookController {
    @Autowired //异步更新
    private AjaxResult ajaxResult;

    @Autowired //账户信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    /**
     * 查询账本
     */
    @ResponseBody
    @RequestMapping("/accountBookList")
    public String accountBook() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "操作成功");
        jsonObject.put("data", accountBookService.selectAllByUserName(globalAccountInfo.getUserName()));
        return jsonObject.toString();
    }

    /**
     * 添加账本
     */
    @RequestMapping("/addAccountBook.do")
    @ResponseBody
    public AjaxResult addAccountBook(String accountBookName) {
        try {
            //判断账本是否存在
            if (accountBookService.hasAccountBook(globalAccountInfo.getUserName(), accountBookName.trim())) {
                //匹配当前用户和账本
                AccountBook accountBook = new AccountBook();
                accountBook.setUserName(globalAccountInfo.getUserName());
                accountBook.setName(accountBookName);
                accountBookService.insert(accountBook);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("添加成功！");
            } else {
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("该账本已存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("服务器出错，请联系管理员！");
        }
        return ajaxResult;
    }
}
