package pers.elias.financial_management.service;

import pers.elias.financial_management.model.AccountUser;

import java.util.Date;

public interface IAccountUserService {
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

    /**
     * 查询二级密码
     */
    String selectSecondPass(String userName);

    /**
     * 判断用户是否设置二级密码
     */
    boolean isSettingSecondPass(String userName);

    /**
     * 判断二级密码是否重复
     */
    boolean checkSecondPass(String userName, String secondPass);

    /**
     * 校验二级密码
     */
    boolean verifySecondPass(String userName, String secondPass);

    /**
     * 注册用户
     */
    AccountUser addAccountUser(AccountUser user);

    /**
     * 判断用户是否存在
     */
    boolean isExists(String userName);

    /**
     * 获取用户从注册日期到现在的天数
     */
    int getUserCreationDays(String userName);

    /**
     * 用户登录
     */
    String login(String userName, String password);
}
