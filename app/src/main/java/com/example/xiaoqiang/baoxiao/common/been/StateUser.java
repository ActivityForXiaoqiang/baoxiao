package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.BmobObject;

public class StateUser extends BmobObject {
    private MyUser user;
    private Company company;
    private boolean isJoinCompay;
    private boolean isAppying;
    private int position; //职位

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isJoinCompay() {
        return isJoinCompay;
    }

    public void setJoinCompay(boolean joinCompay) {
        isJoinCompay = joinCompay;
    }

    public boolean isAppying() {
        return isAppying;
    }

    public void setAppying(boolean appying) {
        isAppying = appying;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
