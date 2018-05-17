package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.BudgetAdapter;
import com.example.xiaoqiang.baoxiao.common.been.BudgetEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.BudgetController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastRefreshLoadActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.EventConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.GlobalConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SPUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.RejectReMarkDialog;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.view.IBudgetListView;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by yhx
 * data: 2018/5/9.
 */

public class BudgetActivity extends FastRefreshLoadActivity<BudgetEntity, BudgetController> implements View.OnClickListener, IBudgetListView {
    private BaseQuickAdapter mAdapter;
    private BudgetEntity mBudgetEntity;
    private int animationIndex = GlobalConstant.GLOBAL_ADAPTER_ANIMATION_VALUE;
    private boolean animationAlways = true;
    private String companyId;
    private int requestType;//0保存 1修改

    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new BudgetAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_layout_budget_modify:
                        requestType = 1;
                        mBudgetEntity = (BudgetEntity) adapter.getItem(position);
                        showBudgetDialog();
                        break;
                }
            }
        });
        changeAdapterAnimation(0);
        changeAdapterAnimationAlways(true);
        return mAdapter;
    }

    private void showTimePickerDialog() {
        Date minDate = TimeFormatUtil.stringToDate("2010-01-01 00:00", FastConstant.TIME_FORMAT_TYPE);
        TimePickerDialog startTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        requestType = 0;

                        mBudgetEntity.setMonthTime(millseconds);
                        mBudgetEntity.setCompanyId(companyId);
                        showBudgetDialog();
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setCyclic(false)
                .setMinMillseconds(minDate.getTime())
                .setCurrentMillseconds(System.currentTimeMillis())
//                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR_MONTH)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.txt_title_blue))
                .setWheelItemTextSize(12)
                .build();
        startTimeDialog.show(getSupportFragmentManager(), "year_month");
    }

    SelectListDialog dialog2;

    void showBM() {
        if (dialog2 == null) {
            dialog2 = new SelectListDialog(this, null, FastConstant.SELECT_DIALOG_DEPARTMENT, "所属部门");
            dialog2.setItemSelectListener(new SelectListDialog.ItemSelect() {
                @Override
                public void onItemUserSelect(StateUser user) {

                }

                @Override
                public void onItemSelect(String content) {
                    Log.e("xiaoqiang", "content" + content);
                    mBudgetEntity = new BudgetEntity();
                    for (Integer key : SpManager.mBumenManager.keySet()) {
                        if (content.equals(SpManager.mBumenManager.get(key))) {
                            mBudgetEntity.setDepartment(key);
                        }
                    }
                    showTimePickerDialog();
                    dialog2.dismiss();
                }
            });
        } else {
            dialog2.show();
        }
    }

    private RejectReMarkDialog rejectReMarkDialog;

    private void showBudgetDialog() {
        rejectReMarkDialog = new RejectReMarkDialog(this);
        rejectReMarkDialog
                .setTitle("请填写   " + TimeFormatUtil.formatTime(mBudgetEntity.getMonthTime(), "yyyy年MM月") + "   预算金额：")
                .setPositiveText("提交")
                .setNegativeText("取消")
                .setOnClickDismiss(false)
                .setIsSetCordon(true)
                .setHintContent("请填写预算金额")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(rejectReMarkDialog.getRemark().trim())) {
                            ToastUtil.show("请填写预算金额");
                        } else {
                            mBudgetEntity.setBudgetAmount(Double.valueOf(rejectReMarkDialog.getRemark()));
                            if (requestType == 0) {
                                if (TextUtils.isEmpty(rejectReMarkDialog.getCordon().trim())) {
                                    ToastUtil.show("请填写预警值");
                                } else {
                                    mBudgetEntity.setCordon(Integer.valueOf(rejectReMarkDialog.getCordon()));
                                    rejectReMarkDialog.dismiss();
                                    mController.saveBudget(mBudgetEntity);
                                }
                            } else {
                                if (TextUtils.isEmpty(rejectReMarkDialog.getCordon().trim())) {
                                    ToastUtil.show("请填写预警值");
                                } else {
                                    rejectReMarkDialog.dismiss();
                                    mBudgetEntity.setCordon(Integer.valueOf(rejectReMarkDialog.getCordon()));
                                    Timber.i(new Gson().toJson(mBudgetEntity));
                                    mController.modifyBudget(mBudgetEntity);
                                }
                            }

                        }
                    }
                }).show();
    }

    @Override
    public void loadData(int page) {
        mController.queryBudgetList(page);
    }

    @Override
    protected BudgetController initController() {
        return new BudgetController().setmContext(this);
    }

    @Override
    protected void attachView() {
        mController.attachView(this);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_budget;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        companyId = SpManager.getInstance().getUserInfo().getCompany().getObjectId();

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("预算");
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onShowList(List<BudgetEntity> list) {
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
    @OnClick({R.id.activity_budget_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_budget_add:
                showBM();
                break;
        }
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
}
