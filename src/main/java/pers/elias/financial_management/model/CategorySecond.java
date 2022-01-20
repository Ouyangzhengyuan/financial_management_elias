package pers.elias.financial_management.model;

import java.io.Serializable;

public class CategorySecond implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.user_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.account_book_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private Integer accountBookId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.first_category_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private Integer firstCategoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.second_category_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private String secondCategoryName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_second.in_ex_status
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private String inExStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.id
     *
     * @return the value of category_second.id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.id
     *
     * @param id the value for category_second.id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.user_name
     *
     * @return the value of category_second.user_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.user_name
     *
     * @param userName the value for category_second.user_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.account_book_id
     *
     * @return the value of category_second.account_book_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public Integer getAccountBookId() {
        return accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.account_book_id
     *
     * @param accountBookId the value for category_second.account_book_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.first_category_id
     *
     * @return the value of category_second.first_category_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.first_category_id
     *
     * @param firstCategoryId the value for category_second.first_category_id
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.second_category_name
     *
     * @return the value of category_second.second_category_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.second_category_name
     *
     * @param secondCategoryName the value for category_second.second_category_name
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_second.in_ex_status
     *
     * @return the value of category_second.in_ex_status
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public String getInExStatus() {
        return inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_second.in_ex_status
     *
     * @param inExStatus the value for category_second.in_ex_status
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    public void setInExStatus(String inExStatus) {
        this.inExStatus = inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_second
     *
     * @mbggenerated Mon May 17 12:50:42 CST 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userName=").append(userName);
        sb.append(", accountBookId=").append(accountBookId);
        sb.append(", firstCategoryId=").append(firstCategoryId);
        sb.append(", secondCategoryName=").append(secondCategoryName);
        sb.append(", inExStatus=").append(inExStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
