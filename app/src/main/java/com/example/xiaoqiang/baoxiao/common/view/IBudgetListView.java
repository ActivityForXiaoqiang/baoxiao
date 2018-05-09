package com.example.xiaoqiang.baoxiao.common.view;

import com.example.xiaoqiang.baoxiao.common.been.BudgetEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.IBaseView;

import java.util.List;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 预算列表view
 */

public interface IBudgetListView extends IBaseView {
    void onShowList(List<BudgetEntity> list);
}
