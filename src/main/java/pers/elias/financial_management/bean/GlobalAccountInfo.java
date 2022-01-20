package pers.elias.financial_management.bean;
import org.springframework.stereotype.Component;

/**
 * 账户相关信息记录
 */
@Component
public class GlobalAccountInfo {
    private String userName;
    private String accountBookName;
    private Integer accountBookId;
    private String accountBookIndex;
    private String accountFinancialIndex;
    private String loginDateTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    public String getAccountBookName() {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
    }

    public String getAccountBookIndex() {
        return accountBookIndex;
    }

    public void setAccountBookIndex(String accountBookIndex) {
        this.accountBookIndex = accountBookIndex;
    }

    public String getAccountFinancialIndex() {
        return accountFinancialIndex;
    }

    public void setAccountFinancialIndex(String accountFinancialIndex) {
        this.accountFinancialIndex = accountFinancialIndex;
    }

    public String getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(String loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    @Override
    public String toString() {
        return "GlobalAccountInfo{" +
                "userName='" + userName + '\'' +
                ", accountBookName='" + accountBookName + '\'' +
                ", accountBookId=" + accountBookId +
                ", accountBookIndex='" + accountBookIndex + '\'' +
                ", accountFinancialIndex='" + accountFinancialIndex + '\'' +
                ", loginDateTime='" + loginDateTime + '\'' +
                '}';
    }
}
