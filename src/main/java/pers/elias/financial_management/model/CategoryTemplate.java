package pers.elias.financial_management.model;

import java.io.Serializable;

public class CategoryTemplate implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.user_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.account_book_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private Integer accountBookId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.category_first_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private Integer categoryFirstId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.category_second_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private Integer categorySecondId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.account_type_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private Integer accountTypeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.in_ex_status
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private String inExStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column category_template.template_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private String templateName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table category_template
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.id
     *
     * @return the value of category_template.id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.id
     *
     * @param id the value for category_template.id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.user_name
     *
     * @return the value of category_template.user_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.user_name
     *
     * @param userName the value for category_template.user_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.account_book_id
     *
     * @return the value of category_template.account_book_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public Integer getAccountBookId() {
        return accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.account_book_id
     *
     * @param accountBookId the value for category_template.account_book_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.category_first_id
     *
     * @return the value of category_template.category_first_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public Integer getCategoryFirstId() {
        return categoryFirstId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.category_first_id
     *
     * @param categoryFirstId the value for category_template.category_first_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setCategoryFirstId(Integer categoryFirstId) {
        this.categoryFirstId = categoryFirstId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.category_second_id
     *
     * @return the value of category_template.category_second_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public Integer getCategorySecondId() {
        return categorySecondId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.category_second_id
     *
     * @param categorySecondId the value for category_template.category_second_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setCategorySecondId(Integer categorySecondId) {
        this.categorySecondId = categorySecondId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.account_type_id
     *
     * @return the value of category_template.account_type_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.account_type_id
     *
     * @param accountTypeId the value for category_template.account_type_id
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.in_ex_status
     *
     * @return the value of category_template.in_ex_status
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public String getInExStatus() {
        return inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.in_ex_status
     *
     * @param inExStatus the value for category_template.in_ex_status
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setInExStatus(String inExStatus) {
        this.inExStatus = inExStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column category_template.template_name
     *
     * @return the value of category_template.template_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column category_template.template_name
     *
     * @param templateName the value for category_template.template_name
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category_template
     *
     * @mbggenerated Tue Jul 13 22:25:28 CST 2021
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
        sb.append(", categoryFirstId=").append(categoryFirstId);
        sb.append(", categorySecondId=").append(categorySecondId);
        sb.append(", accountTypeId=").append(accountTypeId);
        sb.append(", inExStatus=").append(inExStatus);
        sb.append(", templateName=").append(templateName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}