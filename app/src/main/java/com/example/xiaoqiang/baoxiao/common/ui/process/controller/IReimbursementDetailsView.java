package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.IBaseView;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 申请流程view
 */

public interface IReimbursementDetailsView extends IBaseView {

    void onShowProcess(ProcessEntity ProcessEntity);
}
