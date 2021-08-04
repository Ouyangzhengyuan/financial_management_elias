package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.TableReload;

@Controller
@RequestMapping("/tableReload")
public class TableReloadController {
    @Autowired
    private TableReload tableReload;

    @ResponseBody
    @RequestMapping("/getTableReload")
    public Object getTableReload(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("data", tableReload.getIsTableReload());
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("/setTableReload")
    public Object setTableReload(boolean isTableReload){
        JSONObject jsonObject = new JSONObject();
        try{
            //设置编辑模块的表格重载
            tableReload.setIsTableReload(isTableReload);
            jsonObject.put("success", true);
            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "TableReload 设置异常");
            return jsonObject;
        }
    }
}
