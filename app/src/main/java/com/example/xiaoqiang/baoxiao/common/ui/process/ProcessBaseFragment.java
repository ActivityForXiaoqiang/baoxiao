package com.example.xiaoqiang.baoxiao.common.ui.process;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.ProcessAdapter;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastRefreshLoadFragment;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.EventConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.GlobalConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.BackToTopHelper;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SPUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.AlertDialog;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.RejectReMarkDialog;
import com.example.xiaoqiang.baoxiao.common.ui.TravelWayActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.IProcessListView;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.ProcessListController;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementDetailsActivity;
import com.google.gson.Gson;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 2018/4/27
 * Desc:流程列表
 */
public class ProcessBaseFragment extends FastRefreshLoadFragment<ProcessEntity, ProcessListController> implements IProcessListView {

    private BaseQuickAdapter mAdapter;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;
    private boolean animationAlways = true;
    private int pageStyle = 0;//0我的请求 1待办  2已办事宜
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
        boolean isOperate = false;
        if (pageStyle == 1) {
            isOperate = true;
        }
        mAdapter = new ProcessAdapter(isOperate);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ProcessEntity item = (ProcessEntity) mAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.item_process_reject_tv:
                        indexItem = item;
                        showRejectDialog(position);

                        break;
                    case R.id.item_process_approval_tv:
                        indexItem = item;
                        showApprovalDialog(SpManager.getInstance().getNextPoint(item.getPoint(), item.getProcessType()), position);
                        break;
                    case R.id.item_process_reference_price_l:
                        Bundle bundle = new Bundle();
                        bundle.putString("from", item.getSetout().split(" ")[1]);
                        bundle.putString("to", item.getDestination().split(" ")[1]);
                        String mode = "2";
                        if (TextUtils.equals("飞机", item.getVehicle())) {
                            mode = "2";
                        } else if (TextUtils.equals("火车", item.getVehicle())) {
                            mode = "4";
                        }
                        bundle.putString("mode", mode);
                        FastUtil.startActivity(getActivity(), TravelWayActivity.class, bundle);
                        break;
                }
            }
        });
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
        Timber.tag(TAG).i("loadData" + pageStyle + "--**" + DEFAULT_PAGE + "***--" + page);
        StateUser user = SpManager.getInstance().getUserInfo();

        String userId = user.getObjectId();
        if (pageStyle == 0 || pageStyle == 2) {
            int point = -1;
            if (pageStyle == 0) {
                //查看我的请求  关联userid 与流程装填无关
                point = -1;
            } else if (pageStyle == 2) {
                //查看我的已完成事宜  关联userid 与流程状态
                point = FastConstant.PROCESS_POINT_FINISH;//查看我的已完成流程
            }
            mController.getProcessList(page, point, userId, user.getCompany().getObjectId());
        } else if (pageStyle == 1) {
            //需要去操作的
            mController.getAgencyProcessList(page, user.getPosition(), user.getCompany().getObjectId());
        }

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
        if (adapter.getItem(position).getReject()) {
            ToastUtil.show("此报销已被退回，不能查看");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("processEntity", new Gson().toJson(adapter.getItem(position)));
        FastUtil.startActivity(getActivity(), ReimbursementDetailsActivity.class, bundle);
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
        if (DEFAULT_PAGE == 0) {
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
            if (DEFAULT_PAGE == 0) {
                mEasyStatusView.empty();
            } else {
                mAdapter.loadMoreEnd();
            }
            return;
        }
        mEasyStatusView.content();
        if (mRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(null);
        }
        mAdapter.openLoadAnimation();
        mAdapter.addData(list);
        if (list.size() < DEFAULT_PAGE_SIZE) {
            mAdapter.loadMoreEnd();
        }
    }


    @Override
    public void operateSuccess(int position) {
        mAdapter.remove(position);
    }

    private ProcessEntity indexItem;

    private void showApprovalDialog(int nextPoint, final int position) {
        String title = "是否确认批准？";
        if (nextPoint == FastConstant.PROCESS_POINT_FINISH) {
            title = "批准后将归档，是否确认批准？";
        }
        AlertDialog alertDialog = new AlertDialog(getContext());
        alertDialog.setTitle(title)
                .setPositiveText("确定")
                .setNegativeText("取消")
                .setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mController.approvalProcess(indexItem, position);
                    }
                })
                .show();
    }


    private void showRejectDialog(final int position) {
        final RejectReMarkDialog rejectReMarkDialog = new RejectReMarkDialog(getContext());
        rejectReMarkDialog
                .setTitle("请填写驳回原因：")
                .setPositiveText("驳回")
                .setNegativeText("取消")
                .setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(rejectReMarkDialog.getRemark().trim())) {
                            ToastUtil.show("请填写驳回原因");
                        } else {
                            mController.rejectProcces(indexItem, rejectReMarkDialog.getRemark(), position);
                        }
                    }
                }).show();
    }
}
