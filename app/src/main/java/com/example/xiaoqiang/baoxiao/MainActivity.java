package com.example.xiaoqiang.baoxiao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xiaoqiang.baoxiao.common.adapter.SectionAdapter;
import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.BudgetEntity;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.DayTime;
import com.example.xiaoqiang.baoxiao.common.been.MySection;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.BudgetController;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.FastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.NumberFormatterUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.example.xiaoqiang.baoxiao.common.ui.TravelWayActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.CreateCompanyActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.JoinActivity;
import com.example.xiaoqiang.baoxiao.common.ui.company.RequestActivity;
import com.example.xiaoqiang.baoxiao.common.ui.info.BudgetActivity;
import com.example.xiaoqiang.baoxiao.common.ui.info.MineActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.ProcessListActivity;
import com.example.xiaoqiang.baoxiao.common.ui.process.reimbursement.ReimbursementActivity;
import com.example.xiaoqiang.baoxiao.common.ui.sign.LoginActivity;
import com.example.xiaoqiang.baoxiao.common.view.IBudgetListView;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
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


    Company company;


    List<MySection> mainDatas;
    SectionAdapter sectionAdapter;
    AlertDialog dialogTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainDatas = new ArrayList<>();
        dialog = new LoadingDialog(this);
        controller = new QueryController(this);
        user = BmobUser.getCurrentUser(MyUser.class);
        controller.queryStatuser(user);

        if (user.isSuper()) {
            TextView tips = findViewById(R.id.main_tip);
            tips.setVisibility(View.VISIBLE);
        }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sectionAdapter = new SectionAdapter(this, R.layout.item_main, R.layout.item_main_head, mainDatas);
        sectionAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if (user.isSuper()) {
                    if (!mainDatas.get(position).isHeader) {
                        if (dialogTip == null) {
                            dialogTip = new AlertDialog.Builder(MainActivity.this).create();
                            dialogTip.setTitle("提示");
                            dialogTip.setMessage("是否将该成员移除？");
                            dialogTip.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogTip.dismiss();
                                    deletUser(mainDatas.get(position).t);

                                }
                            });
                            dialogTip.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogTip.dismiss();

                                }
                            });
                            dialogTip.show();
                        } else {
                            dialogTip.show();
                        }
                    }
                }

                return false;
            }
        });

        recyclerView.setAdapter(sectionAdapter);

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

    private void deletUser(StateUser stateUser) {
        showDialog();
        stateUser.setDepartment(100);
        stateUser.setAppying(false);
        stateUser.setJoinCompay(false);
        stateUser.setPosition(100);
        stateUser.setCompany(new Company());
        stateUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtil.show("移除成功");
                    smartRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.show("移除失败");
                }
                hideDialog();
            }
        });
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
//                queryTotal(0);
//                queryBM(0);
                break;
            case R.id.nav_create:
                if (stateUser.isJoinCompay()) {
                    ToastUtil.show("已加入公司！ 不能创建！");
                } else {
                    if (stateUser.isAppying()) {
                        ToastUtil.show("正在申请加入公司！ 不能创建！");
                    } else {
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
                if (user.isSuper()) {
                    ToastUtil.show("管理员无需报销！");
                } else {
                    getUserInfo(1);
                }

                break;
            case R.id.nav_shiyi:
                getUserInfo(2);
                break;
            case R.id.nav_logout:
                BmobUser.logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
            ToastUtil.show("您还未登陆");
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
            if (!TextUtils.isEmpty(user.getNickName()))
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

        getDatas(result);

        sectionAdapter.notifyDataSetChanged();
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

    void getDatas(List<StateUser> datas) {
        mainDatas.clear();
        List<StateUser>[] array = new ArrayList[8];
        for (int i = 0; i < array.length; i++) {
            array[i] = new ArrayList<>();
        }

        for (StateUser su : datas) {
            if (su.getDepartment() == -1) {
                array[0].add(su);
            } else if (su.getDepartment() != -2) {
                array[su.getDepartment() + 1].add(su);
            }

        }

        for (int i = 0; i < array.length; i++) {

            if (array[i].size() > 0) {
                switch (i) {
                    case 0:
                        mainDatas.add(new MySection(true, "总经理"));
                        break;
                    case 1:
                        mainDatas.add(new MySection(true, "销售部门"));
                        break;
                    case 2:
                        mainDatas.add(new MySection(true, "人事部门"));
                        break;
                    case 3:
                        mainDatas.add(new MySection(true, "财务部门"));
                        break;
                    case 4:
                        mainDatas.add(new MySection(true, "设计部门"));
                        break;
                    case 5:
                        mainDatas.add(new MySection(true, "技术部门"));
                        break;
                    case 6:
                        mainDatas.add(new MySection(true, "生产部门"));
                        break;
                    case 7:
                        mainDatas.add(new MySection(true, "其他部门"));
                        break;

                }
                addData(array[i]);
            }
        }
        if (stateUser.getPosition() == 5 || stateUser.getPosition() == 3) {
            for (MySection mySection : mainDatas) {
                if (mySection.isHeader) {
                    for (Integer key : SpManager.mPositionManager.keySet()) {
                        if (mySection.header.equals(SpManager.mBumenManager.get(key))) {
                            queryBM(key);
                        }
                    }
                }
            }
        }

    }

    void addData(List<StateUser> arrays) {
        for (StateUser su : arrays) {
            mainDatas.add(new MySection(su));
        }

    }

    //
    void queryTotal(final int bmId, final List<BudgetEntity> list) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        DayTime dayTime = TimeFormatUtil.getMonthTimeOfMonth(ca);

        //--and条件1
        BmobQuery<ProcessEntity> eq1 = new BmobQuery<>();
        eq1.addWhereLessThanOrEqualTo("createdAt", dayTime.getMaxDate());//時間<=MaxTime
        //--and条件2
        BmobQuery<ProcessEntity> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("createdAt", dayTime.getMinDate());//時間>=MinTime
        List<BmobQuery<ProcessEntity>> andQuery = new ArrayList<>();
        andQuery.add(eq1);
        andQuery.add(eq2);

        BmobQuery<ProcessEntity> query = new BmobQuery<>();
        query.and(andQuery);
        query.addWhereEqualTo("point", FastConstant.PROCESS_POINT_FINISH);
        query.addWhereEqualTo("departmentId", bmId);
        query.addWhereEqualTo("companyId", stateUser.getCompany().getObjectId());
        query.sum(new String[]{"amount"});
        query.findStatistics(ProcessEntity.class, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    JSONObject obj = null;
                    try {
                        obj = ary.getJSONObject(0);
                        double sum = obj.getDouble("_sumAmount");
                        for (BudgetEntity be : list) {
                            if (sum > (be.getBudgetAmount() * be.getCordon() / 100)) {
                                for (MySection mySection : mainDatas) {
                                    if (mySection.isHeader) {
                                        if (mySection.header.equals(SpManager.mBumenManager.get(bmId))) {
                                            mySection.isBig = true;
                                            Log.e("xiaoqiang", "??//");
                                        }
                                    }
                                }

                            }
                        }
                        sectionAdapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


    }


    void queryBM(final int bmID) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        DayTime dayTime = TimeFormatUtil.getMonthTimeOfMonth(ca);

        //--and条件1
        BmobQuery<BudgetEntity> eq1 = new BmobQuery<>();
        eq1.addWhereLessThanOrEqualTo("createdAt", dayTime.getMaxDate());//時間<=MaxTime
        //--and条件2
        BmobQuery<BudgetEntity> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("createdAt", dayTime.getMinDate());//時間>=MinTime
        List<BmobQuery<BudgetEntity>> andQuery = new ArrayList<>();
        andQuery.add(eq1);
        andQuery.add(eq2);

        BmobQuery<BudgetEntity> query = new BmobQuery<>();
        query.and(andQuery);
        query.addWhereEqualTo("companyId", stateUser.getCompany().getObjectId());
        query.addWhereEqualTo("department", bmID);
        query.findObjects(new FindListener<BudgetEntity>() {
            @Override
            public void done(List<BudgetEntity> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        Log.e("xiaoqiang", list.size() + "---");
                        queryTotal(bmID, list);
                    }
                }
            }
        });
    }

}
