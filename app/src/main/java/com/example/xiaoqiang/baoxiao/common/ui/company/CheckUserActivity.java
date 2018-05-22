package com.example.xiaoqiang.baoxiao.common.ui.company;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckUserActivity extends MyBaseActivity {

    TextView[] tvs = new TextView[6];
    int[] tv_ids = {R.id.check_nickname, R.id.check_sex, R.id.check_realname, R.id.check_code, R.id.check_birthday, R.id.check_phone};

    ImageView headBg;
    CircleImageView head;

    String json;
    MyUser user;

    @Override
    public Integer getViewId() {
        return R.layout.activity_check;
    }

    @Override
    public void init() {
        hideToolbar();
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = findViewById(tv_ids[i]);
        }

        json = getIntent().getStringExtra("user");
        user = new Gson().fromJson(json, MyUser.class);
        if (!TextUtils.isEmpty(user.getPhotoPath())) {
            Glide.with(this).load(user.getPhotoPath()).into(headBg);
            Glide.with(this).load(user.getPhotoPath()).into(head);
        }

        tvs[0].setText("昵称：" + user.getNickName());
        if (user.isSex()) {
            tvs[1].setText("性别：男");
        } else {
            tvs[1].setText("性别：女");
        }

        tvs[2].setText("真实姓名：" + user.getRealname());

        if (!TextUtils.isEmpty(user.getCode())) {
            tvs[3].setText("证件号码 :" + user.getCode());
        }
        if (!TextUtils.isEmpty(user.getBirthday())) {
            tvs[4].setText("出生日期 :" + user.getBirthday());
        }
        if (!TextUtils.isEmpty(user.getPhotoPath())) {
            tvs[4].setText("手机号码 :" + user.getPhotoPath());
        }
    }
}
