package pers.elias.financial_management.mapper;

import org.springframework.stereotype.Repository;
import pers.elias.financial_management.model.AccountUser;

@Repository
public interface AccountUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    int deleteByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    int insert(AccountUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    int insertSelective(AccountUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    AccountUser selectByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    int updateByPrimaryKeySelective(AccountUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user
     *
     * @mbggenerated Mon May 17 01:28:04 CST 2021
     */
    int updateByPrimaryKey(AccountUser record);
}
