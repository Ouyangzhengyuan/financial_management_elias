package pers.elias.financial_management.service;

import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.model.AccountUserHeader;

public interface IAccountUserHeaderService {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    int deleteByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    int insert(AccountUserHeader record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    int insertSelective(AccountUserHeader record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    AccountUserHeader selectByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    int updateByPrimaryKeySelective(AccountUserHeader record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_user_header
     *
     * @mbggenerated Thu Aug 26 00:12:55 CST 2021
     */
    int updateByPrimaryKey(AccountUserHeader record);

    /**
     * 通过用户名删除
     */
    int deleteByUserName(String userName);
}
