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
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountCurrentService;
import pers.elias.financial_management.utils.PageBean;

import java.util.List;

@Controller
@RequestMapping("/accountCurrent")
public class AccountCurrentController {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private AccountBookService accountBookService;

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
    public AjaxResult addAccountCurrent(AccountCurrentResult accountCurrentResult){
        accountCurrentResult.setUser_name(globalAccountInfo.getUserName());
        accountCurrentResult.setAccount_book_id(globalAccountInfo.getAccountBookId());
        accountCurrentResult.setIn_ex_status('支');
        System.out.println(accountCurrentResult.toString());
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(true);
        return ajaxResult;
    }
}
