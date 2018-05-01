package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;

/**
 * author : yhx
 * time   : 2018/04/26
 * desc   : controller基类
 */

public class BaseController<V extends IBaseView> {
    private V mView;

    public void attachView(V view) {
        mView = view;
    }


    public void detachView() {
        mView = null;
    }

    public V getView() {
        return mView;
    }
}
