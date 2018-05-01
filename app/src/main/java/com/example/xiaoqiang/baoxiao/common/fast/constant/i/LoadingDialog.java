package com.example.xiaoqiang.baoxiao.common.fast.constant.i;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.FastLoadDialog;


/**
 * Function: 用于全局配置网络请求登录Loading提示框
 */
public interface LoadingDialog {

    /**
     * @param activity
     * @return
     */
    @Nullable
    FastLoadDialog createLoadingDialog(@Nullable Activity activity);
}
