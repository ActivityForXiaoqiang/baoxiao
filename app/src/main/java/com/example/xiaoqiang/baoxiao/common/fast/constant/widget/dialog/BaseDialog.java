package com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.xiaoqiang.baoxiao.R;


/**
 * author : yhx
 * time   : 2018/4/26
 * desc   : 底部弹窗基类
 */
public abstract class BaseDialog extends Dialog {

    private static final String TAG = "base_bottom_dialog";

    private static final float DEFAULT_DIM = 0.6f;

    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(getCancelOutside());
        View v = LayoutInflater.from(mContext).inflate(getLayoutRes(), null);
        setContentView(v);
        bindView(v);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindView(View v);

    @Override
    public void onStart() {
        super.onStart();

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        if (getWidth() > 0) {
            params.width = getWidth();
        } else {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        if (getHeight() > 0) {
            params.height = getHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = getGravity();
        window.setAttributes(params);
        window.setWindowAnimations(getWindowAnimations());
    }

    public int getGravity() {
        return Gravity.CENTER;
    }

    public int getWindowAnimations() {
        return -1;
    }

    public int getWidth() {
        return -1;
    }

    public int getHeight() {
        return -1;
    }

    /**
     * 背景透明度
     *
     * @return
     */
    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public boolean getCancelOutside() {
        return true;
    }

}
