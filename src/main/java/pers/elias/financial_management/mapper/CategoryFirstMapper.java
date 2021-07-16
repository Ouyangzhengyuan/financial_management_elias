package pers.elias.financial_management.mapper;

import org.springframework.stereotype.Repository;
import pers.elias.financial_management.model.CategoryFirst;

import java.util.List;

@Repository
public interface CategoryFirstMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    int insert(CategoryFirst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    int insertSelective(CategoryFirst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    CategoryFirst selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    int updateByPrimaryKeySelective(CategoryFirst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_first
     *
     * @mbggenerated Mon May 17 10:32:18 CST 2021
     */
    int updateByPrimaryKey(CategoryFirst record);

    /**
     * 查询指定用户账本所有一级分类
     * 根据字段 in_ex_status 来查询是支出分类还是收入分类
     */
    List<String> selectAllByCategoryFirst(CategoryFirst categoryFirst);

    /**
     * 删除指定用户账本的一级分类
     */
    int deleteByCategoryFirst(CategoryFirst categoryFirst);

    /**
     * 查询指定一级分类
     */
    CategoryFirst selectByCategoryFirst(CategoryFirst categoryFirst);

    /**
     * 查询指定一级分类id
     */
    Integer selectIdByCategoryFirst(CategoryFirst categoryFirst);
}