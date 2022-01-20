package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.service.impl.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 查询一级分类（支出 or 收入）
     */
    @RequestMapping("/firstCategoryList")
    @ResponseBody
    public Object firstCategoryList(String accountBookName, String inExStatus) {
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
    public Object firstCategoryAllList(String accountBookName) {
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
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询当前账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName()); //绑定当前用户
            categoryFirst.setAccountBookId(accountBookId);//绑定当前账本id
            //判断一级分类是否存在
            if (!categoryFirstService.firstCategoryExists(firstCategoryName, categoryFirst)) {
                categoryFirst.setInExStatus(inExStatus); //绑定支出属性
                categoryFirst.setFirstCategoryName(firstCategoryName); //绑定一级分类
                categoryFirstService.insert(categoryFirst);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("添加成功");
            } else {
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("支出或收入已存在同名分类");
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
     * 编辑一级分类
     */
    @ResponseBody
    @RequestMapping("/editFirstCategory.do")
    public String editFirstCategory(String oldCategoryName, String newCategoryName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //一级分类实体
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            categoryFirst.setFirstCategoryName(oldCategoryName);
            //判断一级分类
            if (oldCategoryName.equals(newCategoryName)) {
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            } else if (!categoryFirstService.firstCategoryExists(newCategoryName, categoryFirst)) {
                //查询当前分类所在的id
                Integer categoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
                categoryFirst.setId(categoryId);
                categoryFirst.setFirstCategoryName(newCategoryName);
                //执行修改
                categoryFirstService.updateByPrimaryKeySelective(categoryFirst);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "支出或收入分类已存在同名");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "修改失败");
            return jsonObject.toString();
        }
    }

    /**
     * 删除一级分类
     */
    @ResponseBody
    @RequestMapping("/deleteFirstCategory.do")
    public String deleteFirstCategory(HttpServletRequest request, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //获取前端账本
            String[] categoryFirstList = request.getParameterValues("categoryFirstList[]");
            //循环删除
            for (String categoryName : categoryFirstList) {
                CategoryFirst categoryFirst = new CategoryFirst();
                categoryFirst.setUserName(globalAccountInfo.getUserName());
                categoryFirst.setAccountBookId(accountBookId);
                categoryFirst.setInExStatus(inExStatus);
                categoryFirst.setFirstCategoryName(categoryName);
                //查询一级分类 id
                Integer firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
                //执行删除
                categoryFirstService.deleteByPrimaryKey(firstCategoryId);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除成功");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "删除失败");
            return jsonObject.toString();
        }
    }
}
