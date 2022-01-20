package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountFinancialMapper;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.model.AccountFinancial;
import pers.elias.financial_management.service.IAccountFinancialService;
import pers.elias.financial_management.utils.PageBean;

import java.util.List;
import java.util.Map;

@Service
public class AccountFinancialService implements IAccountFinancialService {
    @Autowired
    private AccountFinancialMapper accountFinancialMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountFinancialMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountFinancial record) {
        return accountFinancialMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountFinancial record) {
        return accountFinancialMapper.insertSelective(record);
    }

    @Override
    public AccountFinancial selectByPrimaryKey(Integer id) {
        return accountFinancialMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountFinancial record) {
        return accountFinancialMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountFinancial record) {
        return accountFinancialMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectAll(AccountFinancial accountFinancial) {
        return accountFinancialMapper.selectAll(accountFinancial);
    }

    @Override
    public Integer selectId(AccountFinancial accountFinancial) {
        return accountFinancialMapper.selectId(accountFinancial);
    }

    @Override
    public List<String> selectSubAccount(Map<String, Object> paramMap) {
        return accountFinancialMapper.selectSubAccount(paramMap);
    }


    @Override
    public PageBean<AccountCurrentResult> selectSubAccountCurrent(Map<String, Object> paramMap) {
        PageBean<AccountCurrentResult> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));
        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<AccountCurrentResult> datas = accountFinancialMapper.selectSubAccountCurrent(paramMap);
        pageBean.setDatas(datas);
        Integer totalsize = accountFinancialMapper.selectCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    public int deleteByUserName(String userName) {
        return accountFinancialMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        return accountFinancialMapper.deleteByAccountBookId(accountBookId);
    }
}
