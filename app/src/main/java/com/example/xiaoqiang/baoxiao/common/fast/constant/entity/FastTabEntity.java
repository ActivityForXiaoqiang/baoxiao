package com.example.xiaoqiang.baoxiao.common.fast.constant.entity;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Function: 主页Tab实体类
 */
public class FastTabEntity implements CustomTabEntity {
    public String mTitle;
    public int mSelectedIcon;
    public int mUnSelectedIcon;
    public Fragment mFragment;

    public FastTabEntity(String title, int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this.mTitle = title;
        this.mSelectedIcon = selectedIcon;
        this.mUnSelectedIcon = unSelectedIcon;
        this.mFragment = fragment;
    }

    public FastTabEntity(int unSelectedIcon, int selectedIcon, Fragment fragment) {
        mSelectedIcon = selectedIcon;
        mUnSelectedIcon = unSelectedIcon;
        mFragment = fragment;
    }

    @Override
    public String getTabTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return "";
        }
        return mTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return mSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return mUnSelectedIcon;
    }
}
