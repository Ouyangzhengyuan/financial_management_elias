package pers.elias.financial_management.service.impl;

import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.mapper.CategorySecondMapper;
import pers.elias.financial_management.model.AccountFinancial;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.ICategorySecondService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class CategorySecondService implements ICategorySecondService {
    @Autowired
    private CategorySecondMapper categorySecondMapper;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    /**
     * 删除二级分类、二级分类关联的模板分类、流水记录
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(Integer id) {
        //删除二级分类下的所有模板分类
        categoryTemplateService.deleteByCategorySecondId(id);
        //删除二级分类下的所有流水记录
        accountCurrentService.deleteByCategorySecondId(id);
        //执行删除二级分类
        return categorySecondMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CategorySecond record) {
        return categorySecondMapper.insert(record);
    }

    @Override
    public int insertSelective(CategorySecond record) {
        return categorySecondMapper.insertSelective(record);
    }

    @Override
    public CategorySecond selectByPrimaryKey(Integer id) {
        return categorySecondMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CategorySecond record) {
        return categorySecondMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CategorySecond record) {
        return categorySecondMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectAllByCategorySecond(CategorySecond categorySecond) {
        return categorySecondMapper.selectAllByCategorySecond(categorySecond);
    }

    @Override
    public List<Integer> selectIdByFirstCategoryId(Integer firstCategoryId) {
        return categorySecondMapper.selectIdByFirstCategoryId(firstCategoryId);
    }

    @Override
    public int deleteByCategorySecond(CategorySecond categorySecond) {
        return categorySecondMapper.deleteByCategorySecond(categorySecond);
    }

    @Override
    public boolean isExists(String categorySecondName, CategorySecond categorySecond) {
        List<String> categorySecondList = categorySecondMapper.selectAllByCategorySecond(categorySecond);
        for (String cSN : categorySecondList) {
            if (categorySecondName.equals(cSN)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer selectIdByCategorySecond(CategorySecond categorySecond) {
        return categorySecondMapper.selectIdByCategorySecond(categorySecond);
    }

    @Override
    public int deleteByUserName(String userName) {
        return categorySecondMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        return categorySecondMapper.deleteByAccountBookId(accountBookId);
    }

    @Override
    public int deleteByFirstCategoryId(Integer firstCategoryId) {
        return categorySecondMapper.deleteByFirstCategoryId(firstCategoryId);
    }
}
