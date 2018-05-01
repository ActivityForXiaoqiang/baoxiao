package com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yhx
 * data: 2018/5/1.
 */

public class LoadingDialog extends BaseDialog {
    private Context mContext;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    private boolean mCanBackClose = true;
    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_loading;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mCanBackClose) {
                        dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        getWindow().setDimAmount(0);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Glide.with(getContext()).asGif().load(R.drawable.loading).apply(GlideManager.getRequestOptions()).into(mIvLoading);
    }

    public LoadingDialog setmCanBackClose(boolean mCanBackClose) {
        this.mCanBackClose = mCanBackClose;
        return this;
    }
}
