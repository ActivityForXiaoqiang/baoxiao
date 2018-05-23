package com.example.xiaoqiang.baoxiao.common.been;

import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.google.gson.Gson;

/**
 * author : yhx
 * time   : 2018/5/4
 */

public class PointEntity {
    private String userId;
    private String dealUser;
    private String creatorName;
    private String creatorHeadImg;
    private String remark;//备注
    private Integer pointStatus = FastConstant.POINT_STATUS_ONE;//1未操作 2已操作
    private Long createTime;
    private Integer point;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorHeadImg() {
        return creatorHeadImg;
    }

    public void setCreatorHeadImg(String creatorHeadImg) {
        this.creatorHeadImg = creatorHeadImg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPointStatus() {
        return pointStatus;
    }

    public void setPointStatus(Integer pointStatus) {
        this.pointStatus = pointStatus;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public StateUser getDealUser() {
        if (dealUser == null) {
            return null;
        }
        return new Gson().fromJson(dealUser, StateUser.class);
    }

    public void setDealUser(StateUser dealUser) {
        if (dealUser == null) {
            return;
        }
        this.dealUser = new Gson().toJson(dealUser);
    }
}
