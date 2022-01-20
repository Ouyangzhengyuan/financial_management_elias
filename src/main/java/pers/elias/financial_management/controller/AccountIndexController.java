package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountIndex;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountFinancialService;
import pers.elias.financial_management.service.impl.AccountIndexService;

/**
 * 用户的账本、一级金融账户索引读取、设置控制器
 */
@Controller
@RequestMapping("/accountIndex")
public class AccountIndexController {
    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountIndexService accountIndexService;


    /**
     * 判断账本初始化
     */
    @ResponseBody
    @RequestMapping("/accountBookInt")
    public String accountBookInt(){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //获取当前用户账本是否初始化
            Integer isAccountBookInt = accountIndexService.selectByPrimaryKey(userName).getAccountBookInit();
            jsonObject.put("success", true);
            jsonObject.put("isAccountBookInt", isAccountBookInt);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            return jsonObject.toString();
        }
    }

    /**
     * 设置账本初始化
     */
    @ResponseBody
    @RequestMapping("/setAccountBookInt")
    public String setAccountBookInt(){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //索引实体
            AccountIndex accountIndex = new AccountIndex();
            accountIndex.setUserName(userName);
            accountIndex.setAccountBookInit(1);
            //标识用户账户已初始化，无需再弹出界面
            accountIndexService.updateByPrimaryKeySelective(accountIndex);
            jsonObject.put("success", true);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            return jsonObject.toString();
        }
    }

    /**
     * 账本索引
     */
    @ResponseBody
    @RequestMapping("/accountBookIndex")
    public Object getLayThis(String accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前上下登录环境用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //查询账本索引
            String index = accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex();
            boolean isExists = false;
            for(String accountBook: accountBookService.selectAllByUserName(userName)){
                if(index != null && index.equals(accountBook)){
                        isExists = true;
                        break;
                }
            }
            //如果不存在那么就默认将用户的第一个账本设置为索引
            if (index == null || !isExists) {
                //查询账本
                String defaultIndex = accountBookService.selectAllByUserName(userName).get(0);
                //账户索引实体
                AccountIndex accountIndex = new AccountIndex();
                accountIndex.setUserName(userName);
                accountIndex.setAccountBookIndex(defaultIndex);
                //持久化索引值
                accountIndexService.updateByPrimaryKeySelective(accountIndex);
                //查询当前账本索引
                String nowIndex = accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex();
                //设置全局索引值
                globalAccountInfo.setAccountBookIndex(nowIndex);
                //设置当前账本
                globalAccountInfo.setAccountBookName(nowIndex);
                jsonObject.put("success", true);
                jsonObject.put("accountBookIndex", globalAccountInfo.getAccountBookIndex());
                return jsonObject.toString();
            } else if (accountBookName != null && !accountBookName.isEmpty()) {
                //账户索引实体
                AccountIndex accountIndex = new AccountIndex();
                accountIndex.setUserName(userName);
                accountIndex.setAccountBookIndex(accountBookName);
                //持久化索引值
                accountIndexService.updateByPrimaryKeySelective(accountIndex);
                //查询当前账本索引
                String nowIndex = accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex();
                //设置全局索引值
                globalAccountInfo.setAccountBookIndex(nowIndex);
                //设置当前账本
                globalAccountInfo.setAccountBookName(nowIndex);
                jsonObject.put("success", true);
                jsonObject.put("accountBookIndex", globalAccountInfo.getAccountBookIndex());
                return jsonObject.toString();
            } else {
                //查询当前账本索引
                String nowIndex = accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex();
                //设置全局索引值
                globalAccountInfo.setAccountBookIndex(nowIndex);
                //设置当前账本
                globalAccountInfo.setAccountBookName(nowIndex);
                jsonObject.put("success", true);
                jsonObject.put("accountBookIndex", globalAccountInfo.getAccountBookIndex());
                return jsonObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            return jsonObject.toString();
        }
    }

    /**
     * 一级金融账户索引
     */
    @ResponseBody
    @RequestMapping("/accountFinancialIndex")
    public String accountFinancialIndex(String accountFinancialName) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前上下登录环境用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //查询金融账户索引
            String index = accountIndexService.selectByPrimaryKey(userName).getAccountFinancialIndex();
            //如果金融账户索引不存在，那么就将 "现金账户" 设置为索引
            if (index == null) {
                //账户索引实体
                AccountIndex accountIndex = new AccountIndex();
                accountIndex.setUserName(userName);
                accountIndex.setAccountFinancialIndex("现金账户");
                //持久化金融账户索引
                accountIndexService.updateByPrimaryKeySelective(accountIndex);
                //设置全局金融账户索引
                globalAccountInfo.setAccountFinancialIndex(accountIndexService.selectByPrimaryKey(userName).getAccountFinancialIndex());
                jsonObject.put("accountFinancialIndex", globalAccountInfo.getAccountFinancialIndex());
                return jsonObject.toString();
            } else if (accountFinancialName != null && !accountFinancialName.isEmpty()) {
                //账户索引实体
                AccountIndex accountIndex = new AccountIndex();
                accountIndex.setUserName(userName);
                accountIndex.setAccountFinancialIndex(accountFinancialName);
                //持久化金融账户索引
                accountIndexService.updateByPrimaryKeySelective(accountIndex);
                //设置全局金融账户索引
                globalAccountInfo.setAccountFinancialIndex(accountIndexService.selectByPrimaryKey(userName).getAccountFinancialIndex());
                jsonObject.put("accountFinancialIndex", globalAccountInfo.getAccountFinancialIndex());
                return jsonObject.toString();
            }else{
                //设置全局金融账户索引
                globalAccountInfo.setAccountFinancialIndex(accountIndexService.selectByPrimaryKey(userName).getAccountFinancialIndex());
                jsonObject.put("accountFinancialIndex", globalAccountInfo.getAccountFinancialIndex());
                return jsonObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "金融账户索引异常");
            return jsonObject.toString();
        }
    }
}
