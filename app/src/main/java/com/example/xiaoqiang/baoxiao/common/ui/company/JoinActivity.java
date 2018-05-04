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

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.Applicant;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.controller.SaveController;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class JoinActivity extends MyBaseActivity implements QueryView, SaveView, UpdataView {

    private RecyclerView recyclerView;

    List<Company> datas;
    QueryController controller;
    UpdataController updataController;
    SaveController saveController;
    jAdapter adapter;

    @Override
    public Integer getViewId() {
        return R.layout.activity_join;
    }

    @Override
    public void init() {
        datas = new ArrayList<>();
        controller = new QueryController(this);
        updataController = new UpdataController(this);
        adapter = new jAdapter();
        recyclerView = findViewById(R.id.join_recycylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        controller.queryAllCompany();

        saveController = new SaveController(this);
    }

    @Override
    public void onQuerySuccess(List<Company> result) {
        datas = result;
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onQueryCompayCreator(MyUser name) {

    }

    @Override
    public void onQueryRequester(List<Applicant> result) {

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

    @Override
    public void onCompanyCreateSuccess(String result) {

    }

    @Override
    public void onRequestCreateSuccess(String result) {

        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        user.setApplying(true);
        updataController.updataUser(user);
    }

    @Override
    public void onStateUserCreateSuccess(String result) {

    }

    @Override
    public void onUpdataUserSuccess() {
        ToastUtil.show("申请成功");
        finish();
    }

    class jViewHolder extends RecyclerView.ViewHolder {
        TextView name, d, c;
        Button join;

        public jViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_company_name);
            d = itemView.findViewById(R.id.item_company_d);
            c = itemView.findViewById(R.id.item_company_c);
            join = itemView.findViewById(R.id.item_company_btn);
        }
    }

    class jAdapter extends RecyclerView.Adapter<jViewHolder> {

        @NonNull
        @Override
        public jViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(JoinActivity.this).inflate(R.layout.item_company, parent, false);
            return new jViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull jViewHolder holder, final int position) {
            holder.name.setText(datas.get(position).getName());
            holder.d.setText(datas.get(position).getDescribe());

            holder.c.setText("创建者：" + datas.get(position).getCreator().getNickName());

            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    if (TextUtils.isEmpty(user.getNickName()) || TextUtils.isEmpty(user.getPhotoPath())) {
                        ToastUtil.show("个人信息尚未完善，不能加入");
                        return;
                    }
                    Applicant applicant = new Applicant();
                    applicant.setCompanyId(datas.get(position).getObjectId());
                    applicant.setUser(BmobUser.getCurrentUser(MyUser.class));
                    saveController.createRequest(applicant);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }
    }

}
