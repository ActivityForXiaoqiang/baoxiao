package com.example.xiaoqiang.baoxiao.common.fast.constant.helper;

import com.aries.ui.view.radius.RadiusViewDelegate;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.application.MyApplication;

/**
 * Function:RadiusViewHelper
 */
public class RadiusViewHelper {

    private static volatile RadiusViewHelper sInstance;

    private RadiusViewHelper() {
    }

    public static RadiusViewHelper getInstance() {
        if (sInstance == null) {
            synchronized (RadiusViewHelper.class) {
                if (sInstance == null) {
                    sInstance = new RadiusViewHelper();
                }
            }
        }
        return sInstance;
    }

    public void setRadiusViewAdapter(RadiusViewDelegate delegate) {
        if (!MyApplication.isSupportElevation()) {
            delegate.setStrokeWidth(MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.dp_line_size));
            delegate.setStrokeColor(MyApplication.getContext().getResources().getColor(R.color.colorLineGray));
        }
    }
}
