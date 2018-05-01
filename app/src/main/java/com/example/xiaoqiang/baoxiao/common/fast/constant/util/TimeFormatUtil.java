package com.example.xiaoqiang.baoxiao.common.fast.constant.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Function:时间转换工具
 */
public class TimeFormatUtil {

    /**
     * 格式化星期
     *
     * @param millis
     * @return 1-星期日...7-星期六
     */
    public static int formatWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int index = calendar.get(Calendar.DAY_OF_WEEK) + 1;
        return index;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        return formatTime(new Date(time), format);
    }

    public static String formatTime(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = new Date();
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }
}
