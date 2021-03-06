package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.IBaseView;

import java.util.List;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 流程列表view
 */

public interface IProcessListView extends IBaseView {
    void onShowList(List<ProcessEntity> list);

    void operateSuccess(int position);
}
