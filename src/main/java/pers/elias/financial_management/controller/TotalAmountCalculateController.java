package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.component.TotalAmountCalculate;
import pers.elias.financial_management.model.AccountCurrent;

@Controller
@RequestMapping("/totalAmountCalculate")
public class TotalAmountCalculateController {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private TotalAmountCalculate totalAmountCalculate;

    /**
     * 获取月支出总额
     */
    @ResponseBody
    @RequestMapping("/getMonthlyExpense")
    public String getMonthlyExpense(String dateSubstring){
        JSONObject jsonObject = new JSONObject();
        try{
            //账本流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountCurrent.setInExStatus("支");
            accountCurrent.setDateConverted(dateSubstring);
            //统计月支出总额
            Double monthlyExpense = totalAmountCalculate.getMonthlyExpense(accountCurrent);
            jsonObject.put("success", true);
            jsonObject.put("data", monthlyExpense);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询月支出失败");
            return jsonObject.toString();
        }
    }

    /**
     * 获取月收入总额
     */
    @ResponseBody
    @RequestMapping("/getMonthlyIncome")
    public String getMonthlyIncome(String dateSubstring){
        JSONObject jsonObject = new JSONObject();
        try{
            //账本流水实体
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountCurrent.setInExStatus("收");
            accountCurrent.setDateConverted(dateSubstring);
            //统计月收入总额
            Double monthlyIncome = totalAmountCalculate.getMonthlyIncome(accountCurrent);
            jsonObject.put("success", true);
            jsonObject.put("data", monthlyIncome);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询月收入失败");
            return jsonObject.toString();
        }
    }

    /**
     * 获取月结余总额
     */
    @ResponseBody
    @RequestMapping("/getMonthlyBalance")
    public String getMonthlyBalance(String dateSubstring){
        JSONObject jsonObject = new JSONObject();
        try{
            //账本流水实体 收入
            AccountCurrent accountCurrentIncome = new AccountCurrent();
            accountCurrentIncome.setUserName(globalAccountInfo.getUserName());
            accountCurrentIncome.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountCurrentIncome.setInExStatus("收");
            accountCurrentIncome.setDateConverted(dateSubstring);
            //账本流水实体 支出
            AccountCurrent accountCurrentExpense = new AccountCurrent();
            accountCurrentExpense.setUserName(globalAccountInfo.getUserName());
            accountCurrentExpense.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountCurrentExpense.setInExStatus("支");
            accountCurrentExpense.setDateConverted(dateSubstring);
            //统计月结余
            Double monthlyBalance = totalAmountCalculate.getMonthlyBalance(accountCurrentIncome, accountCurrentExpense);
            jsonObject.put("success", true);
            jsonObject.put("data", monthlyBalance);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "查询月结余失败");
            return jsonObject.toString();
        }
    }

}
