package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountIndexService;
import pers.elias.financial_management.service.impl.CategoryFirstService;
import pers.elias.financial_management.service.impl.CategorySecondService;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 查询二级分类
     */
    @RequestMapping("/secondCategoryList")
    @ResponseBody
    public String secondCategoryList(String firstCategoryName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
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
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
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
     * 添加二级收支分类
     */
    @RequestMapping("/addSecondCategory.do")
    @ResponseBody
    public AjaxResult addSecondExCategory(String firstCategoryName, String secondCategoryName, String inExStatus) {
            try {
                //获取当前登录的用户
                String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                //设置当前登录的用户
                globalAccountInfo.setUserName(userName);
                //设置当前用户账本
                globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
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
                if (!categorySecondService.isExists(secondCategoryName, categorySecond)) {
                    categorySecond.setSecondCategoryName(secondCategoryName.trim());
                    categorySecond.setInExStatus(inExStatus);
                    categorySecond.setFirstCategoryId(firstExCategoryId);
                    categorySecondService.insert(categorySecond);
                    ajaxResult.setSuccess(true);
                    ajaxResult.setMessage("添加成功");
                    return ajaxResult;
                }
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("支出或收入已存在同名分类");
                return ajaxResult;
            } catch (Exception e) {
                e.printStackTrace();
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("添加失败");
                return ajaxResult;
            }
    }

    /**
     * 编辑二级收支分类
     */
    @ResponseBody
    @RequestMapping("/editSecondCategory.do")
    public String editSecondCategory(String oldCategoryName, String newCategoryName, String inExStatus){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setInExStatus(inExStatus);
            categorySecond.setSecondCategoryName(oldCategoryName);
            //判断一级分类
            if(oldCategoryName.equals(newCategoryName)){
                //查询当前分类所在的id
                Integer categoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
                categorySecond.setId(categoryId);
                categorySecond.setSecondCategoryName(newCategoryName);
                //执行修改
                categorySecondService.updateByPrimaryKeySelective(categorySecond);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            }else if (!categorySecondService.isExists(newCategoryName, categorySecond)) {
                //查询当前分类所在的id
                Integer categoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
                categorySecond.setId(categoryId);
                categorySecond.setSecondCategoryName(newCategoryName);
                //执行修改
                categorySecondService.updateByPrimaryKeySelective(categorySecond);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "支出或收入分类已存在同名");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "修改失败");
            return jsonObject.toString();
        }
    }

    /**
     * 删除二级收支分类
     */
    @ResponseBody
    @RequestMapping("/deleteSecondCategory.do")
    public String deleteSecondCategory(HttpServletRequest request, String inExStatus){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            String[] categoryFirstList = request.getParameterValues("categoryFirstList[]");
            for (String categoryName : categoryFirstList) {
                //二级分类实体
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setInExStatus(inExStatus);
                categorySecond.setSecondCategoryName(categoryName);
                //查询分类所在 id
                Integer categoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
                //执行删除
                categorySecondService.deleteByPrimaryKey(categoryId);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除成功");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "删除异常");
            return jsonObject.toString();
        }
    }
}
