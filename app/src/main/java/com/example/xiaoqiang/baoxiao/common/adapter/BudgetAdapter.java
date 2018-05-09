package com.example.xiaoqiang.baoxiao.common.adapter;

import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.been.BudgetEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.helper.RadiusViewHelper;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;

/**
 * Created:2018/5/9
 * Desc:預算适配器
 */
public class BudgetAdapter extends BaseQuickAdapter<BudgetEntity, BaseViewHolder> {

    public BudgetAdapter() {
        super(R.layout.item_layout_budget);
    }

    @Override
    protected void convert(BaseViewHolder helper, BudgetEntity item) {
        helper.addOnClickListener(R.id.item_layout_budget_modify);
        double budgetAmount;
        if (item.getBudgetAmount() == null) {
            budgetAmount = 0;
        } else {
            budgetAmount = item.getBudgetAmount();
        }
        helper.setText(R.id.item_layout_budget_amount, NumberFormatterUtil.formatMoneyHideZero(budgetAmount + ""));

        double sumAmountReimbursement;
        if (item.getSumAmountProcess() == null) {
            sumAmountReimbursement = 0;
        } else {
            sumAmountReimbursement = item.getSumAmountProcess();
        }

        //
        String percentageStr = "";
        int percentageNum;
        if (budgetAmount > 0) {
            percentageNum = (int) (NumberFormatterUtil.getPercentageNum(sumAmountReimbursement, budgetAmount, 0));
            percentageStr = "\n已占预算(" + percentageNum + "%)";
        }
        helper.setText(R.id.item_layout_budget_reimbursement_amount, NumberFormatterUtil.formatMoneyHideZero(sumAmountReimbursement + "") + percentageStr);

        helper.setText(R.id.item_layout_budget_cordon, "预警值：" + item.getCordon() + "%");

        helper.setText(R.id.item_layout_budget_time, TimeFormatUtil.formatTime(item.getMonthTime(), "yyyy年MM月") + "");
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }
}
