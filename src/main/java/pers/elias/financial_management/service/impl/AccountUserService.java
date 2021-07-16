package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountUserMapper;
import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.service.IAccountUserService;

@Service
public class AccountUserService implements IAccountUserService, UserDetailsService {
    @Autowired
    private AccountUserMapper accountUserMapper;

    @Override
    public int deleteByPrimaryKey(String userName) {
        return accountUserMapper.deleteByPrimaryKey(userName);
    }

    @Override
    public int insert(AccountUser record) {
        return accountUserMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountUser record) {
        return accountUserMapper.insertSelective(record);
    }

    @Override
    public AccountUser selectByPrimaryKey(String userName) {
        return accountUserMapper.selectByPrimaryKey(userName);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountUser record) {
        return accountUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountUser record) {
        return accountUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountUser user = accountUserMapper.selectByPrimaryKey(username);
        if(user == null){
            throw new UsernameNotFoundException("该用户不存在！");
        }
        return user;
    }
}
