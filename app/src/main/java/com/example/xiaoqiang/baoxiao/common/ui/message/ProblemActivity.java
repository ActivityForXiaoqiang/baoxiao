package com.example.xiaoqiang.baoxiao.common.ui.message;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.Problem;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ProblemActivity extends MyBaseActivity {

    EditText title, content;
    Button btn;

    @Override
    public Integer getViewId() {
        return R.layout.activity_problem;
    }

    @Override
    public void init() {
        title = findViewById(R.id.problem_title);
        content = findViewById(R.id.problem_content);
        btn = findViewById(R.id.btn_sure);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    void upload() {
        loadingDialog.show();
        if (TextUtils.isEmpty(title.getText().toString())) {
            ToastUtil.show("没写标题哦");
            return;
        }
        if (TextUtils.isEmpty(content.getText().toString())) {
            ToastUtil.show("没写内容哦");
            return;
        }
        Problem pro = new Problem();
        pro.setUser(BmobUser.getCurrentUser(MyUser.class));
        pro.setContent(content.getText().toString());
        pro.setTitle(title.getText().toString());
        pro.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    loadingDialog.hide();
                    ToastUtil.show("已提交");
                    finish();
                }
            }
        });
    }


}
