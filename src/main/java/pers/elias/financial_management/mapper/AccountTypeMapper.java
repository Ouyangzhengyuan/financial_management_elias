package pers.elias.financial_management.mapper;

import org.springframework.stereotype.Repository;
import pers.elias.financial_management.model.AccountType;

import java.util.List;

@Repository
public interface AccountTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    int insert(AccountType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    int insertSelective(AccountType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    AccountType selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    int updateByPrimaryKeySelective(AccountType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_type
     *
     * @mbggenerated Fri Aug 13 16:39:34 CST 2021
     */
    int updateByPrimaryKey(AccountType record);

    /**
     * 查询所有金融账户
     */
    List<String> selectAccountTypeName(AccountType accountType);

    /**
     * 查询指定金融账户
     */
    Integer selectIdByAccountType(AccountType accountType);

    /**
     * 通过用户名删除
     */
    int deleteByUserName(String userName);

    /**
     * 通过账本id删除
     */
    int deleteByAccountBookId(Integer accountBookId);
}
