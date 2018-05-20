package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;

import java.util.List;

/**
 * Created by yhx
 * data: 2018/5/13.
 */

public class ReportXAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProcessEntity> mDataLists;

    /**
     * 构造函数
     *
     * @param mContext
     * @param mDataLists
     */
    public ReportXAdapter(Context mContext, List<ProcessEntity> mDataLists) {
        this.mContext = mContext;
        this.mDataLists = mDataLists;
    }

    @Override
    public int getCount() {
        return mDataLists.size();
    }

    @Override
    public ProcessEntity getItem(int i) {
        return mDataLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ProcessEntity item = mDataLists.get(position);
        viewHolder vh;
        if (view == null) {
            vh = new viewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_report, null);
            vh.mTvID = view.findViewById(R.id.item_layout_report_process_id);
            vh.mTvCreateTime = view.findViewById(R.id.item_layout_report_create_time);
            vh.mTvName = view.findViewById(R.id.item_layout_report_name);
            vh.mTvPosition = view.findViewById(R.id.item_layout_report_position);
            vh.mTvDePartment = view.findViewById(R.id.item_layout_report_department);
            vh.mTvAmount = view.findViewById(R.id.item_layout_report_amount);
            vh.mTvFinishTime = view.findViewById(R.id.item_layout_report_finish_time);
            vh.mTvReason = view.findViewById(R.id.item_layout_report_reason);
            view.setTag(vh);
        } else {
            vh = (viewHolder) view.getTag();
        }
        vh.mTvID.setText(item.getObjectId());
        vh.mTvName.setText(item.getCreatorName());
        vh.mTvPosition.setText(SpManager.getInstance().mPositionData.get(item.getPosition()));
        String departmentTxt = "";
        if (item.getDepartmentId() == null) {
        } else if (item.getDepartmentId() == -1) {
            departmentTxt = "总经理";
        } else {
            departmentTxt = SpManager.getInstance().mBumenManager.get(item.getDepartmentId());
        }
        vh.mTvCreateTime.setText(item.getCreatedAt());
        vh.mTvDePartment.setText(departmentTxt);
        vh.mTvAmount.setText(NumberFormatterUtil.formatMoneyHideZero(item.getAmount() + ""));
        vh.mTvFinishTime.setText(item.getUpdatedAt());
        vh.mTvReason.setText(item.getReason());
        return view;  // 返回加载好了内容的行布局
    }

    public void addData(List<ProcessEntity> list) {
        mDataLists.addAll(list);
        notifyDataSetChanged();
    }

    public void setNewData(List<ProcessEntity> list) {
        mDataLists.clear();
        notifyDataSetChanged();
        if (list != null) {
            mDataLists.addAll(list);
            notifyDataSetChanged();
        }

    }

    class viewHolder {
        private TextView mTvID;
        private TextView mTvCreateTime;
        private TextView mTvName;
        private TextView mTvPosition;
        private TextView mTvDePartment;
        private TextView mTvAmount;
        private TextView mTvFinishTime;
        private TextView mTvReason;

    }
}
