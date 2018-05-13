package com.example.xiaoqiang.baoxiao.common.ui.company;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.controller.DelectController;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.view.DelectView;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends MyBaseActivity implements QueryView, DelectView {

    RecyclerView recyclerView;

    List<Applicant> datas;
    rAdapter adapter;

    QueryController controller;
    UpdataController updataController;
    DelectController delectController;
    MyUser user;

    SelectListDialog dialog, dialog2;
    Company company;
    String stateId;
    String deleteId;
    StateUser stateUser;
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public Integer getViewId() {
        return R.layout.activity_request;
    }

    @Override
    public void init() {
        stateUser = new StateUser();
        dialog = new SelectListDialog(this, null, FastConstant.SELECT_DIALOG_ZHIWEI, "设置职位");
        dialog.setItemSelectListener(new SelectListDialog.ItemSelect() {
            @Override
            public void onItemUserSelect(StateUser user) {

            }

            @Override
            public void onItemSelect(String content) {
                Log.e("xiaoqiang", "content" + content);
                for (Integer key : SpManager.mPositionManager.keySet()) {
                    if (content.equals(SpManager.mPositionManager.get(key))) {
                        stateUser.setPosition(key);
                        if (key == 5) {
                            stateUser.setJoinCompay(true);
                            stateUser.setCompany(company);
                            stateUser.setDepartment(-1);
                            updataController.updataStateUser(stateUser, stateId);
                        } else {
                            dialog2.show();
                        }
                        dialog.dismiss();


                    }
                }
            }
        });
        dialog2 = new SelectListDialog(this, null, FastConstant.SELECT_DIALOG_DEPARTMENT, "所属部门");
        dialog2.setItemSelectListener(new SelectListDialog.ItemSelect() {
            @Override
            public void onItemUserSelect(StateUser user) {

            }

            @Override
            public void onItemSelect(String content) {
                Log.e("xiaoqiang", "content" + content);
                stateUser.setJoinCompay(true);
                stateUser.setCompany(company);
                for (Integer key : SpManager.mBumenManager.keySet()) {
                    if (content.equals(SpManager.mBumenManager.get(key))) {
                        stateUser.setDepartment(key);
                    }
                }
                dialog2.dismiss();
                updataController.updataStateUser(stateUser, stateId);
            }
        });

        delectController = new DelectController(this);
        updataController = new UpdataController(new UpdataView() {
            @Override
            public void onUpdataUserSuccess() {

                Log.e("xiaoqiang", "?");
            }

            @Override
            public void onUpdataStateUserSuccess() {
                delectController.delete(deleteId);
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
        controller = new QueryController(this);
        datas = new ArrayList<>();
        adapter = new rAdapter();
        recyclerView = findViewById(R.id.reuest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        smartRefreshLayout = findViewById(R.id.request_freshLayout);
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                controller.queryRester(company.getObjectId());
            }
        });
        user = BmobUser.getCurrentUser(MyUser.class);
        controller.queryCompany(user.getObjectId());


    }

    @Override
    public void onQuerySuccess(List<Company> result) {
        company = result.get(0);
        controller.queryRester(result.get(0).getObjectId());
    }

    @Override
    public void onQueryCompayCreator(MyUser name) {

    }

    @Override
    public void onQueryRequester(List<Applicant> result) {
        datas = result;
        adapter.notifyDataSetChanged();
        loadingDialog.hide();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void onQueryStateUser(List<StateUser> result) {
        stateId = result.get(0).getObjectId();
        dialog.show();
    }

    @Override
    public void onQueryCompanyUser(List<StateUser> result) {

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
    public void onDeleteSuccess() {
        smartRefreshLayout.autoRefresh();
    }


    class rViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        TextView nickenake, realname;
        Button y, n;

        public rViewHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.request_head);
            nickenake = itemView.findViewById(R.id.request_nickname);
            realname = itemView.findViewById(R.id.request_realname);
            y = itemView.findViewById(R.id.tongyi);
            n = itemView.findViewById(R.id.jujue);

        }
    }

    class rAdapter extends RecyclerView.Adapter<rViewHolder> {

        @NonNull
        @Override
        public rViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RequestActivity.this).inflate(R.layout.item_request, parent, false);

            return new rViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull rViewHolder holder, final int position) {

            if (!TextUtils.isEmpty(datas.get(position).getUser().getPhotoPath())) {
                Glide.with(RequestActivity.this).load(datas.get(position).getUser().getPhotoPath()).into(holder.head);

            }

            holder.nickenake.setText(datas.get(position).getUser().getNickName());
            holder.realname.setText(datas.get(position).getUser().getRealname());
            holder.y.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.queryStatuser(datas.get(position).getUser());
                    deleteId = datas.get(position).getObjectId();

                }
            });
            holder.n.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteId = datas.get(position).getObjectId();
                    stateUser.setAppying(false);
                    updataController.updataStateUser(stateUser, stateId);

                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }
    }


}
