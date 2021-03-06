package pers.elias.financial_management.mapper;

import org.springframework.stereotype.Repository;
import pers.elias.financial_management.model.CategorySecond;

import java.util.List;

@Repository
public interface CategorySecondMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    int insert(CategorySecond record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    int insertSelective(CategorySecond record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    CategorySecond selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    int updateByPrimaryKeySelective(CategorySecond record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    int updateByPrimaryKey(CategorySecond record);

    /**
     * 查询指定用户账本所有二级分类
     */
    List<String> selectAllByCategorySecond(CategorySecond categorySecond);

    /**
     * 通过一级分类id查询二级分类id
     */
    List<Integer> selectIdByFirstCategoryId(Integer firstCategoryId);

    /**
     * 删除指定用户账本
     */
    int deleteByCategorySecond(CategorySecond categorySecond);

    /**
     *  查询二级分类id
     */
    Integer selectIdByCategorySecond(CategorySecond categorySecond);

    /**
     * 通过用户名删除
     */
    int deleteByUserName(String userName);

    /**
     * 通过账本id删除
     */
    int deleteByAccountBookId(Integer accountBookId);

    /**
     * 通过一级分类id删除
     */
    int deleteByFirstCategoryId(Integer firstCategoryId);

}
