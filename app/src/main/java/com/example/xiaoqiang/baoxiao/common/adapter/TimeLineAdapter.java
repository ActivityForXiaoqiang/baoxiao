package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.PointEntity;
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

    public TimeLineAdapter(List<PointEntity> plist, boolean withLinePadding) {
        this.plist = plist;
        mWithLinePadding = withLinePadding;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;
        view = mLayoutInflater.inflate(R.layout.item_layout_point_time_line , parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        PointEntity item =plist.get(position);
        PointEntity timeLineModel = plist.get(position);
        holder.mTimelineView.setImageResource(R.drawable.marker);
//        holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
//        holder.name.setText( SpManager.getInstance().getUserInfo().getUser().getNickName());
//        holder.status.setText( "");
//        holder.time.setText( TimeFormatUtil.formatTime(item.getCreateTime(), FastConstant.TIME_FORMAT_TYPE));
    }

    @Override
    public int getItemCount() {
        return (plist!=null? plist.size():0);
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
