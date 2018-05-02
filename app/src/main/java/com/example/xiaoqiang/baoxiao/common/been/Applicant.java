package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.BmobObject;

public class Applicant extends BmobObject {
    private String companyId;
    private MyUser user;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
