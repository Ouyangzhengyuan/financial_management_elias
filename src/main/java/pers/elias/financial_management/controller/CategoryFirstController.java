package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.CategoryFirstService;
import pers.elias.financial_management.service.impl.CategorySecondService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryFirstController {
    @Autowired //异步更新
    private AjaxResult ajaxResult;

    @Autowired //全局账户信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    @Autowired //一级分类服务
    private CategoryFirstService categoryFirstService;

    @Autowired //二级分类服务
    private CategorySecondService categorySecondService;

    /**
     * 查询一级分类（支出 or 收入）
     */
    @RequestMapping("/firstCategoryList")
    @ResponseBody
    public Object firstCategoryList(String accountBookName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(accountBookName != null){
                globalAccountInfo.setAccountBookName(accountBookName);
            }
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            globalAccountInfo.setAccountBookId(accountBookId);
            //查询一级支出
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            //响应头
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", categoryFirstService.selectAllByCategoryFirst(categoryFirst));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "一级分类读取失败");
            return jsonObject;
        }
    }

    /**
     * 查询所有分类（支出 + 收入）
     */
    @RequestMapping("/firstCategoryAllList")
    @ResponseBody
    public Object firstCategoryAllList(String accountBookName){
        JSONObject jsonObject = new JSONObject();
        try {
            //查询一级支出分类
            globalAccountInfo.setAccountBookName(accountBookName);
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            globalAccountInfo.setAccountBookId(accountBookId);
            //查询一级支出
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus("支");
            List<String> firstCategoryList = new ArrayList<>();
            firstCategoryList.addAll(categoryFirstService.selectAllByCategoryFirst(categoryFirst));
            //查询一级收入分类
            categoryFirst.setInExStatus("收");
            firstCategoryList.addAll(categoryFirstService.selectAllByCategoryFirst(categoryFirst));
            //响应头
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", firstCategoryList);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "一级分类读取失败");
            return jsonObject;
        }
    }

    /**
     * 添加一级分类
     */
    @RequestMapping("/addFirstCategory.do")
    @ResponseBody
    public AjaxResult addFirstCategory(String firstCategoryName, String inExStatus) {
        try {
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //添加一级支出
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName()); //绑定当前用户
            categoryFirst.setAccountBookId(accountBookId); //绑定当前账本id
            categoryFirst.setInExStatus(inExStatus); //绑定支出属性
            categoryFirst.setFirstCategoryName(firstCategoryName); //绑定一级分类
            //判断一级分类是否存在
            if (categoryFirstService.firstCategoryExists(categoryFirst)) {
                categoryFirstService.insert(categoryFirst);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("添加成功！");
            } else {
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("该类目已存在！请重新添加！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("请选择一个账本！");
            return ajaxResult;
        }
        return ajaxResult;
    }
}
