package com.example.xiaoqiang.baoxiao.common.controller;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.SignupView;

import rx.functions.Action1;

public class SignupController {
    private SignupView signupView;
    private BmobModel bmobModel;

    public SignupController(SignupView signupView) {
        this.signupView = signupView;
        bmobModel = new BmobModel();
    }

    public void signup(String account, String password) {
        signupView.showDialog();
        bmobModel.signup(account, password).subscribe(new Action1<MyUser>() {
            @Override
            public void call(MyUser user) {
                signupView.hideDialog();
                signupView.onSuccess();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                signupView.hideDialog();
                signupView.showError(throwable);
            }
        });
    }
}
