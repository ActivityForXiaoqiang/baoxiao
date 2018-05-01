package com.example.xiaoqiang.baoxiao.common.fast.constant.i;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationViewHelper;


/**
 * Function:NavigationBarControl
 */
public interface NavigationBarControl {


    /**
     * @param activity
     * @return
     */
    @NonNull
    NavigationViewHelper createNavigationBarControl(Activity activity, View bottomView);
}
