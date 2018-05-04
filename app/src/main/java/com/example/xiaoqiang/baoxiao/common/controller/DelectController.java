package com.example.xiaoqiang.baoxiao.common.controller;

import com.example.xiaoqiang.baoxiao.common.model.BmobModel;
import com.example.xiaoqiang.baoxiao.common.view.BmobView;
import com.example.xiaoqiang.baoxiao.common.view.DelectView;

import rx.functions.Action1;

public class DelectController {
    DelectView view;
    BmobModel model;

    public DelectController(DelectView view) {
        this.view = view;
        model = new BmobModel();
    }

    public void delete(String id) {
        model.delectReuest(id).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }
}
