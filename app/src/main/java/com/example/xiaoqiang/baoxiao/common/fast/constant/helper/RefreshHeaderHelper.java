package com.example.xiaoqiang.baoxiao.common.fast.constant.helper;

import android.content.Context;

import com.example.xiaoqiang.baoxiao.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * Function: 刷新头帮助类用于全局设置
 */
public class RefreshHeaderHelper {
    private static volatile RefreshHeaderHelper sInstance;

    private RefreshHeaderHelper() {
    }

    public static RefreshHeaderHelper getInstance() {
        if (sInstance == null) {
            synchronized (RefreshHeaderHelper.class) {
                if (sInstance == null) {
                    sInstance = new RefreshHeaderHelper();
                }
            }
        }
        return sInstance;
    }

    public RefreshHeader getRefreshHeader(Context mContext) {
        MaterialHeader materialHeader = new MaterialHeader(mContext);
        materialHeader.setColorSchemeColors(mContext.getResources().getColor(R.color.colorTextBlack),
                mContext.getResources().getColor(R.color.colorTextBlackLight));
        return materialHeader;
    }
}
