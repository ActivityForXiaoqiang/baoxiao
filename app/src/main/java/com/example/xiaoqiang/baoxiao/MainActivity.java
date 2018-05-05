package com.example.xiaoqiang.baoxiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.example.xiaoqiang.baoxiao.common.ui.company.CreateCompanyActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.JoinActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.RequestActivity;
import com.example.xiaoqiang.baoxiao.common.ui.info.MineActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.ProcessListActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementActivity;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, QueryView {


    CircleImageView head;
    MyUser user;
    TextView nickname;
    QueryController controller;

    StateUser stateUser;

    LoadingDialog dialog;

    RecyclerView recyclerView;
    SmartRefreshLayout smartRefreshLayout;

    List<StateUser> companyUsers;

    Company company;

    mAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        companyUsers = new ArrayList<>();
        dialog = new LoadingDialog(this);
        controller = new QueryController(this);
        user = BmobUser.getCurrentUser(MyUser.class);
        controller.queryStatuser(user);


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

        if (!TextUtils.isEmpty(user.getPhotoPath())) {
            Glide.with(this).load(user.getPhotoPath()).apply(GlideManager.getRequestOptions()).into(head);
        }
        if (!TextUtils.isEmpty(user.getNickName())) {
            nickname.setText(user.getNickName());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new mAdapter();
        recyclerView.setAdapter(adapter);
        smartRefreshLayout = findViewById(R.id.main_smart);
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                controller.queryCompanyUser(company);
            }
        });
    }

    private void initCompayView() {
        RelativeLayout nullView = findViewById(R.id.null_data_view);
        nullView.setVisibility(View.GONE);
        TextView companyTitle = findViewById(R.id.company_title);
        companyTitle.setText(stateUser.getCompany().getName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (stateUser.isJoinCompay() || stateUser.isAppying()) {
            ToastUtil.show("已加入公司或正在申请中");
        } else {
            Intent it = new Intent(MainActivity.this, JoinActivity.class);
            it.putExtra("objId", stateUser.getObjectId());

            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_company:
                break;
            case R.id.nav_create:
                if (stateUser.isJoinCompay()) {
                    ToastUtil.show("已加入公司！ 不能创建！");
                } else {
                    if (stateUser.isAppying()){
                        ToastUtil.show("正在申请加入公司！ 不能创建！");
                    }else {
                        if (TextUtils.isEmpty(user.getNickName())) {
                            Toast.makeText(MainActivity.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent it = new Intent(MainActivity.this, CreateCompanyActivity.class);
                            it.putExtra("objId", stateUser.getObjectId());
                            startActivity(it);
                        }
                    }

                }


                break;
            case R.id.nav_manager:
                if (user.isSuper()) {

                    Intent it = new Intent(MainActivity.this, RequestActivity.class);
                    it.putExtra("objId", stateUser.getObjectId());
                    startActivity(it);
                }

                break;
            case R.id.nav_baoxiao:
                getUserInfo(1);
                break;
            case R.id.nav_shiyi:
                getUserInfo(2);
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

    private void getUserInfo(final int type) {
        SpManager.getInstance().queryCurrentUser(this, user, new SpManager.IGetCurrentUser() {
            @Override
            public void showMyUser(StateUser user) {
                toProcess(type, user);
            }
        });
    }

    private void toProcess(int type, StateUser user) {

        if (user == null) {
            ToastUtil.show("你还未登陆");
            return;
        }

        if (!user.isJoinCompay()) {
            ToastUtil.show("您还没有所属公司");
            return;
        }

        if (type == 1) {
            FastUtil.startActivity(MainActivity.this, ReimbursementActivity.class);
        } else if (type == 2) {
            FastUtil.startActivity(MainActivity.this, ProcessListActivity.class);
        }
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

        if (controller != null) {
            if (user != null) {
                controller.queryStatuser(user);
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
        stateUser = result.get(0);
        if (stateUser.isJoinCompay()) {
            initCompayView();
            company = stateUser.getCompany();
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onQueryCompanyUser(List<StateUser> result) {
        companyUsers = result;
        adapter.notifyDataSetChanged();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void hideDialog() {
        dialog.hide();
    }

    @Override
    public void showError(Throwable throwable) {

    }


    class mViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView head;

        public mViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_main_name);
            head = itemView.findViewById(R.id.mian_item_head);
        }
    }

    class mAdapter extends RecyclerView.Adapter<mViewHolder> {

        @NonNull
        @Override
        public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main, parent, false);

            return new mViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
            MyUser itemuser = companyUsers.get(position).getUser();
            holder.name.setText(itemuser.getNickName());
            Glide.with(MainActivity.this).load(itemuser.getPhotoPath()).into(holder.head);
        }

        @Override
        public int getItemCount() {
            return companyUsers == null ? 0 : companyUsers.size();
        }
    }

}
