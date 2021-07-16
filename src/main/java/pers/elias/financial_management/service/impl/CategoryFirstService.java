package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.CategoryFirstMapper;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.service.ICategoryFirstService;

import java.util.List;

@Service
public class CategoryFirstService implements ICategoryFirstService {
    @Autowired
    private CategoryFirstMapper categoryFirstMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return categoryFirstMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CategoryFirst record) {
        return categoryFirstMapper.insert(record);
    }

    @Override
    public int insertSelective(CategoryFirst record) {
        return categoryFirstMapper.insertSelective(record);
    }

    @Override
    public CategoryFirst selectByPrimaryKey(Integer id) {
        return categoryFirstMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CategoryFirst record) {
        return categoryFirstMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CategoryFirst record) {
        return categoryFirstMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectAllByCategoryFirst(CategoryFirst categoryFirst) {
        return categoryFirstMapper.selectAllByCategoryFirst(categoryFirst);
    }

    @Override
    public int deleteByCategoryFirst(CategoryFirst categoryFirst) {
        return categoryFirstMapper.deleteByCategoryFirst(categoryFirst);
    }

    @Override
    public CategoryFirst selectByCategoryFirst(CategoryFirst categoryFirst) {
        return categoryFirstMapper.selectByCategoryFirst(categoryFirst);
    }

    @Override
    public boolean firstCategoryExists(CategoryFirst categoryFirst) {
        CategoryFirst categoryFirst1 = categoryFirstMapper.selectByCategoryFirst(categoryFirst);
        return categoryFirst1 == null;
    }

    @Override
    public Integer selectIdByCategoryFirst(CategoryFirst categoryFirst) {
        return categoryFirstMapper.selectIdByCategoryFirst(categoryFirst);
    }
}
