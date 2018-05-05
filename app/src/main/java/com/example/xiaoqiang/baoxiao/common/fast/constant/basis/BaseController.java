package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;

import android.content.Context;

import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;

/**
 * author : yhx
 * time   : 2018/04/26
 * desc   : controller基类
 */

public class BaseController<V extends IBaseView> {
    private LoadingDialog loadingDialog;

    public  void showLoadingDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dissmissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

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
