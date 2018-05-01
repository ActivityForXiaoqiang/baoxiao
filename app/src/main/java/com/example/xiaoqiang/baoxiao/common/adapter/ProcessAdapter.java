package com.example.xiaoqiang.baoxiao.common.adapter;

import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.RadiusViewHelper;

/**
 * Created:2018/4/27
 * Desc:流程适配器
 */
public class ProcessAdapter extends BaseQuickAdapter<ProcessEntity, BaseViewHolder> {

    public ProcessAdapter() {
        super(R.layout.item_process);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcessEntity item) {
        helper.setText(R.id.process_title_tv, "")
                .setText(R.id.process_founder_tv, "")
                .setText(R.id.process_accept_time_tv, "")
                .setText(R.id.process_point_tv,   "")
                .setText(R.id.process_founder_amount_tv,  "");
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
