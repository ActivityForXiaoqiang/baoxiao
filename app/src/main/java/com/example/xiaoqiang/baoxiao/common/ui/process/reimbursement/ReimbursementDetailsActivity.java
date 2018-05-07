package com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.PicturesAdapter;
import com.example.xiaoqiang.baoxiao.common.adapter.TimeLineAdapter;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastTitleActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpanUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.IReimbursementDetailsView;
import com.example.xiaoqiang.baoxiao.common.ui.process.controller.ReimbursementDetailsController;
import com.example.xiaoqiang.baoxiao.common.view.NoScollGridView;
import com.google.gson.Gson;

import butterknife.BindView;

public class ReimbursementDetailsActivity extends FastTitleActivity<ReimbursementDetailsController> implements IReimbursementDetailsView {
    private final String TAG = "ReimbursementDetailsActivity";
    private ProcessEntity mProcessEntity;
    @BindView(R.id.reimbursement_default_point_listview)
    RecyclerView mTimeline;
    @BindView(R.id.reimbursement_details_personnel_tv)
    TextView mTvPersonnel;
    @BindView(R.id.reimbursement_details_position_tv)
    TextView mTvPosition;
    @BindView(R.id.reimbursement_details_account_type_tv)
    TextView mTvAccountType;
    @BindView(R.id.reimbursement_details_account_tv)
    TextView mTvAccount;
    @BindView(R.id.reimbursement_details_set_out_tv)
    TextView mTvSetout;
    @BindView(R.id.reimbursement_details_destination_tv)
    TextView mTvDestination;
    @BindView(R.id.reimbursement_details_vehicle_tv)
    TextView mTvVehicle;
    @BindView(R.id.reimbursement_details_start_time_tv)
    TextView mTvStartTime;
    @BindView(R.id.reimbursement_details_end_time_tv)
    TextView mTvEndTime;
    @BindView(R.id.reimbursement_details_amount_tv)
    TextView mTvAmount;
    @BindView(R.id.reimbursement_details_reason_tv)
    TextView mTvReason;
    @BindView(R.id.reimbursement_details_pictures_tv)
    TextView mTvPictures;
    @BindView(R.id.reimbursement_details_gridView)
    NoScollGridView mGridView;
    @BindView(R.id.reimbursement_default_seal)
    ImageView mImgSeal;
    private TimeLineAdapter mPointAdapter;
    private PicturesAdapter mPicturesAdapter;

    @Override
    public int getContentLayout() {
        mProcessEntity = new Gson().fromJson(getIntent().getStringExtra("processEntity"), ProcessEntity.class);
        return R.layout.activity_reimbursement_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mController.queryProcessDetails(mProcessEntity.getObjectId());
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("报销详情");
    }

    @Override
    protected ReimbursementDetailsController initController() {
        return new ReimbursementDetailsController().setmContext(this);
    }

    @Override
    protected void attachView() {
        mController.attachView(this);
    }

    @Override
    public void onRequestCompleted() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onShowProcess(ProcessEntity ProcessEntity) {
        Timber.i("bmob" + new Gson().toJson(ProcessEntity));
        this.mProcessEntity = ProcessEntity;
        initParameters();
    }

    private void initParameters() {
        mTimeline.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTimeline.setHasFixedSize(true);
        mTimeline.setNestedScrollingEnabled(false);
        TimeLineAdapter adapter = new TimeLineAdapter(mProcessEntity.getPointList(), false, mProcessEntity.getPoint(), mProcessEntity
                .getProcessType());
        mTimeline.setAdapter(adapter);

        mTvPersonnel.setText(mProcessEntity.getCreatorName());
        mTvPosition.setText(SpManager.getInstance().mPositionManager.get(mProcessEntity.getPosition()));
        mTvAccountType.setText(mProcessEntity.getAccountType() + "：");
        mTvAccount.setText(mProcessEntity.getAccount());
        mTvReason.setText(mProcessEntity.getReason());
        if (mProcessEntity.getAmount() != null) {
            mTvAmount.setText(NumberFormatterUtil.formatMoneyHideZero(mProcessEntity.getAmount() + ""));
        }

        if (!TextUtils.isEmpty(mProcessEntity.getSetout())) {
            mTvSetout.setText(mProcessEntity.getSetout());
        } else {
            mTvSetout.setText(getRedSpan("未填"));
        }

        if (!TextUtils.isEmpty(mProcessEntity.getDestination())) {
            mTvDestination.setText(mProcessEntity.getDestination());
        } else {
            mTvDestination.setText(getRedSpan("未填"));
        }

        if (!TextUtils.isEmpty(mProcessEntity.getVehicle())) {
            mTvVehicle.setText(mProcessEntity.getVehicle());
        } else {
            mTvVehicle.setText(getRedSpan("未填"));
        }

        if (mProcessEntity.getStartTime() != null) {
            mTvStartTime.setText(TimeFormatUtil.formatTime(mProcessEntity.getStartTime(), FastConstant.TIME_FORMAT_TYPE));
        } else {
            mTvStartTime.setText(getRedSpan("未填"));
        }

        if (mProcessEntity.getEndTime() != null) {
            mTvEndTime.setText(TimeFormatUtil.formatTime(mProcessEntity.getEndTime(), FastConstant.TIME_FORMAT_TYPE));
        } else {
            mTvEndTime.setText(getRedSpan("未填"));
        }

        if (mProcessEntity.getPoint() == FastConstant.PROCESS_POINT_FINISH) {
            mImgSeal.setVisibility(View.VISIBLE);
        }

        if (mProcessEntity.getImgs() != null) {
            mPicturesAdapter = new PicturesAdapter(this, mProcessEntity.getImgs());
            mGridView.setAdapter(mPicturesAdapter);
        } else {
            mTvPictures.setText(new SpanUtil().append("图片：").append("(未上传)").setForegroundColor(ContextCompat.getColor(this, R.color
                    .colorRed)).create());
        }
    }

    private CharSequence getRedSpan(String content) {
        return new SpanUtil().append(content).setForegroundColor(ContextCompat.getColor(this, R.color.colorRed)).create();
    }
}
