package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.xiaoqiang.baoxiao.common.been.PointEntity;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 流程列表Controller
 */

public class ProcessListController extends BaseController<IProcessListView> {
    private int pageSize = 10;
    private Context mContext;

    public ProcessListController setmContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }
/**
 * 获取代办事宜
 * */
    public void getAgencyProcessList(int pageNo, int position, String companyId) {
        showLoadingDialog(mContext);
        BmobQuery<ProcessEntity> query = new BmobQuery<ProcessEntity>();
        if (position == 1 || position == 2) {
            BmobQuery<ProcessEntity> eq1 = new BmobQuery<>();
            eq1.addWhereContainedIn("precessType", Arrays.asList(new int[]{ FastConstant.PROCESS_TYPE_TWO, FastConstant.PROCESS_TYPE_THREE}));
            eq1.addWhereEqualTo("point", FastConstant.PROCESS_POINT_THREE);
            BmobQuery<ProcessEntity> eq2 = new BmobQuery<>();
            eq2.addWhereEqualTo("precessType", FastConstant.PROCESS_TYPE_ONE);
            eq2.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FOUR);
            List<BmobQuery<ProcessEntity>> queries = new ArrayList<BmobQuery<ProcessEntity>>();
            queries.add(eq1);
            queries.add(eq2);
            //或条件处理集合
            query.or(queries);
            //可处理所有流程类型 并且 节点point的流程

        } else if (position == 3 || position == 4) {
            if (position == 4) {
                query.addWhereContainedIn("precessType", Arrays.asList(new int[]{FastConstant.PROCESS_TYPE_ONE, FastConstant.PROCESS_TYPE_THREE}));
            } else {
                query.addWhereEqualTo("precessType", FastConstant.PROCESS_TYPE_ONE);
            }
            query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_TWO);
        } else if (position == 5) {
            //可处理流程类型为1 并且point为三级
            BmobQuery<ProcessEntity> query1 = new BmobQuery<ProcessEntity>();
            query1.addWhereEqualTo("precessType", FastConstant.PROCESS_TYPE_ONE);
            query1.addWhereEqualTo("point", FastConstant.PROCESS_POINT_THREE);
            //或者  流程为2 point为二级的
            BmobQuery<ProcessEntity> query2 = new BmobQuery<ProcessEntity>();
            query2.addWhereEqualTo("precessType", FastConstant.PROCESS_TYPE_TWO);
            query2.addWhereEqualTo("point", FastConstant.PROCESS_POINT_TWO);

            List<BmobQuery<ProcessEntity>> queries = new ArrayList<BmobQuery<ProcessEntity>>();
            queries.add(query1);
            queries.add(query2);
            //或条件处理集合
            query.or(queries);
        }
        query.addWhereEqualTo("companyId", companyId);
        query.addWhereEqualTo("isReject", false);
        query.setLimit(pageSize);
        query.setSkip(pageNo * pageSize);
        query.order("-createTime");// 根据createTime字段降序显示数据
        query.findObjects(new FindListener<ProcessEntity>() {
            @Override
            public void done(List<ProcessEntity> object, BmobException e) {
                dissmissLoadingDialog();
                getView().onShowList(object);
                if (e == null) {
                    Timber.i("ProcessEntity可處理个数：" + object.size());
                } else {
                    ToastUtil.show(e.getMessage());
                    Timber.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    public void getProcessList(int pageNo, int point, String userId, String companyId) {
        //  point当前节点 详细注解看fastConstans类
        showLoadingDialog(mContext);
        BmobQuery<ProcessEntity> query = new BmobQuery<ProcessEntity>();
        if (point != -1) {
            //条件1 需要根据节点 point 查询
            query.addWhereEqualTo("point", point);//
        }
        if (!TextUtils.isEmpty(userId)) {
            //条件2 需要根据userId 查询
            query.addWhereEqualTo("userId", userId);//
        }
        query.addWhereEqualTo("companyId", companyId);//
        query.setLimit(pageSize);// 限制最多pageSize条数据结果作为一页
        query.setSkip(pageSize * pageNo);//忽略条目
        query.order("-createTime");// 根据createTime字段降序显示数据
        query.findObjects(new FindListener<ProcessEntity>() {

            @Override
            public void done(List<ProcessEntity> object, BmobException e) {
                dissmissLoadingDialog();
                getView().onShowList(object);
                if (e == null) {

                } else {
                    ToastUtil.show(e.getMessage());
//                    getView().showError(e.getMessage().toString());
                    Timber.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
    }

    /**
     * 批准请求
     */
    public void approvalProcess(ProcessEntity processEntity, final int position) {
        showLoadingDialog(mContext);
        StateUser user = SpManager.getInstance().getUserInfo();
        List<PointEntity> plist = processEntity.getPointList();
        int endPosition = plist.size() - 1;
        plist.get(endPosition).setCreateTime(System.currentTimeMillis());
        plist.get(endPosition).setCreatorName(user.getUser().getNickName());
        plist.get(endPosition).setUserId(user.getObjectId());
        plist.get(endPosition).setCreatorHeadImg(user.getUser().getPhotoPath());
        plist.get(endPosition).setPointStatus(FastConstant.POINT_STATUS_TWO);

        int nextPoint = SpManager.getInstance().getNextPoint(processEntity.getPoint(), processEntity.getProcessType());
        processEntity.setPoint(nextPoint);

        PointEntity pointE1 = new PointEntity();
        pointE1.setPoint(nextPoint);
        plist.add(pointE1);
        processEntity.setPointList(plist);//转换string保存

        processEntity.update(processEntity.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    getView().operateSuccess(position);
//                    ToastUtil.show("");
                    ToastUtil.show("成功批准");
                    Log.i("bmob", "成功批准");
                } else {
                    ToastUtil.show(e.getMessage());
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 驳回请求
     */
    public void rejectProcces(ProcessEntity processEntity, String rejectRemark, final int position) {
        showLoadingDialog(mContext);
        StateUser user = SpManager.getInstance().getUserInfo();
        List<PointEntity> plist = processEntity.getPointList();
        int endPosition = plist.size() - 1;
        plist.get(endPosition).setCreateTime(System.currentTimeMillis());
        plist.get(endPosition).setCreatorName(user.getUser().getNickName());
        plist.get(endPosition).setUserId(user.getObjectId());
        plist.get(endPosition).setCreatorHeadImg(user.getUser().getPhotoPath());
        plist.get(endPosition).setPointStatus(FastConstant.POINT_STATUS_TWO);
        plist.get(endPosition).setRemark(rejectRemark);

        PointEntity pointE1 = new PointEntity();
        pointE1.setPoint(FastConstant.PROCESS_POINT_ONE);//回到创建人那里
        plist.add(pointE1);
        processEntity.setPointList(plist);//转换string去保存

        processEntity.setReject(true);
        processEntity.update(processEntity.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    getView().operateSuccess(position);
                    ToastUtil.show("成功驳回");
                    Timber.i("bmob", "成功驳回");
                } else {
                    ToastUtil.show(e.getMessage());
                    Timber.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
