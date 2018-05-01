package com.example.xiaoqiang.baoxiao.common.ui.company;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.Company;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends MyBaseActivity implements QueryView {

    private RecyclerView recyclerView;

    List<Company> datas;
    QueryController controller;


    jAdapter adapter;

    @Override
    public Integer getViewId() {
        return R.layout.activity_join;
    }

    @Override
    public void init() {
        datas = new ArrayList<>();
        controller = new QueryController(this);
        adapter = new jAdapter();
        recyclerView = findViewById(R.id.join_recycylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        controller.queryAllCompany();

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
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(Throwable throwable) {

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
        public void onBindViewHolder(@NonNull jViewHolder holder, int position) {
            holder.name.setText(datas.get(position).getName());
            holder.d.setText(datas.get(position).getDescribe());
            holder.c.setText("创建者："+datas.get(position).getCreatorNickName());
            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }
    }

}
