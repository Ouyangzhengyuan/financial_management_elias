package pers.elias.financial_management.model.template_account_book.impl;

import pers.elias.financial_management.model.template_account_book.Template;

public class BusinessTemplate implements Template {
    /**
     * 一级支出分类静态常量
     */
    public static final String[] firstExpenseCategory = {"材料货品", "人力支出", "办公杂物", "运营推广", "固定资产", "金融保险", "其他杂项"};

    /**
     * 二级支出分类静态常量
     */
    public static final String[][] secondExpenseCategory = {
            {"材料进货"},
            {"招聘培训", "员工薪资", "员工福利", "员工社保"},
            {"房租费用", "水电物管", "办公用品", "通信宽带", "维修保养", "交通差旅", "日常餐饮"},
            {"市场运营", "邮寄费用", "税务费用", "广告费用", "招待费用"},
            {"生产设备", "车辆费用", "房产费用"},
            {"手续支出", "利息支出", "投资亏损", "保费支出"},
            {"烂账损失", "赔偿罚款", "其他支出"}
    };

    /**
     * 一级收入分类静态常量
     */
    public static final String[] firstIncomeCategory = {"职业收入", "其他收入"};

    /**
     * 二级收入分类静态常量
     */
    public static final String[][] secondIncomeCategory = {
            {"工资收入", "加班收入", "奖金收入", "兼职收入", "投资收入", "经营收入"},
            {"累积款项", "礼金收入", "中奖收入", "家庭补贴"}
    };

    /**
     * 一级金融账户静态常量
     */
    public static final String[] accountFinancialList = {
            "现金账户",
            "金融账户",
            "虚拟账户",
            "信用账户",
            "负债账户"
    };

    /**
     * 二级金融账户静态常量
     */
    public static final String[][] accountType = {
            {"现金口袋"},
            {"银行卡/储蓄卡"},
            {"支付宝", "余额宝", "微信钱包", "公交卡"},
            {"花呗", "信用卡"},
            {"应付款项", "债务"}
    };

    @Override
    public String[] getFirstExpenseCategory() {
        return firstExpenseCategory;
    }

    @Override
    public String[][] getSecondExpenseCategory() {
        return secondExpenseCategory;
    }

    @Override
    public String[] getFirstIncomeCategory() {
        return firstIncomeCategory;
    }

    @Override
    public String[][] getSecondIncomeCategory() {
        return secondIncomeCategory;
    }

    @Override
    public String[] getAccountFinancialList() {
        return accountFinancialList;
    }

    @Override
    public String[][] getAccountType() {
        return accountType;
    }
}
