package com.example.xiaoqiang.baoxiao.common.fast.constant.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * author : yhx
 * time   : 2017/7/25
 * desc   : 数字格式化工具类
 */
public class NumberFormatterUtil {

    /**
     * 以，格式化金额
     *
     * @param amount
     * @return
     */
    public static String formatMoney(String amount) {
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        DecimalFormat df = new DecimalFormat(",###,##0.00");
        return df.format(new BigDecimal(amount));
    }

    /**
     * 以，格式化金额
     *
     * @param amount
     * @return
     */
    public static String formatMoneyHideZero(String amount) {
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        DecimalFormat df = new DecimalFormat(",###,###.##");
        return df.format(new BigDecimal(amount));
    }

    /**
     * 以，格式化金额，返回整数
     *
     * @param amount
     * @return
     */
    public static String formatMoneyWithInteger(String amount) {
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(new BigDecimal(amount));
    }

    /**
     * 格式化金额，整数转整数，小数保留两位
     *
     * @return
     */
    public static String formatMoneyWithSelfValue(String amount) {
        if (isIntegerForDouble(parseDouble(amount))) {
            return formatMoneyWithInteger(amount);
        } else {
            return formatMoney(amount);
        }
    }

    /**
     * 判断double是否是整数
     *
     * @param obj
     * @return
     */
    private static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj - Math.floor(obj) < eps;
    }

    /**
     * String转double
     *
     * @param value
     * @return
     */
    public static double parseDouble(String value) {
        double result;
        try {
            result = Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }
}
