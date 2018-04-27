package com.example.xiaoqiang.baoxiao.common.controller;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.LoginView;

import rx.functions.Action1;

public class LoginController {
    private LoginView loginView;
    private BmobModel bmobModel;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        bmobModel = new BmobModel();
    }

    public void login(final String accout, String password) {
        loginView.showDialog();
        bmobModel.login(accout, password).subscribe(new Action1<MyUser>() {
            @Override
            public void call(MyUser user) {
                loginView.hideDialog();
                loginView.onSuccess(user, accout);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                loginView.hideDialog();
                loginView.showError(throwable);
            }
        });
    }
}
