package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;

@Component
public class GlobalAccountInfo {
    private String userName;
    private String accountBookName;
    private Integer accountBookId;

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

    @Override
    public String toString() {
        return "GlobalAccountInfo{" +
                "userName='" + userName + '\'' +
                ", accountBookName='" + accountBookName + '\'' +
                ", accountBookId=" + accountBookId +
                '}';
    }
}
