package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.elias.financial_management.mapper.AccountTypeMapper;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.IAccountTypeService;

import java.util.List;

@Service
public class AccountTypeService implements IAccountTypeService {
    @Autowired
    private AccountTypeMapper accountTypeMapper;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Override
    @Transactional
    public int deleteByPrimaryKey(Integer id) {
        //删除子账户下的所有分类模板
        categoryTemplateService.deleteByAccountTypeId(id);
        //删除子账户下的所有流水
        accountCurrentService.deleteByAccountTypeId(id);
        //删除子账户
        return accountTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountType record) {
        return accountTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountType record) {
        return accountTypeMapper.insertSelective(record);
    }

    @Override
    public AccountType selectByPrimaryKey(Integer id) {
        return accountTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountType record) {
        return accountTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountType record) {
        return accountTypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectAccountTypeName(AccountType accountType) {
        return accountTypeMapper.selectAccountTypeName(accountType);
    }

    @Override
    public boolean isExists(AccountType accountType) {
        for(String accountTypeName: accountTypeMapper.selectAccountTypeName(accountType)){
            if(accountType.getAccountTypeName().equals(accountTypeName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer selectIdByAccountType(AccountType accountType) {
        return accountTypeMapper.selectIdByAccountType(accountType);
    }

    @Override
    public int deleteByUserName(String userName) {
        return accountTypeMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        return accountTypeMapper.deleteByAccountBookId(accountBookId);
    }
}
