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
        DecimalFormat df = new DecimalFormat(",###,##0.##");
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

    public static String formatDouble(Double value) {
        if (value == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#####.##");
        return df.format(new BigDecimal(value));
//        NumberFormat nf =NumberFormat.getInstance();
//        nf.setGroupingUsed(false);
//       return nf.format(value);
    }

    public static float DoubleToFloat(Double value) {
        if (value == null) {
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#####");
        String str = df.format(new BigDecimal(value));
        BigDecimal b1 = new BigDecimal(str);
        return b1.floatValue();
    }


    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double getPercentageNum(double v1, double v2, int scale) {
        if (v2 <= 0) {
            v2 = 0.1;
        }
        BigDecimal a = new BigDecimal(v1 + "");
        BigDecimal b = new BigDecimal(v2 + "");
        //a除以b 乘以100
        double value = (a.divide(b, 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))).doubleValue();
        Timber.i("----:" + value);
        return value;
    }


    /**
     * 格式化百分比
     *
     * @return
     */
    public static String formatPercentageNum(String amount) {
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        DecimalFormat df = new DecimalFormat("##0.00");
        return df.format(new BigDecimal(amount));
    }

}
