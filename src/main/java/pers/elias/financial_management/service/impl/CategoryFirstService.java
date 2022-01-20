package pers.elias.financial_management.service.impl;

import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.mapper.CategoryFirstMapper;
import pers.elias.financial_management.model.CategoryFirst;
import pers.elias.financial_management.model.CategorySecond;
import pers.elias.financial_management.service.ICategoryFirstService;

import java.util.List;

@Service
public class CategoryFirstService implements ICategoryFirstService {
    @Autowired
    private CategoryFirstMapper categoryFirstMapper;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountBookService accountBookService;

    /**
     * 删除一级分类同时删除有关联的数据
     */
    @Override
    public int deleteByPrimaryKey(Integer id) {
        //删除流水记录
        globalAccountInfo.setAccountBookName(globalAccountInfo.getAccountBookIndex());
        Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
        List<Integer> categorySecondIdList = categorySecondService.selectIdByFirstCategoryId(id);
        for(Integer categorySecondId: categorySecondIdList){
            accountCurrentService.deleteByCategorySecondId(categorySecondId);
        }
        //删除二级分类
        categorySecondService.deleteByFirstCategoryId(id);
        //删除模板分类
        categoryTemplateService.deleteByFirstCategoryId(id);
        //删除一级分类
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
    public boolean firstCategoryExists(String categoryFirstName ,CategoryFirst categoryFirst) {
        List<String> categoryFirstList = categoryFirstMapper.selectAllByCategoryFirst(categoryFirst);
        for(String cF: categoryFirstList){
            if(categoryFirstName.equals(cF)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer selectIdByCategoryFirst(CategoryFirst categoryFirst) {
        return categoryFirstMapper.selectIdByCategoryFirst(categoryFirst);
    }

    @Override
    public int deleteByUserName(String userName) {
        return categoryFirstMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        return categoryFirstMapper.deleteByAccountBookId(accountBookId);
    }
}
