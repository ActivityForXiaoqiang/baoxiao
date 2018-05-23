package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.PointEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.timeline.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yhx
 * data: 2018/5/5.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<PointEntity> plist;
    private Context mContext;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private Integer currentPoint;
    private Integer processType;

    public TimeLineAdapter(List<PointEntity> plist, boolean withLinePadding, Integer currentPoint, Integer processType) {
       /* //反序
        for (int i = plist.size() - 1; i >= 0; i--) {
            this.plist = new ArrayList<>();
            this.plist.add(plist.get(i));
        }*/
        this.plist = plist;
        mWithLinePadding = withLinePadding;
        this.currentPoint = currentPoint;
        this.processType = processType;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;
        view = mLayoutInflater.inflate(R.layout.item_layout_point_time_line, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        PointEntity item = plist.get(position);
        GlideManager.loadCircleImg(item.getCreatorHeadImg(), holder.mTimelineView, R.drawable.ic_marker);
//        holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.marker), ContextCompat.getColor(mContext, R.color
// .colorPrimary));
        holder.name.setText(item.getCreatorName());
        String statusInfo = "";
        if (item.getPoint() == FastConstant.PROCESS_POINT_ONE) {
            if (position == 0) {

            }
        }
        if (item.getPointStatus() == FastConstant.POINT_STATUS_TWO) {
            //已经操作了
            if (item.getPoint() == FastConstant.PROCESS_POINT_ONE) {
                //节点在自己
                if (position == 0) {
                    statusInfo = "发起申请";
                } else {
                    statusInfo = "提交至退回节点";
                }
            } else {
                //节点在上级

                if (TextUtils.isEmpty(item.getRemark())) {
                    //同意
                    statusInfo = "已同意";
                    if (currentPoint == FastConstant.PROCESS_POINT_FINISH && (position == plist.size() - 1)) {
                        statusInfo = "归档";
                    }
                } else {
                    //不同意  驳回
                    statusInfo = "已退回";
                }
            }
        } else if (item.getPointStatus() == FastConstant.POINT_STATUS_ONE) {
            //未操作
            if (item.getPoint() == FastConstant.PROCESS_POINT_ONE) {
                //节点在自己
                if (position != 0) {
                    statusInfo = "等待修改";
                }
            } else {
                //节点在上级
//                statusInfo = "等待审批";
                statusInfo = SpManager.getInstance().getPointInfo(item.getPoint(), processType);
                if (TextUtils.equals("部门主管", statusInfo)) {
                    statusInfo = "等待主管审批";
                } else if (TextUtils.equals("财务主管", statusInfo)) {
                    statusInfo = "等待财务主管审批";
                } else if (TextUtils.equals("总经理", statusInfo)) {
                    statusInfo = "等待总经理审批";
                } else if (TextUtils.equals("财务结算", statusInfo)) {
                    statusInfo = "等待财务结算";
                } else if (TextUtils.equals("申请人", statusInfo)) {
                    statusInfo = "等待申请人确认收款";
                }
            }

        }
        holder.status.setText(statusInfo);

        if (item.getCreateTime() != null) {
            holder.time.setText(TimeFormatUtil.formatTime(item.getCreateTime(), FastConstant.TIME_FORMAT_TYPE));
        }
    }

    private void getStatusInfo() {

    }

    @Override
    public int getItemCount() {
        return (plist != null ? plist.size() : 0);
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_layout_point_time_line_name)
        TextView name;
        @BindView(R.id.item_layout_point_time_line_status)
        TextView status;
        @BindView(R.id.item_layout_point_time_line_time)
        TextView time;
        @BindView(R.id.item_layout_point_time_line_marker)
        TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);
        }
    }

}
