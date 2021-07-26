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
            AccountCurrent accountCurrent = new AccountCurrent();
            accountCurrent.setUserName(globalAccountInfo.getUserName());
            accountCurrent.setAccountBookId(globalAccountInfo.getAccountBookId());
            accountCurrent.setInExStatus("支");
            accountCurrent.setDateConverted(dateSubstring);
            Double monthlyExpense = totalAmountCalculate.getMonthlyExpense(accountCurrent);
            jsonObject.put("success", true);
            jsonObject.put("msg", "查询月支出成功");
            jsonObject.put("data", monthlyExpense);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", true);
            jsonObject.put("msg", "查询月支出失败");
            return jsonObject.toString();
        }
    }
}
