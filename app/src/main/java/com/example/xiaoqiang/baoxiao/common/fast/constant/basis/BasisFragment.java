package com.example.xiaoqiang.baoxiao.common.fast.constant.basis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiaoqiang.baoxiao.common.fast.constant.FastConfig;
import com.example.xiaoqiang.baoxiao.common.fast.constant.i.IBasisView;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.LoggerManager;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function:所有Fragment的基类
 */
public abstract class BasisFragment extends Fragment implements IBasisView {

    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected boolean mIsViewLoaded;
    protected Unbinder mUnBinder;
    protected final String TAG = getClass().getSimpleName();
    protected boolean mIsVisibleChanged = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        mIsFirstShow = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        beforeSetContentView();
        mContentView = inflater.inflate(getContentLayout(), container, false);
        mUnBinder = ButterKnife.bind(this, mContentView);
        mIsViewLoaded = true;
        EventBus.getDefault().register(this);
        beforeInitView();
        initView(savedInstanceState);
        if (!mIsVisibleChanged && (getUserVisibleHint() || isVisible() || !isHidden())) {
            onVisibleChanged(true);
        }
        LoggerManager.i(TAG, "mIsVisibleChanged:" + mIsVisibleChanged + ";getUserVisibleHint:" + getUserVisibleHint() + ";isHidden:" + isHidden() + ";isVisible:" + isVisible());
        return mContentView;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    @Override
    public int getContentBackground() {
        return FastConfig.getInstance(getContext()).getContentViewBackgroundResource();
    }

    @Override
    public void beforeSetContentView() {

    }

    @Override
    public void beforeInitView() {
        if (getContentBackground() > 0) {
            mContentView.setBackgroundResource(getContentBackground());
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 不在viewpager中Fragment懒加载
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onVisibleChanged(!hidden);
    }

    /**
     * 在viewpager中的Fragment懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onVisibleChanged(isVisibleToUser);
    }

    /**
     * 用户可见变化回调
     *
     * @param isVisibleToUser
     */
    protected void onVisibleChanged(final boolean isVisibleToUser) {
        LoggerManager.i(TAG, "isVisibleToUser:" + isVisibleToUser);
        mIsVisibleChanged = true;
        if (isVisibleToUser) {
            if (!mIsViewLoaded) {//避免因视图未加载子类刷新UI抛出异常
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onVisibleChanged(isVisibleToUser);
                    }
                }, 66);
            } else {
                lazyLoad();
            }
        }
    }

    private void lazyLoad() {
        if (mIsFirstShow && mIsViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }
}
