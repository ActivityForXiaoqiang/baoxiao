package com.example.xiaoqiang.baoxiao.common.ui.company;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.controller.SaveController;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;

import cn.bmob.v3.BmobUser;

public class CreateCompanyActivity extends MyBaseActivity implements SaveView {

    EditText name, miaoshu;
    Button create;
    MyUser current;

    SaveController controller;
    UpdataController updataController;
    QueryController querycontroller;

    @Override
    public Integer getViewId() {
        return R.layout.activity_add_company;
    }

    @Override
    public void init() {
        controller = new SaveController(this);
        name = findViewById(R.id.company_n);
        miaoshu = findViewById(R.id.company_d);
        create = findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        updataController = new UpdataController(new UpdataView() {
            @Override
            public void onUpdataUserSuccess() {
                finish();
            }

            @Override
            public void showDialog() {
                loadingDialog.show();
            }

            @Override
            public void hideDialog() {
                loadingDialog.hide();
            }

            @Override
            public void showError(Throwable throwable) {

            }
        });
    }

    void create() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(CreateCompanyActivity.this, "公司名称未录入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(miaoshu.getText().toString())) {
            Toast.makeText(CreateCompanyActivity.this, "公司描述未录入", Toast.LENGTH_SHORT).show();
            return;
        }

        controller.createCompany(name.getText().toString(), miaoshu.getText().toString());
    }

    @Override
    public void onCompanyCreateSuccess(String result) {
        current = BmobUser.getCurrentUser(MyUser.class);
        current.setJoinCompany(true);
        current.setSuper(true);

        updataController.updataUser(current);
    }

    @Override
    public void onRequestCreateSuccess(String result) {

    }

    @Override
    public void onStateUserCreateSuccess(String result) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(Throwable throwable) {

    }
}
