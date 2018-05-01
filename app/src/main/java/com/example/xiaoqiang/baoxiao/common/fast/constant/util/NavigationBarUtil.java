package com.example.xiaoqiang.baoxiao.common.fast.constant.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.LoggerManager;

import java.lang.reflect.Method;

/**
 * Function: 导航栏控制工具类
 */
public class NavigationBarUtil {

    /**
     * 判断底部navigator是否已经显示
     *
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        try {
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } catch (Exception e) {
            LoggerManager.e("hasSoftKeys:" + e.getMessage());
            return false;
        }

    }

    /**
     * 获取导航栏高度
     *
     * @param windowManager
     * @return
     */
    public static int getNavigationBarHeight(WindowManager windowManager) {
        int dpi = 0;
        try {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                dpi = dm.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dpi - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            LoggerManager.e("getNavigationBarHeight:" + e.getMessage());
            return 0;
        }

    }
}
