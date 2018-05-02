package com.example.xiaoqiang.baoxiao.common.ui.process;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastTitleActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.EventConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.LoggerManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.TabLayoutManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SPUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * author : yhx
 * time   : 2018/4/28
 * desc   :
 */
public class ProcessListActivity extends FastTitleActivity {

    @BindView(R.id.vp_contentFastLib)
    ViewPager vpContent;
    @BindView(R.id.fast_layout_title_tab)
    LinearLayout mTabLayout;
    private List<Fragment> listFragment = new ArrayList<>();
    private SegmentTabLayout mSegmentTab;
    private SlidingTabLayout mSlidingTab;
    private View viewSliding;
    private View viewSegment;

    private boolean isSliding = true;

    @Override
    public int getContentBackground() {
        return R.color.colorBackground;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        LoggerManager.d(TAG, "refreshActivityTab:" + isSliding);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        isSliding = true;
        if (isSliding && viewSliding == null) {
            viewSliding = View.inflate(mContext, R.layout.fast_layout_activity_sliding, null);
            mSlidingTab = viewSliding.findViewById(R.id.tabLayout_slidingActivity);
        }
        if (isSliding) {
            if (mTabLayout.indexOfChild(viewSliding) == -1) {
                mTabLayout.addView(viewSliding);
            }

            viewSliding.setVisibility(View.VISIBLE);

            if (viewSegment != null) {
                viewSegment.setVisibility(View.GONE);
            }
        }

        titleBar.setTitleMainText("我的流程");
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_title_tab_view_pager;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void initView(Bundle savedInstanceState) {

        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
//        setTab();
    }

    @Override
    public void loadData() {
        super.loadData();
        setTab();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            LoggerManager.d(TAG, "竖屏");
//        } else {
//            LoggerManager.d(TAG, "横屏");
//        }
//        setTab();
//    }

    private void setTab() {
        isSliding = (boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_TAB_SLIDING, isSliding);
        vpContent.removeAllViews();
        listFragment.clear();

        listFragment.add(ProcessBaseFragment.newInstance(0));
        listFragment.add(ProcessBaseFragment.newInstance(1));
        listFragment.add(ProcessBaseFragment.newInstance(2));
        if (isSliding) {
            TabLayoutManager.getInstance().setSlidingTabData(this, mSlidingTab, vpContent,
                    getTitles(R.array.arrays_tab_activity), listFragment);
        } else {
            TabLayoutManager.getInstance().setSegmentTabData(this, mSegmentTab, vpContent,
                    getResources().getStringArray(R.array.arrays_tab_activity), listFragment);
        }
        //SlidingTabLayout--需这样切换一下不然选中变粗没有效果不知是SlidingTabLayout BUG还是设置问题
        mSlidingTab.setCurrentTab(1);
        mSlidingTab.setCurrentTab(0);
    }

    private List<String> getTitles(int array) {
        return Arrays.asList(getResources().getStringArray(array));
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_REFRESH_ACTIVITY_TAB)
    public void refreshActivityTab(boolean isSliding) {
        mIsFirstShow = true;
        setTitleBar(mTitleBar);
        setTab();
    }

    @Override
    protected BaseController initController() {
        return null;
    }

    @Override
    protected void attachView() {

    }
}
