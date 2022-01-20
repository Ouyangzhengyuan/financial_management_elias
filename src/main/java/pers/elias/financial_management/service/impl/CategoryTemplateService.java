package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.CategoryTemplateMapper;
import pers.elias.financial_management.model.CategoryTemplate;
import pers.elias.financial_management.service.ICategoryTemplateService;

import java.util.List;

@Service
public class CategoryTemplateService implements ICategoryTemplateService {
    @Autowired
    private CategoryTemplateMapper categoryTemplateMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return categoryTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CategoryTemplate record) {
        return categoryTemplateMapper.insert(record);
    }

    @Override
    public int insertSelective(CategoryTemplate record) {
        return categoryTemplateMapper.insertSelective(record);
    }

    @Override
    public CategoryTemplate selectByPrimaryKey(Integer id) {
        return categoryTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CategoryTemplate record) {
        return categoryTemplateMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CategoryTemplate record) {
        return categoryTemplateMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> selectAllByCategoryTemplate(CategoryTemplate categoryTemplate) {
        return categoryTemplateMapper.selectAllByCategoryTemplate(categoryTemplate);
    }

    @Override
    public List<CategoryTemplate> selectAllTemplate(CategoryTemplate categoryTemplate) {
        return categoryTemplateMapper.selectAllTemplate(categoryTemplate);
    }

    @Override
    public int deleteByCategoryTemplate(CategoryTemplate categoryTemplate) {
        return categoryTemplateMapper.deleteByCategoryTemplate(categoryTemplate);
    }

    @Override
    public boolean isExists(CategoryTemplate categoryTemplate) {
        for(String categoryTemplateName: categoryTemplateMapper.selectAllByCategoryTemplate(categoryTemplate)){
            if(categoryTemplate.getTemplateName().trim().equals(categoryTemplateName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public CategoryTemplate selectByCategoryTemplate(CategoryTemplate categoryTemplate) {
        return categoryTemplateMapper.selectByCategoryTemplate(categoryTemplate);
    }

    @Override
    public int deleteByUserName(String userName) {
        return categoryTemplateMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        return categoryTemplateMapper.deleteByAccountBookId(accountBookId);
    }

    @Override
    public int deleteByCategorySecondId(Integer categorySecondId) {
        return categoryTemplateMapper.deleteByCategorySecondId(categorySecondId);
    }

    @Override
    public int deleteByFirstCategoryId(Integer firstCategoryId) {
        return categoryTemplateMapper.deleteByFirstCategoryId(firstCategoryId);
    }

    @Override
    public int deleteByAccountTypeId(Integer accountTypeId) {
        return categoryTemplateMapper.deleteByAccountTypeId(accountTypeId);
    }
}
