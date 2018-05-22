package com.example.xiaoqiang.baoxiao.common.been;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by yhx
 * data: 2018/4/30.
 */

public class ProcessEntity extends BmobObject {
    private String creatorName;//创建人姓名 M
    private Integer position;//创建人職位 M
    private String account;//账号
    private String accountType;//账号类型 M
    private Double amount;//金额
    private String reason;//原因
    private Long startTime;
    private Long endTime;
    private String userId;//创建人id
    private String companyId;//创建人所属公司
    private String setout;//出发地
    private String destination;//目的地
    private String vehicle;//交通工具
    private List<String> imgs;//附件
    private Integer point;//当前节点 相關節點信息注釋前往FastConstant類中查看
    private Integer processType;//流程类型 和流程节点 一起判断  1普通員工申請  2主管申請  3總經理申請
    private String pointList;//節點时间轴
    private Boolean isReject = false;//是否被驳回
    private Integer departmentId;//部门id
    private boolean isTravel = false;//是否差旅
    private List<String> userIdlist;//经手的用户id

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSetout() {
        return setout;
    }

    public void setSetout(String setout) {
        this.setout = setout;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public List<PointEntity> getPointList() {
        if (TextUtils.isEmpty(pointList)) {
            return null;
        }
        return new Gson().fromJson(pointList, new TypeToken<List<PointEntity>>() {
        }.getType());
    }

    public void setPointList(List<PointEntity> pointList) {
        if (pointList == null) {
            return;
        }
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (pointList.get(i).getUserId() != null) {
                arr.add(pointList.get(i).getUserId());
            }
        }
        setUserIdlist(arr);
        this.pointList = new Gson().toJson(pointList);
    }

    public Boolean getReject() {
        return isReject;
    }

    public void setReject(Boolean reject) {
        isReject = reject;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }


    public boolean isTravel() {
        return isTravel;
    }

    public void setTravel(boolean travel) {
        isTravel = travel;
    }

    public List<String> getUserIdlist() {
        return userIdlist;
    }

    public void setUserIdlist(List<String> userIdlist) {
        this.userIdlist = userIdlist;
    }
}
