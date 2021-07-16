package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.mapper.AccountBookMapper;
import pers.elias.financial_management.model.AccountBook;
import pers.elias.financial_management.service.IAccountBookService;

import java.util.List;

@Service
public class AccountBookService implements IAccountBookService {
    @Autowired
    private AccountBookMapper accountBookMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountBookMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountBook record) {
        return accountBookMapper.insert(record);
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
    public boolean hasAccountBook(String userName, String accountBookName) {
        List<String> accountBookList = accountBookMapper.selectAllByUserName(userName);
        for(String accountBook: accountBookList){
            if(accountBookName.equals(accountBook)){
                return false;
            }
        }
        return true;
    }
}
