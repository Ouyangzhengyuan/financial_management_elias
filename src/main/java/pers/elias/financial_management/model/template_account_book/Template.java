package pers.elias.financial_management.model.template_account_book;

public interface Template {
     String[] getFirstExpenseCategory();
     String[][] getSecondExpenseCategory();
     String[] getFirstIncomeCategory();
     String[][] getSecondIncomeCategory();
     String[] getAccountFinancialList();
     String[][] getAccountType();
}
