package com.example.xiaoqiang.baoxiao.common.ui.process;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.ProcessAdapter;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastRefreshLoadFragment;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.EventConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.GlobalConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.BackToTopHelper;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SPUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.IProcessListView;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.ProcessListController;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementActivity;
import com.google.gson.Gson;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created: 2018/4/27
 * Desc:流程列表
 */
public class ProcessBaseFragment extends FastRefreshLoadFragment<ProcessEntity, ProcessListController> implements IProcessListView {

    private BaseQuickAdapter mAdapter;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;
    private boolean animationAlways = true;
    private int pageStyle = 0;//0我的请求 1待办  2已办事宜
    private int index = 0;
    ArrayList<ProcessEntity> pList = new ArrayList<>();

    public static ProcessBaseFragment newInstance(int pageStyle) {
        Bundle args = new Bundle();
        ProcessBaseFragment fragment = new ProcessBaseFragment();
        args.putInt("pageStyle", pageStyle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        pageStyle = getArguments().getInt("pageStyle");
    }

    @Override
    public BaseQuickAdapter<ProcessEntity, BaseViewHolder> getAdapter() {
        mAdapter = new ProcessAdapter();
        changeAdapterAnimation(0);
        changeAdapterAnimationAlways(true);
        return mAdapter;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fast_layout_multi_status_refresh_recycler;
    }

    @Override
    public int getContentBackground() {
        return -1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        new BackToTopHelper().init(mRecyclerView, mEasyStatusView);
    }

    @Override
    public void loadData(int page) {
        Gson gson = new Gson();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        MyUser user = gson.fromJson(gson.toJson(bmobUser), MyUser.class);
        String userId = user.getObjectId();
        int status = user.getPosition();
        if (pageStyle == 0) {
            //查看我的请求  关联userid 与流程装填无关
            status = -1;
        } else if (pageStyle == 1) {
            //只跟流程相关
            userId = "";
        } else if (pageStyle == 2) {
            //查看我的已完成事宜  关联userid 与流程状态
            status = 6;//查看我的已完成流程
        }
        mController.queryProcessList(index, status, userId);
    }

    @Override
    protected ProcessListController initController() {
        return new ProcessListController().setmContext(getContext());
    }

    @Override
    protected void attachView() {
        mController.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mController != null) {
            mController.detachView();
        }
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<ProcessEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        startActivity(new Intent(getContext(), ReimbursementActivity.class));
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION)
    public void changeAdapterAnimation(int index) {
        if (mAdapter != null) {
            animationIndex = (int) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_INDEX, animationIndex - 1) + 1;
            mAdapter.openLoadAnimation(animationIndex);
        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventConstant.EVENT_KEY_CHANGE_ADAPTER_ANIMATION_ALWAYS)
    public void changeAdapterAnimationAlways(boolean always) {
        if (mAdapter != null) {
            animationAlways = (Boolean) SPUtil.get(mContext, SPConstant.SP_KEY_ACTIVITY_ANIMATION_ALWAYS, true);
            mAdapter.isFirstOnly(!animationAlways);
        }
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void showError(String msg) {
        if (index == 0) {
            mEasyStatusView.error();
        } else {
            ToastUtil.show(msg);
        }
    }

    @Override
    public void onShowList(List<ProcessEntity> list) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadmore();
        }
        mAdapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            if (index == 0) {
                mEasyStatusView.empty();
            } else {
                mAdapter.loadMoreEnd();
            }
            return;
        }
        mEasyStatusView.content();
        if (mRefreshLayout.isRefreshing())
            mAdapter.setNewData(null);
        mAdapter.openLoadAnimation();
        mAdapter.addData(list);
        if (list.size() < DEFAULT_PAGE_SIZE) {
            mAdapter.loadMoreEnd();
        }
    }


}
