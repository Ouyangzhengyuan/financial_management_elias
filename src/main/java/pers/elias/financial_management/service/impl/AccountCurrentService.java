package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountCurrentMapper;
import pers.elias.financial_management.model.AccountCurrent;
import pers.elias.financial_management.model.AccountCurrentResult;
import pers.elias.financial_management.service.IAccountCurrentService;
import pers.elias.financial_management.utils.PageBean;

import java.util.List;
import java.util.Map;

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
    public PageBean<AccountCurrentResult>selectAllByAccountCurrent(Map<String, Object> paramMap) {
        PageBean<AccountCurrentResult> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<AccountCurrentResult> datas = accountCurrentMapper.selectAllByAccountCurrent(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = accountCurrentMapper.selectCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }


    @Override
    public List<AccountCurrentResult> selectByConditions(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectByConditions(accountCurrent);
    }

    @Override
    public List<Double> selectDailyExpense(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectDailyExpense(accountCurrent);
    }

    @Override
    public List<Double> selectMonthlyExpense(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectMonthlyExpense(accountCurrent);
    }

    @Override
    public List<Double> selectYearlyExpense(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectYearlyExpense(accountCurrent);
    }

    @Override
    public List<Double> selectDailyIncome(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectDailyIncome(accountCurrent);
    }

    @Override
    public List<Double> selectMonthlyIncome(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectMonthlyIncome(accountCurrent);
    }

    @Override
    public List<Double> selectYearlyIncome(AccountCurrent accountCurrent) {
        return accountCurrentMapper.selectYearlyIncome(accountCurrent);
    }

    @Override
    public AccountCurrentResult selectById(Integer id) {
        return accountCurrentMapper.selectById(id);
    }
}
