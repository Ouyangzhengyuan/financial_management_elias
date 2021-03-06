package pers.elias.financial_management.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountCurrent implements Serializable {
    private String dateConverted;

    private Integer accountFinancialId;

    private List<Integer> accountFinancialIdList;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.id
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.user_name
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.account_book_id
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Integer accountBookId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.date
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Date date;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.first_category_id
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Integer firstCategoryId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.second_category_id
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Integer secondCategoryId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.in_ex_status
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private String inExStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.amount
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Double amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.account_type_id
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private Integer accountTypeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_current.remarks
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private String remarks;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table account_current
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.id
     *
     * @return the value of account_current.id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.id
     *
     * @param id the value for account_current.id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.user_name
     *
     * @return the value of account_current.user_name
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.user_name
     *
     * @param userName the value for account_current.user_name
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.account_book_id
     *
     * @return the value of account_current.account_book_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Integer getAccountBookId() {
        return accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.account_book_id
     *
     * @param accountBookId the value for account_current.account_book_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.date
     *
     * @return the value of account_current.date
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.date
     *
     * @param date the value for account_current.date
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.first_category_id
     *
     * @return the value of account_current.first_category_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.first_category_id
     *
     * @param firstCategoryId the value for account_current.first_category_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.second_category_id
     *
     * @return the value of account_current.second_category_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Integer getSecondCategoryId() {
        return secondCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.second_category_id
     *
     * @param secondCategoryId the value for account_current.second_category_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setSecondCategoryId(Integer secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.in_ex_status
     *
     * @return the value of account_current.in_ex_status
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public String getInExStatus() {
        return inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.in_ex_status
     *
     * @param inExStatus the value for account_current.in_ex_status
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setInExStatus(String inExStatus) {
        this.inExStatus = inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.amount
     *
     * @return the value of account_current.amount
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.amount
     *
     * @param amount the value for account_current.amount
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.account_type_id
     *
     * @return the value of account_current.account_type_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.account_type_id
     *
     * @param accountTypeId the value for account_current.account_type_id
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_current.remarks
     *
     * @return the value of account_current.remarks
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_current.remarks
     *
     * @param remarks the value for account_current.remarks
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDateConverted() {
        return dateConverted;
    }

    public void setDateConverted(String dateConverted) {
        this.dateConverted = dateConverted;
    }

    public Integer getAccountFinancialId() {
        return accountFinancialId;
    }

    public void setAccountFinancialId(Integer accountFinancialId) {
        this.accountFinancialId = accountFinancialId;
    }

    public List<Integer> getAccountFinancialIdList() {
        return accountFinancialIdList;
    }

    public void setAccountFinancialIdList(List<Integer> accountFinancialIdList) {
        this.accountFinancialIdList = accountFinancialIdList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_current
     *
     * @mbggenerated Fri Sep 24 18:49:05 CST 2021
     */
    @Override
    public String toString() {
        return "AccountCurrent{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", accountBookId=" + accountBookId +
                ", date=" + date +
                ", firstCategoryId=" + firstCategoryId +
                ", secondCategoryId=" + secondCategoryId +
                ", inExStatus='" + inExStatus + '\'' +
                ", amount=" + amount +
                ", accountTypeId=" + accountTypeId +
                ", remarks='" + remarks + '\'' +
                ", dateConverted='" + dateConverted + '\'' +
                ", accountFinancialId=" + accountFinancialId +
                ", accountFinancialIdList=" + accountFinancialIdList +
                '}';
    }
}
