package com.example.xiaoqiang.baoxiao.common.controller;

import android.util.Log;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;

import rx.functions.Action1;

public class UpdataController {
    UpdataView view;
    BmobModel model;

    public UpdataController(UpdataView view) {
        this.view = view;
        model = new BmobModel();
    }

    public void updataUser(MyUser user) {
        view.showDialog();
        model.updataUser(user).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.hideDialog();
                view.onUpdataUserSuccess();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideDialog();
                view.showError(throwable);
            }
        });
    }
    public void updataUser(MyUser user,String id) {
        Log.e("??",id);
        view.showDialog();
        model.updataUser(user,id).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.hideDialog();
                view.onUpdataUserSuccess();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideDialog();
                view.showError(throwable);
            }
        });
    }
}
