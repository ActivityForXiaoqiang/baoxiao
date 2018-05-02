package com.example.xiaoqiang.baoxiao.common.controller;

import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;

import rx.functions.Action1;

public class SaveController {
    private SaveView view;
    private BmobModel model;

    public SaveController(SaveView view) {
        this.view = view;
        model = new BmobModel();
    }


    public void createCompany(String name, String miaoshu) {
        view.showDialog();
        model.createCompany(name, miaoshu).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideDialog();
                view.onCompanyCreateSuccess(s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideDialog();
                view.showError(throwable);
            }
        });
    }

    public void createRequest(Applicant applicant) {
        view.showDialog();
        model.createRequest(applicant).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideDialog();
                view.onRequestCreateSuccess(s);

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
