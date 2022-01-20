package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.bean.GlobalAccountInfoTemp;
import pers.elias.financial_management.mapper.AccountBookMapper;
import pers.elias.financial_management.model.*;
import pers.elias.financial_management.model.template_account_book.Template;
import pers.elias.financial_management.service.IAccountBookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountBookService implements IAccountBookService {
    @Autowired
    private AccountBookMapper accountBookMapper;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private GlobalAccountInfoTemp globalAccountInfoTemp;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountBookHeaderService accountBookHeaderService;

    /**
     * 删除账本以及删除账本关联的所有数据
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(Integer id) {
        //删除该账本下的所有一级分类
        categoryFirstService.deleteByAccountBookId(id);
        //删除该账本下的所有二级分类
        categorySecondService.deleteByAccountBookId(id);
        //删除该账本下的所有分类模板
        categoryTemplateService.deleteByAccountBookId(id);
        //删除该账本下的所有一级金融账户
        accountFinancialService.deleteByAccountBookId(id);
        //删除该账本下的所有二级金融账户
        accountTypeService.deleteByAccountBookId(id);
        //删除该账本下的所有流水记录
        accountCurrentService.deleteByAccountBookId(id);
        //删除该账本下的账本头像
        accountBookHeaderService.deleteByAccountBookId(id);
        //执行删除当前账本
        return accountBookMapper.deleteByPrimaryKey(id);
    }

    /**
     * 账本初始化自动创建关联数据
     */
    @Override
    @Transactional
    public int initAccountBook(AccountBook record, Template template) {
        //添加账本
        int success = accountBookMapper.insert(record);
        //查询新账本的 id
        globalAccountInfoTemp.setUserName(globalAccountInfo.getUserName());
        globalAccountInfoTemp.setAccountBookName(record.getName());
        Integer accountBookId = selectIdByUserNameAndBook(globalAccountInfoTemp);
        //一级金融账户实体
        AccountFinancial accountFinancial = new AccountFinancial();
        accountFinancial.setUserName(globalAccountInfoTemp.getUserName());
        accountFinancial.setAccountBookId(accountBookId);
        //二级金融账户实体
        AccountType accountType = new AccountType();
        accountType.setUserName(globalAccountInfo.getUserName());
        accountType.setAccountBookId(accountBookId);
        //遍历一级金融账户常量
        for (int i = 0; i < template.getAccountFinancialList().length; i++) {
            accountFinancial.setFinancialAccountName(template.getAccountFinancialList()[i]);
            accountFinancialService.insert(accountFinancial);
            //查询新账本的 id
            Integer accountFinancialId = accountFinancialService.selectId(accountFinancial);
            //遍历二级金融账户常量
            for (int n = 0; n < template.getAccountType()[i].length; n++) {
                accountType.setFinancialAccountId(accountFinancialId);
                accountType.setAccountTypeName(template.getAccountType()[i][n]);
                accountTypeService.insert(accountType);
            }
        }
        //一级分类实体
        CategoryFirst categoryFirst = new CategoryFirst();
        categoryFirst.setUserName(globalAccountInfoTemp.getUserName());
        categoryFirst.setAccountBookId(accountBookId);
        categoryFirst.setInExStatus("收");
        //二级分类实体
        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setUserName(globalAccountInfoTemp.getUserName());
        categorySecond.setAccountBookId(accountBookId);
        categorySecond.setInExStatus("收");
        //添加一、二级收入分类
        for (int i = 0; i < template.getFirstIncomeCategory().length; i++) { //遍历一级收入分类
            categoryFirst.setFirstCategoryName(template.getFirstIncomeCategory()[i]);
            categoryFirstService.insert(categoryFirst);//执行添加
            Integer firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            for (int n = 0; n < template.getSecondIncomeCategory()[i].length; n++) {//遍历二级收入分类
                categorySecond.setSecondCategoryName(template.getSecondIncomeCategory()[i][n]);
                categorySecond.setFirstCategoryId(firstCategoryId);
                categorySecondService.insert(categorySecond);
            }
        }
        //修改收支属性
        categoryFirst.setInExStatus("支");
        categorySecond.setInExStatus("支");
        //添加一、二级支出分类
        for (int i = 0; i < template.getFirstExpenseCategory().length; i++) {//遍历一级支出分类
            categoryFirst.setFirstCategoryName(template.getFirstExpenseCategory()[i]);
            categoryFirstService.insert(categoryFirst);//执行添加
            Integer firstCategoryId = categoryFirstService.selectIdByCategoryFirst(categoryFirst);
            for (int n = 0; n < template.getSecondExpenseCategory()[i].length; n++) {//遍历二级支出分类
                categorySecond.setSecondCategoryName(template.getSecondExpenseCategory()[i][n]);
                categorySecond.setFirstCategoryId(firstCategoryId);
                categorySecondService.insert(categorySecond);//执行添加
            }
        }
        return success;
    }

    /**
     * 添加账本后自动为账本添加一级金融账户、二级金融账户、一级收支分类、二级收支分类
     */
    @Override
    @Transactional
    public int insert(AccountBook record, Template template) {
        //添加账本
        int success = accountBookMapper.insert(record);
        //查询新账本的 id
        globalAccountInfoTemp.setUserName(globalAccountInfo.getUserName());
        globalAccountInfoTemp.setAccountBookName(record.getName());
        Integer accountBookId = selectIdByUserNameAndBook(globalAccountInfoTemp);
        //一级金融账户实体
        AccountFinancial accountFinancial = new AccountFinancial();
        accountFinancial.setUserName(globalAccountInfoTemp.getUserName());
        accountFinancial.setAccountBookId(accountBookId);
        //创建一级金融账户
        for (int i = 0; i < template.getAccountFinancialList().length; i++) {
            accountFinancial.setFinancialAccountName(template.getAccountFinancialList()[i]);
            accountFinancialService.insert(accountFinancial);
        }
        return success;
    }

    @Override
    public int insertSelective(AccountBook record) {
        return accountBookMapper.insertSelective(record);
    }

    @Override
    public AccountBook selectByPrimaryKey(Integer id) {
        return accountBookMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountBook record) {
        return accountBookMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountBook record) {
        return accountBookMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByUserNameAndBook(AccountBook accountBook) {
        return accountBookMapper.deleteByUserNameAndBook(accountBook);
    }

    @Override
    public List<String> selectAllByUserName(String userName) {
        return accountBookMapper.selectAllByUserName(userName);
    }

    @Override
    public Integer selectIdByUserNameAndBook(GlobalAccountInfo globalAccountInfo) {
        return accountBookMapper.selectIdByUserNameAndBook(globalAccountInfo);
    }

    @Override
    public boolean isExists(String userName, String accountBookName) {
        List<String> accountBookList = accountBookMapper.selectAllByUserName(userName);
        for (String accountBook : accountBookList) {
            if (accountBookName.equals(accountBook)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 账本管理数据
     */
    @Override
    public Map<String, Object> accountBookManage(String accountBookName) {
        Map<String, Object> accountBookMap = new HashMap<>();
        //查询账本 id
        globalAccountInfo.setAccountBookName(accountBookName);
        Integer accountBookId = accountBookMapper.selectIdByUserNameAndBook(globalAccountInfo);
        //流水实体映射
        Map<String, Object> accountCurrentMap = new HashMap<>();
        accountCurrentMap.put("userName", globalAccountInfo.getUserName());
        accountCurrentMap.put("accountBookId", accountBookId);
        //查询账本流水总数
        Integer count = accountCurrentService.selectCount(accountCurrentMap);
        //查询总支出
        accountCurrentMap.put("inExStatus", "支");
        return null;
    }

    @Override
    public int deleteByUserName(String userName) {
        return accountBookMapper.deleteByUserName(userName);
    }
}
