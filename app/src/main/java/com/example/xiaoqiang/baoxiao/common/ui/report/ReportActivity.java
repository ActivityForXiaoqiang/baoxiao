package com.example.xiaoqiang.baoxiao.common.ui.report;

import android.graphics.Color;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

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

        test();
    }


    void test() {
        List<IBarDataSet> dataSets = null;

        List<BarEntry> valueSet1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float value = (float) (Math.random() * 12/*100以内的随机数*/);
            valueSet1.add(new BarEntry(value, i));
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "目标");
        barDataSet1.setColor(Color.parseColor("#45a2ff"));

        barDataSet1.setBarShadowColor(Color.parseColor("#01000000"));
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        barChart.setData(data);

    }


}
