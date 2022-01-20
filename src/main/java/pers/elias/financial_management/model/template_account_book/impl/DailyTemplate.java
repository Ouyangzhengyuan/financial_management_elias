package pers.elias.financial_management.model.template_account_book.impl;

import pers.elias.financial_management.model.template_account_book.Template;

public class DailyTemplate implements Template {
    /**
     * 一级支出分类静态常量
     */
    public static final String[] firstExpenseCategory = {"其他支出", "美妆护理", "衣服饰品", "食品酒水", "居家物业", "行车交通", "交流通讯", "休闲娱乐", "学习进修", "人情往来", "医疗保健", "金融保险"};

    /**
     * 二级支出分类静态常量
     */
    public static final String[][] secondExpenseCategory = {
            {"未知账单", "坏账烂账", "其他"},
            {"造型烫染", "护肤清洁", "彩妆用品"},
            {"衣服裤子", "帽子鞋子", "配饰首饰", "香水香波"},
            {"早餐中餐晚餐", "宵夜啤酒", "食材原料", "酒水香烟", "小吃饮品", "湿巾纸巾"},
            {"桶装饮水", "水电煤气", "物业房租", "日常用品", "厨房用品", "卫浴用品", "家居家纺", "家具维修"},
            {"公共交通", "打车的士", "火车高铁", "飞机游轮"},
            {"话费宽带", "快递服务", "闪送服务"},
            {"酒吧/夜场/唱歌", "麻将纸牌", "酒店住宿", "旅游出行"},
            {"书报杂志", "培训进修"},
            {"礼金红包", "送礼请客", "慈善捐助", "人情借款", "人情还款"},
            {"保健品", "药品费", "医疗费用"},
            {"信用租借", "信贷还款", "利息手续", "提现手续", "刷卡手续", "保险定存"}
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
