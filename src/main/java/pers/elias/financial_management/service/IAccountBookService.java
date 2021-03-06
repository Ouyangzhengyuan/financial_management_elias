package pers.elias.financial_management.service;

import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountBook;
import pers.elias.financial_management.model.template_account_book.Template;

import java.util.List;
import java.util.Map;

public interface IAccountBookService {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
     int insert(AccountBook record, Template template);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
    int insertSelective(AccountBook record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
    AccountBook selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
    int updateByPrimaryKeySelective(AccountBook record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_book
     *
     * @mbggenerated Mon May 17 01:32:15 CST 2021
     */
    int updateByPrimaryKey(AccountBook record);

    /**
     * 删除指定用户账本
     */
    int deleteByUserNameAndBook(AccountBook accountBook);

    /**
     * 查询指定用户账本集
     */
    List<String> selectAllByUserName(String userName);

    /**
     * 反向查询id
     */
    Integer selectIdByUserNameAndBook(GlobalAccountInfo globalAccountInfo);

    /**
     * 判断账本是否重名
     */
    boolean isExists(String userName, String accountBookName);

    /**
     * 获取账本管理数据
     */
    Map<String, Object> accountBookManage(String accountBookName);

    /**
     * 通过用户名删除
     */
    int deleteByUserName(String userName);

    /**
     * 账本初始化
     */
    int initAccountBook(AccountBook accountBook, Template template);
}
