package com.example.xiaoqiang.baoxiao.common.ui.report;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListPopwindow;
import com.example.xiaoqiang.baoxiao.common.view.MyHorizontalScrollView;
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
    MyHorizontalScrollView mHorizontalScrollView;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.esv_layoutFastLib)
    EasyStatusView mEasyStatusView;
    @BindView(R.id.report_x_time)
    TextView mTvTime;
    @BindView(R.id.activity_report_x_name)
    TextView mTvName;
    @BindView(R.id.activity_report_x_position)
    TextView mTvPosition;
    @BindView(R.id.activity_report_x_department)
    TextView mTvDepartment;
    @BindView(R.id.activity_report_x_name_img)
    ImageView mImgName;
    @BindView(R.id.activity_report_x_position_img)
    ImageView mImgPosition;
    @BindView(R.id.activity_report_x_department_img)
    ImageView mImgDepartment;
    @BindView(R.id.activity_report_x_department_r)
    RelativeLayout mTvDepartmentR;
    private ReportXAdapter mAdapter;
    private Calendar mCalendar;
    private StateUser mStateUser;
    private View footerView;//脚布局
    private TextView mTvSum;//总额度tv
    private List<StateUser> mUserList;
    private List<String> ulist, plist, dlist;//人员集合 ,职位集合，部门集合
    private int conditionIndex;//0,1,2  0姓名 1职位 2部门
    private String mUserId;
    private int mPosition = -1;
    private int mDepartment = -1;

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

        mStateUser = SpManager.getInstance().getUserInfo();

        if (mStateUser.getPosition() == 0 || mStateUser.getPosition() == 1 || mStateUser.getPosition() == 2) {

        } else if (mStateUser.getPosition() == 3) {
            mImgName.setVisibility(View.VISIBLE);
            mImgPosition.setVisibility(View.VISIBLE);
        } else if (mStateUser.getPosition() == 5 || mStateUser.getPosition() == 4) {
            mImgName.setVisibility(View.VISIBLE);
            mImgPosition.setVisibility(View.VISIBLE);
            mImgDepartment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        mAdapter = new ReportXAdapter(this, new ArrayList<ProcessEntity>());
        mListView.setAdapter(mAdapter);
        mListView.addFooterView(footerView);
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
    @OnClick({R.id.report_x_left, R.id.report_x_right, R.id.report_x_time,
            R.id.activity_report_x_name, R.id.activity_report_x_position, R.id.activity_report_x_department})
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
            case R.id.activity_report_x_name:
                if (mImgName.getVisibility() == View.GONE) {
                    return;
                }
                conditionIndex = 0;
                if (ulist == null || ulist.size() == 0) {
                    getPersonList();
                } else {
                    showPop(view, ulist);
                }

                break;
            case R.id.activity_report_x_position:
                if (mImgPosition.getVisibility() == View.GONE) {
                    return;
                }
                conditionIndex = 1;
                if (plist == null) {
                    plist = new ArrayList();
                    plist.add("全部");
                    plist.addAll(SpManager.getInstance().mPositionData);
                }
                showPop(view, plist);
                break;
            case R.id.activity_report_x_department:
                if (mImgDepartment.getVisibility() == View.GONE) {
                    return;
                }
                conditionIndex = 2;
                if (dlist == null) {
                    dlist = new ArrayList();
                    dlist.add("全部");
                    dlist.addAll(SpManager.getInstance().mBumenData);
                }

                showPop(view, dlist);
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

    private SelectListPopwindow popwindow;

    //选择条件弹框
    private void showPop(View v, List<String> contents) {
        popwindow = new SelectListPopwindow(this, contents, DisplayUtil.getWindowHeight(this), new
                SelectListPopwindow.OnMyPopListener() {
                    @Override
                    public void onItemClick(int position, String content) {
                        //初始化 条件
                        mUserId = null;
                        mPosition = -1;
                        mDepartment = -1;
                        if (position == 0) {
                            initCondition(content);
                            queryProcessList();
                            return;
                        }
                        if (conditionIndex == 0) {
                            //按人员
                            mUserId = mUserList.get(position - 1).getObjectId();
                        } else if (conditionIndex == 1) {
                            if (position == 1) {
                                mPosition = position - 1;
                            } else {
                                mPosition = position;
                            }
                            //按职位
                        } else {
                            //按部门
                            mDepartment = position - 1;
                        }
                        initCondition(content);
                        queryProcessList();
                    }

                    @Override
                    public void onDismiss() {
                        findViewByViewId(R.id.report_x_mark).setVisibility(View.GONE);
                    }
                });
        popwindow.showAsDropDown(v);
        findViewByViewId(R.id.report_x_mark).setVisibility(View.VISIBLE);
    }

    private void initCondition(String content) {
        if (!TextUtils.isEmpty(mUserId)) {
            mTvName.setText("姓名\n(" + content + ")");
        } else {
            mTvName.setText("姓名");
        }

        if (mPosition != -1) {
            mTvPosition.setText("职位\n(" + content + ")");
        } else {
            mTvPosition.setText("职位");
        }

        if (mDepartment != -1) {
            mTvDepartment.setText("部门\n(" + content + ")");
        } else {
            mTvDepartment.setText("部门");
        }
    }


    /**
     * 获取 人员列表
     */
    public void getPersonList() {
        //最后组装完整的and条件
        showLoadingDialog();
        BmobQuery<StateUser> query = new BmobQuery<>();
        query.order("-position");// 根据职位字段降序显示数据
        if (mStateUser.getPosition() == 0 || mStateUser.getPosition() == 1 || mStateUser.getPosition() == 2) {

        } else if (mStateUser.getPosition() == 3) {
            //只能看到自己部门的
            query.addWhereEqualTo("department", mStateUser.getDepartment());
        } else if (mStateUser.getPosition() == 5 || mStateUser.getPosition() == 4) {

        }
        query.addWhereEqualTo("company", mStateUser.getCompany());
        query.include("user,company");
        query.findObjects(new FindListener<StateUser>() {
            @Override
            public void done(List<StateUser> object, BmobException e) {
                dismissLoadingDialog();
                if (e == null) {
                    mUserList = object;
                    ulist = new ArrayList<>();
                    ulist.add("全部");
                    for (int i = 0; i < mUserList.size(); i++) {
                        ulist.add(mUserList.get(i).getUser().getRealname());
                    }
                    showPop(mTvName, ulist);
                    Timber.i("---" + new Gson().toJson(ulist));
                } else {
                    Timber.d("showError:" + e.toString());
                }
            }

        });
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
            query.addWhereEqualTo("userId", mStateUser.getObjectId());
        } else if (position == 3) {
            if (!TextUtils.isEmpty(mUserId)) {
                query.addWhereEqualTo("userId", mUserId);
            }
            if (mPosition != -1) {
                query.addWhereEqualTo("position", mPosition);
            }
            query.addWhereEqualTo("departmentId", mStateUser.getDepartment());
        } else if (position == 5 || position == 4) {
            if (!TextUtils.isEmpty(mUserId)) {
                query.addWhereEqualTo("userId", mUserId);
            }
            if (mPosition != -1) {
                query.addWhereEqualTo("position", mPosition);
            }
            if (mDepartment != -1) {
                query.addWhereEqualTo("departmentId", mDepartment);
            }
            Timber.i("mUserId:" + mUserId + "--mPosition:" + mPosition + "--mDepartment:" + mDepartment);
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
    private void queryAmountSumByMonth() {
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
            query.addWhereEqualTo("userId", mStateUser.getObjectId());
        } else if (position == 3) {
            if (!TextUtils.isEmpty(mUserId)) {
                query.addWhereEqualTo("userId", mUserId);
            }
            if (mPosition != -1) {
                query.addWhereEqualTo("position", mPosition);
            }
            query.addWhereEqualTo("departmentId", mStateUser.getDepartment());

        } else if (position == 5 || position == 4) {
            if (!TextUtils.isEmpty(mUserId)) {
                query.addWhereEqualTo("userId", mUserId);
            }
            if (mPosition != -1) {
                query.addWhereEqualTo("position", mPosition);
            }
            if (mDepartment != -1) {
                query.addWhereEqualTo("departmentId", mDepartment);
            }
            Timber.i("mUserId:" + mUserId + "--mPosition:" + mPosition + "--mDepartment:" + mDepartment);
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
            //获取屏幕宽度
            //计算控件居正中时距离左侧屏幕的距离
            int middleLeftPosition = (DisplayUtil.getWindowWidth(this) - mTvDepartmentR.getWidth()) / 2;
            //正中间位置需要向左偏移的距离
            int offset = mTvDepartmentR.getLeft() - middleLeftPosition;
            //让水平的滚动视图按照执行的x的偏移量进行移动
            Timber.i("offset:" + offset);
            mHorizontalScrollView.smoothScrollTo(offset, 0);
            return;
        }
        mDataList = list;
        queryAmountSumByMonth();
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
