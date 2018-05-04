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
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.SelectListDialog;
import com.example.xiaoqiang.baoxiao.common.view.QueryView;
import com.example.xiaoqiang.baoxiao.common.view.SaveView;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends MyBaseActivity implements QueryView {

    RecyclerView recyclerView;

    List<Applicant> datas;
    rAdapter adapter;

    QueryController controller;
    UpdataController updataController;
    MyUser user, fix_user,chose_user;

    SelectListDialog dialog;

    String companyId;
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public Integer getViewId() {
        return R.layout.activity_request;
    }

    @Override
    public void init() {
        dialog = new SelectListDialog(this, null, FastConstant.SELECT_DIALOG_ZHIWEI, "设置职位");
        dialog.setItemSelectListener(new SelectListDialog.ItemSelect() {
            @Override
            public void onItemUserSelect(MyUser user) {

            }

            @Override
            public void onItemSelect(String content) {
                Log.e("xiaoqiang", "content" + content);
                for (String key : SpManager.mPositionManager.keySet()) {
                    if (content.equals(SpManager.mPositionManager.get(key))) {
                        Log.e("xiaoqiang", "content+????"+Integer.valueOf(key));
                        fix_user.setPosition(Integer.valueOf(key));
                        fix_user.setJoinCompany(true);
                        fix_user.setCompanyId(companyId);
                        updataController.updataUser(fix_user, chose_user.getObjectId());
                    }
                }
            }
        });
        updataController = new UpdataController(new UpdataView() {
            @Override
            public void onUpdataUserSuccess() {

                Log.e("xiaoqiang", "?");
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
                controller.queryRester(companyId);
            }
        });
        user = BmobUser.getCurrentUser(MyUser.class);
        fix_user=new MyUser();
        controller.queryCompany(user.getObjectId());


    }

    @Override
    public void onQuerySuccess(List<Company> result) {
        Log.e("xiaoqiang", result.get(0).getObjectId() + "???/");
        companyId = result.get(0).getObjectId();
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
        loadingDialog.hide();
        smartRefreshLayout.finishRefresh();
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
            chose_user = datas.get(position).getUser();
            if (!TextUtils.isEmpty(datas.get(position).getUser().getPhotoPath())) {
                Glide.with(RequestActivity.this).load(datas.get(position).getUser().getPhotoPath()).into(holder.head);

            }
            holder.nickenake.setText(datas.get(position).getUser().getNickName());
            holder.y.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.show();
                }
            });
            holder.n.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fix_user.setApplying(false);
                    updataController.updataUser(fix_user, chose_user.getObjectId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }
    }

}
