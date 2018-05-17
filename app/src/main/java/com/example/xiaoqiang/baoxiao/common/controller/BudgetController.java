package com.example.xiaoqiang.baoxiao.common.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.xiaoqiang.baoxiao.common.been.BudgetEntity;
import com.example.xiaoqiang.baoxiao.common.been.DayTime;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.view.IBudgetListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yhx
 * data: 2018/5/9.
 * 预算Controller
 */

public class BudgetController extends BaseController<IBudgetListView> {
    private Context mContext;
    private int pageSize = 10;

    public BudgetController setmContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    private List<BudgetEntity> blist;
    private int index = 0;

    /**
     * 获取预算
     */
    public void queryBudgetList(int pageNo) {
        String companyId = SpManager.getInstance().getUserInfo().getCompany().getObjectId();
        //  point当前节点 详细注解看fastConstans类
        showLoadingDialog(mContext);
        BmobQuery<BudgetEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("companyId", companyId);//
        query.setLimit(pageSize);// 限制最多pageSize条数据结果作为一页
        query.setSkip(pageSize * pageNo);//忽略条目
        query.order("-monthTime");// 根据createTime字段降序显示数据
        query.findObjects(new FindListener<BudgetEntity>() {

            @Override
            public void done(List<BudgetEntity> object, BmobException e) {

                if (e == null) {
                    if (object.size() == 0) {
                        dissmissLoadingDialog();
                        getView().onShowList(object);
                    } else {
                        queryProcessAmountSumBymonth(object);
                    }
                    Timber.i("bmob" + "成功：" + new Gson().toJson(object));
                } else {
                    ToastUtil.show(e.getMessage());
//                    getView().showError(e.getMessage().toString());
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }


    /**
     * 获取一个月的报销总额度
     */
    public void queryProcessAmountSumBymonth(List<BudgetEntity> blist) {
        this.blist = blist;
        //获取当前月
        index = 0;
        queryBudgetAmountSumByMonth(blist.get(index).getMonthTime(), blist.get(index).getDepartment());
    }

    /**
     * 查询一个月的报销额
     */
    public void queryBudgetAmountSumByMonth(long year_month, int departmentId) {
        //获取当前月
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date(year_month));
        DayTime dayTime = TimeFormatUtil.getMonthTimeOfMonth(ca);
        Timber.i(new Gson().toJson(dayTime));
        //--and条件1
        BmobQuery<ProcessEntity> eq1 = new BmobQuery<>();
        eq1.addWhereLessThanOrEqualTo("updatedAt", dayTime.getMaxDate());//時間<=MaxTime
        //--and条件2
        BmobQuery<ProcessEntity> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("updatedAt", dayTime.getMinDate());//時間>=MinTime
        List<BmobQuery<ProcessEntity>> andQuery = new ArrayList<>();
        andQuery.add(eq1);
        andQuery.add(eq2);

        BmobQuery<ProcessEntity> query = new BmobQuery<>();
        query.and(andQuery);
        query.addWhereEqualTo("departmentId", departmentId);
        query.addWhereEqualTo("companyId", SpManager.getInstance().getUserInfo().getCompany().getObjectId());
        query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FINISH);
        query.sum(new String[]{"amount"});
        query.findStatistics(ProcessEntity.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Message message = new Message();
                    if (ary != null) {//
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            double sum = obj.getDouble("_sumAmount");//_(关键字)+首字母大写的列名

                            message.what = 0;
                            message.obj = sum;
                            mHandler.sendMessage(message);
//                            valueSet1.add(new BarEntry(index + 1, (float) sum));
                            Timber.i("報銷总額：" + NumberFormatterUtil.formatDouble(sum));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        message.what = 1;
                        message.obj = Double.valueOf("0");
                        mHandler.sendMessage(message);
                        //没有记录
//                        ToastUtil.show("查询成功，无数据");
                    }
                } else {
                    ToastUtil.show(e.getMessage());
                    dissmissLoadingDialog();
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }

        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    blist.get(index).setSumAmountProcess((Double) msg.obj);
                    break;
                case 1:
                    //没有记录
                    blist.get(index).setSumAmountProcess(0.0);
                    break;
            }
            index++;
            if (index >= blist.size()) {
                getView().onShowList(blist);
                //返回数据
                dissmissLoadingDialog();
            } else {
                queryBudgetAmountSumByMonth(blist.get(index).getMonthTime(), blist.get(index).getDepartment());
            }
        }
    };

    /**
     * 保存流程
     */
    public void saveBudget(BudgetEntity be) {
        showLoadingDialog(mContext);
        be.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    ToastUtil.show("提交成功");
                    getView().onRequestCompleted();
                } else {
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }
        });
    }


    /**
     * 修改
     */
    public void modifyBudget(BudgetEntity budgetEntity) {
        showLoadingDialog(mContext);
        budgetEntity.update(budgetEntity.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    getView().onRequestCompleted();
//                    ToastUtil.show("");
                    ToastUtil.show("预算修改成功");
                    Timber.i("bmob" + "预算修改成功");
                } else {
                    ToastUtil.show(e.getMessage());
                    Timber.i("bmob" + "失败：" + e.getMessage() + "+" + e.getErrorCode());
                }
            }
        });
    }
}
