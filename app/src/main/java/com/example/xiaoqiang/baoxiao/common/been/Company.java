package com.example.xiaoqiang.baoxiao.common.been;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Company extends BmobObject {

    private String creator;
    private String name;
    private String describe;



    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
