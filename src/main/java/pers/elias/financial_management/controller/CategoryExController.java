package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.component.AjaxResult;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.CategoryFirstService;
import pers.elias.financial_management.service.impl.CategorySecondService;

import java.util.List;

@Controller
@RequestMapping("/categoryEx")
public class CategoryExController {
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
     * 查询一级支出
     */
    @RequestMapping("/firstExpenseCategory.json")
    @ResponseBody
    public String firstExpenseCategory(String accountBookName) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (accountBookName != null) {
                globalAccountInfo.setAccountBookName(accountBookName);
            }
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            globalAccountInfo.setAccountBookId(accountBookId);
            //查询一级支出
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus("支");
            //响应头
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", categoryFirstService.selectAllByCategoryFirst(categoryFirst));
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "一级分类读取失败");
            return jsonObject.toString();
        }
    }

    /**
     * 添加一级支出
     */
    @RequestMapping("/addFirstExCategory.do")
    @ResponseBody
    public AjaxResult addFirstCategory(String firstCategoryName) {
        try {
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //添加一级支出
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName()); //绑定当前用户
            categoryFirst.setAccountBookId(accountBookId); //绑定当前账本id
            categoryFirst.setInExStatus("支"); //绑定支出属性
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

    /**
     * 查询二级支出
     * 渲染二级支出
     */
    @RequestMapping("/secondExpenseCategory.json")
    @ResponseBody
    public String secondExpenseCategory(String firstExpenseCategory) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(firstExpenseCategory == null){
                jsonObject.put("code", 200);
                jsonObject.put("msg", "查询二级分类失败");
                return jsonObject.toString();
            }
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体对象
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus("支");
            categoryFirst.setFirstCategoryName(firstExpenseCategory);
            //查询一级分类id
            Integer firstCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
            //查询二级支出 渲染列表
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setFirstCategoryId(firstCategoryId);
            categorySecond.setInExStatus("支");
            List<String> secondCategoryList = categorySecondService.selectAllByCategorySecond(categorySecond);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", secondCategoryList);
            return jsonObject.toString();
        } catch (Exception e) {
            jsonObject.put("code", 200);
            jsonObject.put("msg", "查询失败");
            return jsonObject.toString();
        }
    }


    /**
     * 添加二级支出
     */
    @RequestMapping("/addSecondExCategory.do")
    @ResponseBody
    public AjaxResult addSecondExCategory(String firstExCategoryName, String secondCategoryName) {
        if (firstExCategoryName != null && !firstExCategoryName.trim().isEmpty()) {
            try {
                //反向查询当前账本id
                Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
                //反向查询一级支出id
                CategoryFirst categoryFirst = new CategoryFirst();
                categoryFirst.setAccountBookId(accountBookId);
                categoryFirst.setInExStatus("支");
                categoryFirst.setFirstCategoryName(firstExCategoryName);
                Integer firstExCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
                //添加二级支出
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setFirstCategoryId(firstExCategoryId);
                categorySecond.setSecondCategoryName(secondCategoryName.trim());
                categorySecond.setInExStatus("支");
                if (categorySecondService.isExists(categorySecond)) {
                    categorySecondService.insert(categorySecond);
                    ajaxResult.setSuccess(true);
                    ajaxResult.setMessage("添加成功");
                    return ajaxResult;
                }
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("该类目已存在！");
                return ajaxResult;
            } catch (Exception e) {
                e.printStackTrace();
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("添加失败");
                return ajaxResult;
            }
        }
        ajaxResult.setSuccess(false);
        ajaxResult.setMessage("请选择一个账本！");
        return ajaxResult;
    }
}
