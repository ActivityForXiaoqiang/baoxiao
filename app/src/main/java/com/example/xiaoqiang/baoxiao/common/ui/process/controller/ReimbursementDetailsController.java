package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import android.content.Context;

import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.google.gson.Gson;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by yhx
 * data: 2018/5/5.
 * 流程详情Controller
 */

public class ReimbursementDetailsController extends BaseController<IReimbursementDetailsView> {
    private Context mContext;


    public ReimbursementDetailsController setmContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }


    /**
     * 获取流程详情
     */
    public void queryProcessDetails(String processId) {
        showLoadingDialog(mContext);
        BmobQuery<ProcessEntity> query = new BmobQuery<>();
        query.getObject(processId, new QueryListener<ProcessEntity>() {

            @Override
            public void done(ProcessEntity object, BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    getView().onShowProcess(object);
                    Timber.i("bmob" + new Gson().toJson(object));
                } else {
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }


}
