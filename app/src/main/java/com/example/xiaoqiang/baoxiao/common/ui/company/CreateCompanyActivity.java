package com.example.xiaoqiang.baoxiao.common.ui.company;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.controller.SaveController;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CreateCompanyActivity extends MyBaseActivity implements SaveView {

    EditText name, miaoshu;
    Button create;
    MyUser current;

    SaveController controller;
    UpdataController updataController;
    QueryController querycontroller;

    String stateId;

    @Override
    public Integer getViewId() {
        return R.layout.activity_add_company;
    }

    @Override
    public void init() {
        stateId = getIntent().getStringExtra("objId");
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

                queryCompay(current);
            }

            @Override
            public void onUpdataStateUserSuccess() {
                ToastUtil.show("创建成功");
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

    void queryCompay(MyUser user) {
        BmobQuery<Company> query = new BmobQuery<>();
        query.addWhereEqualTo("creator", user);
        query.findObjects(new FindListener<Company>() {
            @Override
            public void done(List<Company> list, BmobException e) {
                StateUser stateUser = new StateUser();
                stateUser.setJoinCompay(true);
                stateUser.setCompany(list.get(0));
                updataController.updataStateUser(stateUser, stateId);
            }
        });
    }
}
