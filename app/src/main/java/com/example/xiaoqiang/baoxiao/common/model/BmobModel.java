package com.example.xiaoqiang.baoxiao.common.model;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;

import cn.bmob.v3.BmobUser;
import rx.Observable;

public class BmobModel {

    public rx.Observable<MyUser> login(String account, String password) {
        return BmobUser.loginByAccountObservable(MyUser.class, account, password);
    }

    public Observable<MyUser> signup(String account, String password) {
        MyUser user = new MyUser();
        user.setUsername(account);
        user.setPassword(password);
        return user.signUpObservable(MyUser.class);
    }
}
