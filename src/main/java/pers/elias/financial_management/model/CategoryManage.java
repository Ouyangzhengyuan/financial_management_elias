package pers.elias.financial_management.model;

public class CategoryManage {
    private String categoryFirstName;
    private String categorySecondName;

    public String getCategoryFirstName() {
        return categoryFirstName;
    }

    public void setCategoryFirstName(String categoryFirstName) {
        this.categoryFirstName = categoryFirstName;
    }

    public String getCategorySecondName() {
        return categorySecondName;
    }

    public void setCategorySecondName(String categorySecondName) {
        this.categorySecondName = categorySecondName;
    }

    @Override
    public String toString() {
        return "CategoryManage{" +
                "categoryFirstName='" + categoryFirstName + '\'' +
                ", categorySecondName='" + categorySecondName + '\'' +
                '}';
    }
}
