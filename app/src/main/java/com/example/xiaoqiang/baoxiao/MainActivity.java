package com.example.xiaoqiang.baoxiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.ui.info.MineActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementActivity;
import com.google.gson.Gson;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * 小区
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headLayout = navigationView.getHeaderView(0);
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MineActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_manage:
                toReimbursement();
                break;
        }
        return false;
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
}
