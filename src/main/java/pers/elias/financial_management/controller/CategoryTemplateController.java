package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.model.CategoryTemplate;
import pers.elias.financial_management.model.template_account_book.Template;
import pers.elias.financial_management.service.impl.*;

import java.util.*;

@Controller
@RequestMapping("/categoryTemplate")
public class CategoryTemplateController {
    @Autowired //异步更新
    private AjaxResult ajaxResult;

    @Autowired //账户全局信息
    private GlobalAccountInfo globalAccountInfo;

    @Autowired //账本服务
    private AccountBookService accountBookService;

    @Autowired //分类模板服务
    private CategoryTemplateService categoryTemplateService;

    @Autowired //金融账户服务
    private AccountTypeService accountTypeService;

    @Autowired //一级分类服务
    private CategoryFirstService categoryFirstService;

    @Autowired // 二级分类服务
    private CategorySecondService categorySecondService;

    @Autowired
    private AccountIndexService accountIndexService;

    /**
     * 查询所有分类模板
     */
    @RequestMapping("/categoryTemplateList")
    @ResponseBody
    public String categoryTemplateList(String accountBookName, String inExStatus) {
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
            //模板实体
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            List<String> categoryTemplateList = categoryTemplateService.selectAllByCategoryTemplate(categoryTemplate);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "操作成功");
            jsonObject.put("data", categoryTemplateList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "模板读取失败");
            return jsonObject.toString();
        }
    }

    /**
     * 查询指定分类模板绑定的数据
     */
    @RequestMapping("/categoryTemplateSingle")
    @ResponseBody
    public String categoryTemplateSingle(String categoryTemplateName, String inExStatus){
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
            //创建分类模板实体
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            categoryTemplate.setTemplateName(categoryTemplateName);
            //执行分类模板查询
            CategoryTemplate cT = categoryTemplateService.selectByCategoryTemplate(categoryTemplate);
            //执行一级分类名称查询
            String firstCategoryName = categoryFirstService.selectByPrimaryKey(cT.getCategoryFirstId()).getFirstCategoryName();
            //执行二级分类名称查询
            String secondCategoryName = categorySecondService.selectByPrimaryKey(cT.getCategorySecondId()).getSecondCategoryName();
            //执行金融账户名称查询
            String accountTypeName = accountTypeService.selectByPrimaryKey(cT.getAccountTypeId()).getAccountTypeName();
            //创建结果对象集
            List<String> dataList = new ArrayList<>();
            dataList.add(firstCategoryName);
            dataList.add(secondCategoryName);
            dataList.add(accountTypeName);
            //响应头
            jsonObject.put("success", true);
            jsonObject.put("msg", "分类模板查询成功");
            jsonObject.put("data", dataList);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "分类模板查询失败");
            return jsonObject.toString();
        }
    }

    /**
     * 添加模板
     */
    @RequestMapping("/addCategoryTemplate.do")
    @ResponseBody
    public AjaxResult addCategoryTemplate(String templateName, String accountTypeName, String firstCategoryName, String secondCategoryName, String inExStatus) {
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询 账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //反向查询 金融账户 id
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //反向查询 一级分类 id
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            categoryFirst.setFirstCategoryName(firstCategoryName);
            Integer categoryFirstId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            //反向查询 二级分类 id
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setInExStatus(inExStatus);
            categorySecond.setSecondCategoryName(secondCategoryName);
            Integer categorySecondId = categorySecondService.selectIdByCategorySecond(categorySecond);
            //创建模板实体对象
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            categoryTemplate.setAccountTypeId(accountTypeId);
            categoryTemplate.setCategoryFirstId(categoryFirstId);
            categoryTemplate.setCategorySecondId(categorySecondId);
            categoryTemplate.setTemplateName(templateName);
            //判断模板是否存在
            if(categoryTemplateService.isExists(categoryTemplate)){
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("该分类模板已存在");
                return ajaxResult;
            }
            //添加模板
            if(categoryTemplateService.insert(categoryTemplate) != 1) {
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("添加失败");
                return ajaxResult;
            }
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("添加成功");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("添加失败");
            return ajaxResult;
        }
    }

    /**
     * 编辑模板
     */
    @ResponseBody
    @RequestMapping("/editCategoryTemplate.do")
    public AjaxResult editCategoryTemplate(String templateName, String accountTypeName, String firstCategoryName, String secondCategoryName, String inExStatus, String oldTemplateName){
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询 账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //查询当前模板 id
            CategoryTemplate oldTemplate = new CategoryTemplate();
            oldTemplate.setUserName(globalAccountInfo.getUserName());
            oldTemplate.setAccountBookId(accountBookId);
            oldTemplate.setInExStatus(inExStatus);
            oldTemplate.setTemplateName(oldTemplateName);
            Integer templateId = categoryTemplateService.selectByCategoryTemplate(oldTemplate).getId();
            //反向查询 金融账户 id
            AccountType accountType = new AccountType();
            accountType.setUserName(globalAccountInfo.getUserName());
            accountType.setAccountBookId(accountBookId);
            accountType.setAccountTypeName(accountTypeName);
            Integer accountTypeId = accountTypeService.selectIdByAccountType(accountType);
            //反向查询 一级分类 id
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            categoryFirst.setFirstCategoryName(firstCategoryName);
            Integer categoryFirstId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            //反向查询 二级分类 id
            CategorySecond categorySecond = new CategorySecond();
            categorySecond.setUserName(globalAccountInfo.getUserName());
            categorySecond.setAccountBookId(accountBookId);
            categorySecond.setInExStatus(inExStatus);
            categorySecond.setSecondCategoryName(secondCategoryName);
            Integer categorySecondId = categorySecondService.selectIdByCategorySecond(categorySecond);
            //创建模板实体对象
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            categoryTemplate.setAccountTypeId(accountTypeId);
            categoryTemplate.setCategoryFirstId(categoryFirstId);
            categoryTemplate.setCategorySecondId(categorySecondId);
            categoryTemplate.setId(templateId);
            categoryTemplate.setTemplateName(templateName);
            //判断模板是否修改
            if(oldTemplateName.equals(templateName)) {
                //执行更新
                categoryTemplateService.updateByPrimaryKeySelective(categoryTemplate);
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("修改成功");
                return ajaxResult;
                //新模板是否存在
            }else if(categoryTemplateService.isExists(categoryTemplate)){
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("该分类模板已存在");
                return ajaxResult;
            }
            //执行更新
            categoryTemplateService.updateByPrimaryKeySelective(categoryTemplate);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("修改成功");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
            return ajaxResult;
        }
    }

    /**
     * 删除模板
     */
    @ResponseBody
    @RequestMapping("/deleteCategoryTemplate.do")
    public String deleteCategoryTemplate(Integer[] idList, String inExStatus){
        JSONObject jsonObject = new JSONObject();
        try{
            for(int id: idList){
                categoryTemplateService.deleteByPrimaryKey(id);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除成功");
            return jsonObject.toString();
        }catch (Exception e){
            jsonObject.put("success", false);
            jsonObject.put("msg", "删除失败");
            return jsonObject.toString();
        }
    }
}
