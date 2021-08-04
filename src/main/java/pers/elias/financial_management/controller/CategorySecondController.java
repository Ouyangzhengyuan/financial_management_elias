package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/category")
public class CategorySecondController {
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
     * 查询二级分类
     */
    @RequestMapping("/secondCategoryList")
    @ResponseBody
    public String secondCategoryList(String firstCategoryName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(firstCategoryName == null){
                jsonObject.put("code", 200);
                jsonObject.put("msg", "查询二级分类失败");
                return jsonObject.toString();
            }
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体对象
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            categoryFirst.setFirstCategoryName(firstCategoryName);
            //查询一级分类id
            Integer firstCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
            //查询二级支出 渲染列表
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setFirstCategoryId(firstCategoryId);
            categorySecond.setInExStatus(inExStatus);
            List<String> secondCategoryList = categorySecondService.selectAllByCategorySecond(categorySecond);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", secondCategoryList);
            return jsonObject.toString();
        } catch (Exception e) {
            jsonObject.put("code", 200);
            jsonObject.put("msg", "二级分类查询失败");
            return jsonObject.toString();
        }
    }

    /**
     * 查询所有二级分类（支出 + 收入）
     */
    @RequestMapping("/secondCategoryAllList")
    @ResponseBody
    public Object secondCategoryAllList(String firstCategoryName){
        JSONObject jsonObject = new JSONObject();
        try {
            if(firstCategoryName == null){
                jsonObject.put("code", 200);
                jsonObject.put("msg", "查询二级分类失败");
                return jsonObject.toString();
            }
            //反向查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体对象
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setFirstCategoryName(firstCategoryName);
            //查询一级分类id
            Integer firstCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
            //查询二分支出分类
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setFirstCategoryId(firstCategoryId);
            List<String> secondCategoryList = categorySecondService.selectAllByCategorySecond(categorySecond);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", secondCategoryList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "查询失败");
            return jsonObject.toString();
        }
    }



    /**
     * 添加二级支出
     */
    @RequestMapping("/addSecondCategory.do")
    @ResponseBody
    public AjaxResult addSecondExCategory(String firstCategoryName, String secondCategoryName, String inExStatus) {
            try {
                //反向查询当前账本id
                Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
                //反向查询一级支出id
                CategoryFirst categoryFirst = new CategoryFirst();
                categoryFirst.setAccountBookId(accountBookId);
                categoryFirst.setInExStatus(inExStatus);
                categoryFirst.setFirstCategoryName(firstCategoryName);
                Integer firstExCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
                //添加二级支出
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setFirstCategoryId(firstExCategoryId);
                categorySecond.setSecondCategoryName(secondCategoryName.trim());
                categorySecond.setInExStatus(inExStatus);
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
}
