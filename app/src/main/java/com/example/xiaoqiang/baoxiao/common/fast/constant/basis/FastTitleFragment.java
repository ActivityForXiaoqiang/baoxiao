package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;

import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.common.fast.constant.FastConfig;
import com.example.xiaoqiang.baoxiao.common.fast.constant.delegate.FastTitleDelegate;
import com.example.xiaoqiang.baoxiao.common.fast.constant.i.IFastTitleView;

/**
 * Function: 设置有TitleBar的Fragment
 */

public abstract class FastTitleFragment<c extends BaseController> extends BasisFragment implements IFastTitleView {
    protected c mController;
    protected FastTitleDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getLeftIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getLeftClickListener() {
        return null;
    }

    @Override
    public boolean isLightStatusBarEnable() {
        return FastConfig.getInstance(getContext()).getTitleConfig().isLightStatusBarEnable();
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mFastTitleDelegate = new FastTitleDelegate(mContentView, mContext, this);
        mTitleBar = mFastTitleDelegate.mTitleBar;
        mController=initController();
    }

    protected abstract c initController();
}
