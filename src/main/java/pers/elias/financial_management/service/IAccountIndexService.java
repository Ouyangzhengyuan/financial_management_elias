package pers.elias.financial_management.service;

import pers.elias.financial_management.model.AccountIndex;

public interface IAccountIndexService {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    int deleteByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    int insert(AccountIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    int insertSelective(AccountIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    AccountIndex selectByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    int updateByPrimaryKeySelective(AccountIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_index
     *
     * @mbggenerated Tue Sep 14 06:01:23 CST 2021
     */
    int updateByPrimaryKey(AccountIndex record);
}
