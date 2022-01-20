package pers.elias.financial_management.model.template_account_book.impl;

import pers.elias.financial_management.model.template_account_book.Template;

public class TravelTemplate implements Template {
    /**
     * 一级支出分类静态常量
     */
    public static final String[] firstExpenseCategory = {"旅游交通", "旅游住宿", "旅游餐饮", "旅游娱乐", "旅游用品", "旅游购物", "其他消费"};

    /**
     * 二级支出分类静态常量
     */
    public static final String[][] secondExpenseCategory = {
            {"机票费用", "火车高铁", "轮船游轮", "长途汽车", "打车公交", "高速过路", "油费"},
            {"酒店住宿", "停车费用"},
            {"聚餐吃饭", "小吃零食", "特色食品"},
            {"景点门票", "酒吧夜店", "KTV唱歌", "车辆租赁", "旅游团费", "向导费用", "其他娱乐"},
            {"衣裤鞋帽", "数码设备", "美妆护肤", "个护用品", "其他用品"},
            {"纪念用品", "日常用品", "药品保健", "其他物品"},
            {"流量费用", "其他杂费"}
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
