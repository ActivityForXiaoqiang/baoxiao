package com.example.xiaoqiang.baoxiao.common.application;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private final String BMOB_APPID = "dab83fd5d3a1e9a37787615bca47e6e3";

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this, BMOB_APPID);

    }
}
