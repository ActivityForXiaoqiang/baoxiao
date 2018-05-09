package com.example.xiaoqiang.baoxiao.common.ui.report;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.DayTime;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class ReportActivity extends MyBaseActivity {
    BarChart barChart;

    @Override
    public Integer getViewId() {
        return R.layout.activity_report;
    }

    @Override
    public void init() {
        barChart = findViewById(R.id.barChart);
        barChart.setNoDataText("没有数据");
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisLineWidth(1f);
        xAxis.setLabelCount(6);
        xAxis.setAxisMinValue(0);
        xAxis.setAxisMaxValue(31);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMinValue(0);

        queryProcessAmountSumBymonth();

    }

    List<IBarDataSet> dataSets = new ArrayList<>();

    private void test() {
        Timber.i("test");
        for (int i = 0; i < valueSet1.size(); i++) {
            Timber.i("x===:" + valueSet1.get(i).getX() + "    y:" + valueSet1.get(i).getY());
        }
       /* valueSet1 = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            float value=0;
            if(i==5){
                 value = (float) (Math.random() * 12*//*100以内的随机数*//*);
            }
            valueSet1.add(new BarEntry(value, i));
        }*/

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "目标");
        barDataSet1.setColor(Color.parseColor("#45a2ff"));

        barDataSet1.setBarShadowColor(Color.parseColor("#01000000"));
        dataSets.clear();
        dataSets.add(barDataSet1);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        barChart.setData(data);
        barChart.scrollTo(0, 10);
    }


    List<BarEntry> valueSet1;

    private List<DayTime> dayTimes;
    private int index = 0;
    private String companyId;

    /**
     * 获取一个月的报销总额度
     */
    public void queryProcessAmountSumBymonth() {
        //获取上个月
//        List<DayTime> dlist = TimeFormatUtil.getMonthTime(TimeFormatUtil.getNextMonth(new Date()));
        //获取当前月
        List<DayTime> dlist = TimeFormatUtil.getMonthTime(new Date());
        Timber.i(new Gson().toJson(dlist));
        companyId = SpManager.getInstance().getUserInfo().getCompany().getObjectId();
        this.dayTimes = dlist;
        valueSet1 = new ArrayList<>();
        Timber.i(new Gson().toJson(dayTimes));
        showLoadingDialog();
        index = 0;
        queryProcessAmountSumByDay(dayTimes.get(index));
    }

    private void queryProcessAmountSumByDay(DayTime dayTime) {
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
        query.addWhereEqualTo("companyId", companyId);
        query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FINISH);
        query.sum(new String[]{"amount"});
        query.findStatistics(ProcessEntity.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Message message = new Message();
                    if (ary != null) {//
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            double sum = obj.getDouble("_sumAmount");//_(关键字)+首字母大写的列名

                            message.what = 0;
                            message.obj = NumberFormatterUtil.DoubleToFloat(sum);
                            mHandler.sendMessage(message);
//                            valueSet1.add(new BarEntry(index + 1, (float) sum));
//                            Timber.i("報銷总額：" + NumberFormatterUtil.formatDouble(sum));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        message.what = 1;
                        message.obj = Float.valueOf("0");
                        mHandler.sendMessage(message);
                        //没有记录
//                        valueSet1.add(new BarEntry(index + 1, 0));
//                        ToastUtil.show("查询成功，无数据");
                    }

                  /*  index++;
                    if (valueSet1.size() >= dayTimes.size()) {
                        test();
                        //返回数据
                        dissmissLoadingDialog();
                    } else {
                        queryProcessAmountSumByDay(dayTimes.get(index));
                    }*/

                } else {
                    ToastUtil.show(e.getMessage());
                    dissmissLoadingDialog();
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            index++;
            switch (msg.what) {
                case 0:
                    float sum = (float) msg.obj;
                    valueSet1.add(new BarEntry(index, (float) sum));
                    break;
                case 1:
                    //没有记录
                    valueSet1.add(new BarEntry(index, 0));
                    break;
            }

            if (valueSet1.size() >= dayTimes.size()) {
                test();
                //返回数据
                dissmissLoadingDialog();
            } else {
                queryProcessAmountSumByDay(dayTimes.get(index));
            }
        }
    };
    private LoadingDialog loadingDialog;

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dissmissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
