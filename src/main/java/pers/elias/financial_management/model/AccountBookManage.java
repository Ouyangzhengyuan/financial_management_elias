package pers.elias.financial_management.model;

public class AccountBookManage {
    private String accountBookName;
    private Integer count;
    private Double allIn;
    private Double allEx;
    private Double allDebts;
    private Double netAssets;

    public String getAccountBookName() {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getAllIn() {
        return allIn;
    }

    public void setAllIn(Double allIn) {
        this.allIn = allIn;
    }

    public Double getAllEx() {
        return allEx;
    }

    public void setAllEx(Double allEx) {
        this.allEx = allEx;
    }

    public Double getAllDebts() {
        return allDebts;
    }

    public void setAllDebts(Double allDebts) {
        this.allDebts = allDebts;
    }

    public Double getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(Double netAssets) {
        this.netAssets = netAssets;
    }

    @Override
    public String toString() {
        return "AccountBookManage{" +
                "accountBookName='" + accountBookName + '\'' +
                ", count=" + count +
                ", allIn=" + allIn +
                ", allEx=" + allEx +
                ", allDebts=" + allDebts +
                ", netAssets=" + netAssets +
                '}';
    }
}
