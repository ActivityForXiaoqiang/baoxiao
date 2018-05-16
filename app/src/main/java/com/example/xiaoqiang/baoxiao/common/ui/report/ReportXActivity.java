package com.example.xiaoqiang.baoxiao.common.ui.report;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.ReportXAdapter;
import com.example.xiaoqiang.baoxiao.common.been.DayTime;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.FastConfig;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.FastTitleActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.i.IMultiStatusView;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.DisplayUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpanUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.marno.easystatelibrary.EasyStatusView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class ReportXActivity extends FastTitleActivity implements View.OnClickListener {
    @BindView(R.id.mHorizontalScrollView)
    HorizontalScrollView mHorizontalScrollView;
    @BindView(R.id.activity_report_x_department)
    TextView mTvDepartment;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.esv_layoutFastLib)
    EasyStatusView mEasyStatusView;
    @BindView(R.id.report_x_time)
    TextView mTvTime;
    private ReportXAdapter mAdapter;
    private Calendar mCalendar;
    private StateUser mStateUser;
    private View footerView;//脚布局
    private TextView mTvSum;//总额度tv

    public int getContentLayout() {
        return R.layout.activity_report_x;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (mEasyStatusView != null) {
            IMultiStatusView iMultiStatusView = FastConfig.getInstance(mContext).getMultiStatusView().createMultiStatusView();
            mEasyStatusView.setLoadingView(iMultiStatusView.getLoadingView());
            mEasyStatusView.setEmptyView(iMultiStatusView.getEmptyView());
            mEasyStatusView.setErrorView(iMultiStatusView.getErrorView());
            mEasyStatusView.setNoNetworkView(iMultiStatusView.getNoNetView());
            mEasyStatusView.content();
            mEasyStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentStatus = mEasyStatusView.getCurrentStatus();
                    if (currentStatus != 2 && currentStatus != 1) {//非loading且非content状态
//                        mStatusView.loading();//有自己的loadingdialog 暂不需要布局的加载loading了
//                        mIFastRefreshLoadView.onRefresh(mRefreshLayout);
                    }
                }
            });
        }

        footerView = View.inflate(this, R.layout.item_layout_report, null);
        mTvSum = footerView.findViewById(R.id.item_layout_report_amount);
        mTvSum.setMaxLines(4);
        footerView.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        super.loadData();
        mStateUser = SpManager.getInstance().getUserInfo();
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        mAdapter = new ReportXAdapter(this, new ArrayList<ProcessEntity>());
        mListView.setAdapter(mAdapter);
        mListView.addFooterView(footerView);
//        List<ProcessEntity> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            ProcessEntity pe = new ProcessEntity();
//            list.add(pe);
//        }
//        mAdapter.addData(list);
        initTime();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("报表");
    }

    @Override
    protected BaseController initController() {
        return null;
    }

    @Override
    protected void attachView() {

    }

    @Override
    @OnClick({R.id.report_x_left, R.id.report_x_right, R.id.report_x_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_x_left:
                changeMonth(false);
                break;
            case R.id.report_x_right:
                changeMonth(true);
                break;
            case R.id.report_x_time:
                showTimePickerDialog();
                break;
        }
    }

    private long clickTime = 0;

    private void changeMonth(boolean isNext) {
        if (System.currentTimeMillis() - clickTime < 300) {
            return;
        }
        int month;
        if (isNext) {
            month = 1;
        } else {
            month = -1;
        }
        mCalendar.add(Calendar.MONTH, month);
        clickTime = System.currentTimeMillis();
        initTime();
    }

    private void showTimePickerDialog() {
        Date minDate = TimeFormatUtil.stringToDate("2000-01-01 00:00", FastConstant.TIME_FORMAT_TYPE);
        TimePickerDialog startTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        mCalendar.setTimeInMillis(millseconds);
                        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
                        initTime();
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setCyclic(false)
                .setMinMillseconds(minDate.getTime())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR_MONTH)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.txt_title_blue))
                .setWheelItemTextSize(12)
                .build();
        startTimeDialog.show(getSupportFragmentManager(), "year_month");
    }

    private void initTime() {
        mTvTime.setText(TimeFormatUtil.formatTime(mCalendar.getTimeInMillis(), "yyyy-MM"));
        queryProcessList();
    }

    /**
     * 获取指定月份的报销流程
     */
    public void queryProcessList() {
        //  point当前节点 详细注解看fastConstans类
        showLoadingDialog();
//        获取当前月
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(mCalendar);
        DayTime dayTime = TimeFormatUtil.getMonthTimeOfMonth(mCalendar);
        Timber.i(new Gson().toJson(dayTime));
        //--and条件1
        BmobQuery<ProcessEntity> eq1 = new BmobQuery<>();
        eq1.addWhereLessThanOrEqualTo("updatedAt", dayTime.getMaxDate());//時間<=MaxTime
        //--and条件2
        BmobQuery<ProcessEntity> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("updatedAt", dayTime.getMinDate());//時間>=MinTime
        List<BmobQuery<ProcessEntity>> andQuery = new ArrayList<>();
        andQuery.add(eq1);
        andQuery.add(eq2);

        BmobQuery<ProcessEntity> query = new BmobQuery<>();
        query.and(andQuery);

        int position = mStateUser.getPosition();
        if (position == 0 || position == 1 || position == 2) {
            query.addWhereEqualTo("userId", mStateUser.getUser().getObjectId());
        } else if (position == 3) {
            query.addWhereEqualTo("department", mStateUser.getDepartment());
        } else if (position == 5 || position == 4) {

        }
//            query.setLimit(pageSize);// 限制最多pageSize条数据结果作为一页
//            query.setSkip(pageSize * pageNo);//忽略条目
        query.addWhereEqualTo("companyId", mStateUser.getCompany().getObjectId());
        query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FINISH);
        query.order("-createTime");// 根据createTime字段降序显示数据
        query.findObjects(new FindListener<ProcessEntity>() {

            @Override
            public void done(List<ProcessEntity> object, BmobException e) {
                onShowList(object);
                if (e == null) {
                    Timber.i("bmob" + "成功：" + new Gson().toJson(object));
                } else {
                    ToastUtil.show(e.getMessage());
//                    getView().showError(e.getMessage().toString());
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }

    /**
     * 查询一个月的报销总额
     */
    private void queryBudgetAmountSumByMonth() {
        DayTime dayTime = TimeFormatUtil.getMonthTimeOfMonth(mCalendar);
        Timber.i(new Gson().toJson(dayTime));
        //--and条件1
        BmobQuery<ProcessEntity> eq1 = new BmobQuery<>();
        eq1.addWhereLessThanOrEqualTo("updatedAt", dayTime.getMaxDate());//時間<=MaxTime
        //--and条件2
        BmobQuery<ProcessEntity> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("updatedAt", dayTime.getMinDate());//時間>=MinTime
        List<BmobQuery<ProcessEntity>> andQuery = new ArrayList<>();
        andQuery.add(eq1);
        andQuery.add(eq2);

        BmobQuery<ProcessEntity> query = new BmobQuery<>();
        query.and(andQuery);

        int position = mStateUser.getPosition();
        if (position == 0 || position == 1 || position == 2) {
            query.addWhereEqualTo("userId", mStateUser.getUser().getObjectId());
        } else if (position == 3) {
            query.addWhereEqualTo("department", mStateUser.getDepartment());
        } else if (position == 5 || position == 4) {

        }
//            query.setLimit(pageSize);// 限制最多pageSize条数据结果作为一页
//            query.setSkip(pageSize * pageNo);//忽略条目
        query.addWhereEqualTo("companyId", mStateUser.getCompany().getObjectId());
        query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FINISH);
        query.sum(new String[]{"amount"});
        query.findStatistics(ProcessEntity.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                dismissLoadingDialog();
                if (e == null) {
                    Message message = new Message();
                    if (ary != null) {//
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            double sum = obj.getDouble("_sumAmount");//_(关键字)+首字母大写的列名
                            fillData(sum);
//                            valueSet1.add(new BarEntry(index + 1, (float) sum));
                            Timber.i("報銷总額：" + NumberFormatterUtil.formatDouble(sum));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        //没有记录
//                        ToastUtil.show("查询成功，无数据");
                    }
                } else {
                    ToastUtil.show(e.getMessage());
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }

    private List<ProcessEntity> mDataList;

    private void onShowList(List<ProcessEntity> list) {
        if (list == null || list.size() == 0) {
            dismissLoadingDialog();
            footerView.setVisibility(View.GONE);
            mEasyStatusView.empty();
            mHorizontalScrollView.smoothScrollTo(mTvDepartment.getLeft()-(DisplayUtil.getWindowWidth(this)-mTvDepartment.getWidth())/2, 0);

            return;
        }
        mDataList = list;
        queryBudgetAmountSumByMonth();
    }

    /**
     * 填充数据
     */
    private void fillData(double sum) {
        mHorizontalScrollView.smoothScrollTo(0, 0);
        mEasyStatusView.content();
        mAdapter.setNewData(mDataList);
        footerView.setVisibility(View.VISIBLE);
        mTvSum.setText(new SpanUtil()
                .append("总额：")
                .append(NumberFormatterUtil.formatMoneyHideZero(sum + ""))
                .setForegroundColor(ContextCompat.getColor(this, R.color.colorRed))
                .create());
    }

    private LoadingDialog loadingDialog;

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


}
