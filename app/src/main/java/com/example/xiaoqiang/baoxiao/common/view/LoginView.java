package com.example.xiaoqiang.baoxiao.common.view;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;

public interface LoginView extends BmobView {
    void onSuccess(MyUser user, String accout);
}
