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
import com.example.xiaoqiang.baoxiao.common.controller.QueryController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends MyBaseActivity implements QueryView {

    RecyclerView recyclerView;

    List<Applicant> datas;
    rAdapter adapter;

    QueryController controller;

    MyUser user;

    SelectListDialog dialog;


    @Override
    public Integer getViewId() {
        return R.layout.activity_request;
    }

    @Override
    public void init() {

        controller = new QueryController(this);
        datas = new ArrayList<>();
        adapter = new rAdapter();
        recyclerView = findViewById(R.id.reuest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        user = BmobUser.getCurrentUser(MyUser.class);
        controller.queryCompany(user.getObjectId());


    }

    @Override
    public void onQuerySuccess(List<Company> result) {
        Log.e("xiaoqiang", result.get(0).getObjectId() + "???/");
        controller.queryRester(result.get(0).getObjectId());
    }

    @Override
    public void onQueryCompayCreator(MyUser name) {

    }

    @Override
    public void onQueryRequester(List<Applicant> result) {
        Log.e("xiaoqiang", result.size() + "???22");
        datas = result;
        adapter.notifyDataSetChanged();
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
            holder.y.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.n.setOnClickListener(new View.OnClickListener() {
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
