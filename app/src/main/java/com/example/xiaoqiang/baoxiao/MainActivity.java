package com.example.xiaoqiang.baoxiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.example.xiaoqiang.baoxiao.common.ui.company.CreateCompanyActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.JoinActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.ui.company.RequestActivity;
import com.example.xiaoqiang.baoxiao.common.ui.info.MineActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementActivity;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.BmobUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, QueryView {


    CircleImageView head;
    MyUser user;
    TextView nickname;
    QueryController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new QueryController(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headLayout = navigationView.getHeaderView(0);
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MineActivity.class));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        head = headLayout.findViewById(R.id.nav_headImg);
        nickname = headLayout.findViewById(R.id.nav_username);
        user = BmobUser.getCurrentUser(MyUser.class);
        if (!TextUtils.isEmpty(user.getPhotoPath())) {
            Glide.with(this).load(user.getPhotoPath()).apply(GlideManager.getRequestOptions()).into(head);
        }
        if (!TextUtils.isEmpty(user.getNickName())) {
            nickname.setText(user.getNickName());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        controller.queryStatuser(user);

    }

    private void initCompayView() {
        RelativeLayout nullView = findViewById(R.id.null_data_view);
        nullView.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user.isSuper()) {
            initCompayView();
        } else {
            controller.queryStatuser(user);
//            if (user.isJoinCompany()) {
//                initCompayView();
//
//            } else {
                getMenuInflater().inflate(R.menu.main, menu);
//
//            }
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(MainActivity.this, JoinActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_company:
                break;
            case R.id.nav_create:
                if (TextUtils.isEmpty(user.getNickName())) {
                    Toast.makeText(MainActivity.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, CreateCompanyActivity.class));
                }

                break;
            case R.id.nav_manager:
                if (user.isSuper()) {
                    startActivity(new Intent(MainActivity.this, RequestActivity.class));
                }

                break;
            case R.id.nav_baoxiao:
                toReimbursement();
                break;
            case R.id.nav_shiyi:
                break;
            case R.id.nav_logout:
                BmobUser.logOut();
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toReimbursement() {
        Gson gson = new Gson();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser == null) {
            ToastUtil.show("你还未登陆");
            return;
        }
        MyUser user = gson.fromJson(gson.toJson(bmobUser), MyUser.class);

//        if (TextUtils.isEmpty(user.getCompanyId())) {
//            ToastUtil.show("您还没有所属公司");
//            return;
//        }
        FastUtil.startActivity(MainActivity.this, ReimbursementActivity.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            user = BmobUser.getCurrentUser(MyUser.class);
            nickname.setText(user.getNickName());

            if (!TextUtils.isEmpty(user.getPhotoPath())) {
                Glide.with(this).load(user.getPhotoPath()).apply(GlideManager.getRequestOptions()).into(head);
            }
        }
    }


    @Override
    public void onQuerySuccess(List<Company> result) {

    }

    @Override
    public void onQueryCompayCreator(MyUser name) {

    }

    @Override
    public void onQueryRequester(List<Applicant> result) {

    }

    @Override
    public void onQueryStateUser(List<StateUser> result) {
            
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
