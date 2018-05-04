package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String nickName;
    private String photoPath;
    private int position; //职位id
    private boolean sex;
    private boolean isSuper;
    private boolean isJoinCompany;
    private boolean isApplying;
    private String companyId;//所属公司

    public boolean isApplying() {
        return isApplying;
    }

    public void setApplying(boolean applying) {
        isApplying = applying;
    }

    public boolean isJoinCompany() {
        return isJoinCompany;
    }

    public void setJoinCompany(boolean joinCompany) {
        isJoinCompany = joinCompany;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
