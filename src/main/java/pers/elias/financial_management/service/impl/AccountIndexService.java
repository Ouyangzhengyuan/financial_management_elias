package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountIndexMapper;
import pers.elias.financial_management.model.AccountIndex;
import pers.elias.financial_management.service.IAccountIndexService;

@Service
public class AccountIndexService implements IAccountIndexService {
    @Autowired
    private AccountIndexMapper accountIndexMapper;

    @Override
    public int deleteByPrimaryKey(String userName) {
        return accountIndexMapper.deleteByPrimaryKey(userName);
    }

    @Override
    public int insert(AccountIndex record) {
        return accountIndexMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountIndex record) {
        return accountIndexMapper.insertSelective(record);
    }

    @Override
    public AccountIndex selectByPrimaryKey(String userName) {
        return accountIndexMapper.selectByPrimaryKey(userName);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountIndex record) {
        return accountIndexMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountIndex record) {
        return accountIndexMapper.updateByPrimaryKey(record);
    }
}
