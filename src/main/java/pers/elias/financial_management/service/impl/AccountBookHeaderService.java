package pers.elias.financial_management.service.impl;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.component.GlobalAccountInfo;
import pers.elias.financial_management.mapper.AccountBookHeaderMapper;
import pers.elias.financial_management.model.AccountBookHeader;
import pers.elias.financial_management.service.IAccountBookHeaderService;

import javax.xml.bind.annotation.XmlElementDecl;

@Service
public class AccountBookHeaderService implements IAccountBookHeaderService {
    @Autowired
    private AccountBookHeaderMapper accountBookHeaderMapper;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountBookHeaderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountBookHeader record) {
        return accountBookHeaderMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountBookHeader record) {
        return accountBookHeaderMapper.insertSelective(record);
    }

    @Override
    public AccountBookHeader selectByPrimaryKey(Integer id) {
        return accountBookHeaderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountBookHeader record) {
        return accountBookHeaderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountBookHeader record) {
        return accountBookHeaderMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountBookHeader selectByAccountBookHeader(AccountBookHeader accountBookHeader) {
        return accountBookHeaderMapper.selectByAccountBookHeader(accountBookHeader);
    }

    @Override
    public boolean isExists(AccountBookHeader accountBookHeader) {
        AccountBookHeader aBH = accountBookHeaderMapper.selectByAccountBookHeader(accountBookHeader);
        if(aBH != null){
            if(aBH.getUserName() != null && aBH.getAccountBookId() != null){
                return aBH.getUserName().equals(globalAccountInfo.getUserName()) && aBH.getAccountBookId().equals(globalAccountInfo.getAccountBookId());
            }
        }
        return false;
    }

    @Override
    public int updateByAccountBookHeader(AccountBookHeader accountBookHeader) {
        return accountBookHeaderMapper.updateByAccountBookHeader(accountBookHeader);
    }
}
