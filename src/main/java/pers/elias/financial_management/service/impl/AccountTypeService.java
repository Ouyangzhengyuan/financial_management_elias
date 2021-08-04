package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountTypeMapper;
import pers.elias.financial_management.model.AccountType;
import pers.elias.financial_management.service.IAccountTypeService;

import java.util.List;

@Service
public class AccountTypeService implements IAccountTypeService {
    @Autowired
    private AccountTypeMapper accountTypeMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
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
    public List<String> selectAccountTypeName(Integer accountBookId) {
        return accountTypeMapper.selectAccountTypeName(accountBookId);
    }

    @Override
    public boolean isExists(AccountType accountType) {
        for(String accountTypeName: accountTypeMapper.selectAccountTypeName(accountType.getAccountBookId())){
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
}
