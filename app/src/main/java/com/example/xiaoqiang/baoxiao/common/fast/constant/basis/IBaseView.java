package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;

/**
 * author : yhx
 * time   : 2018/04/26
 * desc   : View基类
 */

public interface IBaseView {

    /**
     * 请求结束
     */
    void onRequestCompleted();

    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);

}
