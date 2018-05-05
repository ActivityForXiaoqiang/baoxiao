package com.example.xiaoqiang.baoxiao.common.adapter;

import android.support.v4.content.ContextCompat;

import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.RadiusViewHelper;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpanUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;

/**
 * Created:2018/4/27
 * Desc:流程适配器
 */
public class ProcessAdapter extends BaseQuickAdapter<ProcessEntity, BaseViewHolder> {
    private boolean isOperate = false;//是否可操作

    public ProcessAdapter(boolean isOperate) {
        super(R.layout.item_process);
        this.isOperate = isOperate;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcessEntity item) {
        if (isOperate) {
            helper.setVisible(R.id.item_process_operate_l, true);
        }
        String pointInfo;
        if (item.getPoint() == FastConstant.PROCESS_POINT_ONE) {
            pointInfo = SpManager.getInstance().getUserInfo().getUser().getNickName() + "(我)";
        } else {
            pointInfo = SpManager.getInstance().getPointInfo(item.getPoint(), item.getProcessType());

        }

        CharSequence titleCs = new SpanUtil().append("申请事由：")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.txt_black1))
                .append(item.getReason()).create();
        helper.addOnClickListener(R.id.item_process_reject_tv)
                .addOnClickListener(R.id.item_process_approval_tv);
        helper.setText(R.id.process_title_tv, titleCs)
                .setText(R.id.process_founder_tv, SpManager.getInstance().getUserInfo().getUser().getNickName())
                .setText(R.id.process_accept_time_tv, TimeFormatUtil.formatTime(item.getCreateTime(), FastConstant.TIME_FORMAT_TYPE))
                .setText(R.id.process_point_tv, pointInfo)
                .setText(R.id.process_point_status_tv, item.getPointList().get(item.getPointList().size() - 1).getPointStatus() == 1 ? "未操作" : "已处理")
                .setText(R.id.process_apply_amount_tv, NumberFormatterUtil.formatMoneyWithSelfValue(item.getAmount() + ""));
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
