package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.BmobObject;

public class Problem extends BmobObject {
    private MyUser user;
    private String title;
    private String content;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
