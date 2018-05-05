package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;

import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.common.fast.constant.FastConfig;
import com.example.xiaoqiang.baoxiao.common.fast.constant.delegate.FastTitleDelegate;
import com.example.xiaoqiang.baoxiao.common.fast.constant.i.IFastTitleView;

/**
 * Function: title 基类
 */

public abstract class FastTitleRefreshLoadBaseActivity extends BasisActivity implements IFastTitleView {
    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public int getLeftIcon() {
        return FastConfig.getInstance(this).getTitleConfig().getLeftTextDrawable();
    }

    @Override
    public View.OnClickListener getLeftClickListener() {
        if (FastConfig.getInstance(this).getTitleConfig().isLeftTextFinishEnable()) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            };
        } else {
            return null;
        }
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return FastConfig.getInstance(this).getTitleConfig().isLightStatusBarEnable();
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastTitleDelegate = new FastTitleDelegate(mContentView, mContext, this);
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }

}
