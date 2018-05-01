package com.example.xiaoqiang.baoxiao.common.ui.sign;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xiaoqiang.baoxiao.MainActivity;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.LoginController;
import com.example.xiaoqiang.baoxiao.common.view.LoginView;

import cn.bmob.v3.BmobUser;

public class LoginActivity extends MyBaseActivity implements LoginView {

    private FloatingActionButton fab;
    private EditText usernam, password;
    private Button button;

    private LoginController controller;

    @Override
    public Integer getViewId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void init() {
        hideToolbar();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, SignupActivity.class), options.toBundle());
            }
        });

        controller = new LoginController(this);
        usernam = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        button = findViewById(R.id.bt_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        if (BmobUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }


    void login() {
        usernam.setError(null);
        password.setError(null);
        if (TextUtils.isEmpty(usernam.getText().toString())) {
            usernam.setError("未填写");
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("未填写");
            return;
        }

        controller.login(usernam.getText().toString(), password.getText().toString());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(MyUser user, String accout) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
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
