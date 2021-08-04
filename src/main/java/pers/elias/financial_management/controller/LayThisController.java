package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.service.impl.AccountBookService;

@Controller
@RequestMapping("/layThis")
public class LayThisController {
    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @ResponseBody
    @RequestMapping("/select")
    public Object getLayThis(String layThisName) {
        if(globalAccountInfo.getLayThisName() == null){
            globalAccountInfo.setLayThisName(accountBookService.selectAllByUserName(globalAccountInfo.getUserName()).get(0));
        }else if(!layThisName.isEmpty()){
            globalAccountInfo.setLayThisName(layThisName);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("layThisName", globalAccountInfo.getLayThisName());
        return jsonObject;
    }
}
