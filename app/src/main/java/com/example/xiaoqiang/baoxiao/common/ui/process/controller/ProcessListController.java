package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import android.text.TextUtils;

import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 流程列表Controller
 */

public class ProcessListController extends BaseController<IProcessListView> {
    private int pageSize = 10;

    public void queryProcessList(int index, int status, String userId) {
        //最后组装完整的and条件
        List<BmobQuery<ProcessEntity>> andQuerys = new ArrayList<BmobQuery<ProcessEntity>>();
        if (status != -1) {
            //条件1 需要根据status 查询
            BmobQuery<ProcessEntity> eq1 = new BmobQuery<ProcessEntity>();
            eq1.addWhereEqualTo("status", status);//
            andQuerys.add(eq1);
        }
        if (!TextUtils.isEmpty(userId)) {
            //条件2 需要根据userId 查询
            BmobQuery<ProcessEntity> eq2 = new BmobQuery<ProcessEntity>();
            eq2.addWhereEqualTo("userId", userId);//
            andQuerys.add(eq2);
        }

        BmobQuery<ProcessEntity> query = new BmobQuery<ProcessEntity>();
        query.and(andQuerys);
        query.setLimit(pageSize);// 限制最多pageSize条数据结果作为一页
        query.setSkip(pageSize * index);//忽略条目
        query.order("-createTime");// 根据createTime字段降序显示数据
        // status== status
        query.addWhereEqualTo("status", status);
        query.findObjects(new FindListener<ProcessEntity>() {

            @Override
            public void done(List<ProcessEntity> object, BmobException e) {
                if (e == null) {
                } else {
                    getView().onSuccess(object);
                }
            }

        });
    }

}
