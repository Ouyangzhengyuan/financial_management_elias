package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.CategorySecondMapper;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.ICategorySecondService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class CategorySecondService implements ICategorySecondService {
    @Autowired
    private CategorySecondMapper categorySecondMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
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
    public int deleteByCategorySecond(CategorySecond categorySecond) {
        return categorySecondMapper.deleteByCategorySecond(categorySecond);
    }

    @Override
    public boolean isExists(CategorySecond categorySecond) {
        for (String cSN : categorySecondMapper.selectAllByCategorySecond(categorySecond)) {
            if (cSN.equals(categorySecond.getSecondCategoryName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer selectIdByCategorySecond(CategorySecond categorySecond) {
        return categorySecondMapper.selectIdByCategorySecond(categorySecond);
    }
}
