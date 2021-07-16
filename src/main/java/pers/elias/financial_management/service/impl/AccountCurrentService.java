package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountCurrentMapper;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.service.IAccountCurrentService;

import java.util.List;

@Service
public class AccountCurrentService implements IAccountCurrentService {

    @Autowired
    private AccountCurrentMapper accountCurrentMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountCurrentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountCurrent record) {
        return accountCurrentMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountCurrent record) {
        return accountCurrentMapper.insertSelective(record);
    }

    @Override
    public AccountCurrent selectByPrimaryKey(Integer id) {
        return accountCurrentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountCurrent record) {
        return accountCurrentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountCurrent record) {
        return accountCurrentMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<AccountCurrentResult> selectAllByAccountCurrent(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectAllByAccountCurrent(accountCurrent);
    }
}
