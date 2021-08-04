package pers.elias.financial_management.model;

import pers.elias.financial_management.utils.DateTimeUtil;
import pers.elias.financial_management.utils.KeepTwoDecimals;

import java.util.Date;

public class AccountCurrentResult {
    private Integer id;
    private String user_name;
    private Integer account_book_id;
    private Date date;
    private String dateConverted;
    private String first_category_name;
    private String second_category_name;
    private String in_ex_status;
    private Double amount;
    private String amountString;
    private String account_type_name;
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccount_book_id() {
        return account_book_id;
    }

    public void setAccount_book_id(Integer account_book_id) {
        this.account_book_id = account_book_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.dateConverted = DateTimeUtil.dateSimpleToString(date);
        this.date = date;
    }

    public String getSecond_category_name() {
        return second_category_name;
    }

    public void setSecond_category_name(String second_category_name) {
        this.second_category_name = second_category_name;
    }

    public String getIn_ex_status() {
        return in_ex_status;
    }

    public void setIn_ex_status(String in_ex_status) {
        this.in_ex_status = in_ex_status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amountString = KeepTwoDecimals.keepTwoDeci(amount);
        this.amount = amount;
    }

    public String getAccount_type_name() {
        return account_type_name;
    }

    public void setAccount_type_name(String account_type_name) {
        this.account_type_name = account_type_name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDateConverted() {
        return dateConverted;
    }

    public void setDateConverted(String dateConverted) {
        this.dateConverted = dateConverted;
    }

    public String getFirst_category_name() {
        return first_category_name;
    }

    public void setFirst_category_name(String first_category_name) {
        this.first_category_name = first_category_name;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    @Override
    public String toString() {
        return "AccountCurrentResult{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", account_book_id=" + account_book_id +
                ", date=" + date +
                ", dateConverted='" + dateConverted + '\'' +
                ", first_category_name='" + first_category_name + '\'' +
                ", second_category_name='" + second_category_name + '\'' +
                ", in_ex_status=" + in_ex_status +
                ", amount=" + amount +
                ", amountString='" + amountString + '\'' +
                ", account_type_name='" + account_type_name + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
