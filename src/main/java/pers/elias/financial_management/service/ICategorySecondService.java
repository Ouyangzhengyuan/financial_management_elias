package pers.elias.financial_management.service;

import pers.elias.financial_management.model.CategorySecond;

import java.util.List;

public interface ICategorySecondService {
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
     * 删除指定用户账本
     */
    int deleteByCategorySecond(CategorySecond categorySecond);

    /**
     * 是否存在二级分类
     */
    boolean isExists(CategorySecond categorySecond);

    /**
     *  查询二级分类id
     */
    Integer selectIdByCategorySecond(CategorySecond categorySecond);
}