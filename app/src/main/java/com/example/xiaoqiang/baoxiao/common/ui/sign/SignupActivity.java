package com.example.xiaoqiang.baoxiao.common.ui.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.SaveController;
import com.example.xiaoqiang.baoxiao.common.controller.SignupController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;
import com.example.xiaoqiang.baoxiao.common.view.SignupView;

public class SignupActivity extends MyBaseActivity implements SignupView, SaveView {

    private FloatingActionButton fab;
    private CardView cvAdd;
    private EditText username, password, repeatpassword;
    private Button button;

    private SignupController controller;

    private SaveController saveController;

    @Override
    public Integer getViewId() {
        return R.layout.activity_signup;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void init() {

        hideToolbar();
        ShowEnterAnimation();
        cvAdd = findViewById(R.id.cv_add);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        controller = new SignupController(this);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        repeatpassword = findViewById(R.id.et_repeatpassword);
        button = findViewById(R.id.bt_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        saveController = new SaveController(this);
    }


    void signup() {
        username.setError(null);
        password.setError(null);
        repeatpassword.setError(null);

        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("未填写");
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("未填写");
            return;
        }
        if (TextUtils.isEmpty(repeatpassword.getText().toString())) {
            repeatpassword.setError("未填写");
            return;
        }

        if (!password.getText().toString().equals(repeatpassword.getText().toString())) {
            Toast.makeText(this, "密码不一致1", Toast.LENGTH_SHORT).show();
            return;
        }

        controller.signup(username.getText().toString(), password.getText().toString());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.mipmap.plus);
                SignupActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess(MyUser user) {
        StateUser stateUser = new StateUser();
        stateUser.setAppying(false);
        stateUser.setJoinCompay(false);
        stateUser.setPosition(0);
        stateUser.setUser(user);
        stateUser.setCompany(null);
        saveController.createStateUser(stateUser);

    }

    @Override
    public void showDialog() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void onCompanyCreateSuccess(String result) {

    }

    @Override
    public void onRequestCreateSuccess(String result) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStateUserCreateSuccess(String result) {
        animateRevealClose();
    }
}
