package com.example.xiaoqiang.baoxiao.common.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.Plan;
import com.example.xiaoqiang.baoxiao.common.been.Trave;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.TimeFormatUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.AsyncRequestExecutor;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import java.util.Date;
import java.util.List;

public class TravelWayActivity extends MyBaseActivity {
    String appkey = "3d1580255eed96ac2fa1368adbc4ae4c";
    String url = "http://v.juhe.cn/multway/plans?from=" + "%from%" + "&to=" + "%to%" + "&date=" + "%time%" + "&mode=" + "%mode%" + "&type=3&key=" + appkey;

    private RecyclerView recyclerView;
    private List<String> datas;
    private Trave trave;

    private tAdapter adapter;

    String from, to, time, mode;

    @Override
    public Integer getViewId() {
        return R.layout.activity_trave;
    }

    @Override
    public void init() {


        time = TimeFormatUtil.formatTime(new Date(), "yyyy-MM-dd");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        mode = getIntent().getStringExtra("mode");

        url.replace("%from%", from);
        url.replace("%to%", to);
        url.replace("%time%", time);
        url.replace("%mode%", mode);

        recyclerView = findViewById(R.id.trave_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new tAdapter();
        recyclerView.setAdapter(adapter);

        StringRequest request = new StringRequest(url, RequestMethod.GET);
        AsyncRequestExecutor.INSTANCE.execute(0, request, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.e("xiaoqiang", response.get());
                dealData(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.e("xiaoqiang", "?" + response.responseCode());
                super.onFailed(what, response);
            }
        });
    }


    void dealData(String data) {
        trave = new Gson().fromJson(data, Trave.class);
        if (trave.error_code != 0) {
            ToastUtil.show("服务器错误！" + trave.error_code);
            return;
        }
        adapter.notifyDataSetChanged();
    }


    class tVH extends RecyclerView.ViewHolder {
        TextView title, no, zuowei, start, end, jiage;

        public tVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.t_item_title);
            no = itemView.findViewById(R.id.checi);
            start = itemView.findViewById(R.id.starttime);
            end = itemView.findViewById(R.id.endtime);
            zuowei = itemView.findViewById(R.id.zhuwei);
            jiage = itemView.findViewById(R.id.jiage);
        }
    }

    class tAdapter extends RecyclerView.Adapter<tVH> {

        @NonNull
        @Override
        public tVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TravelWayActivity.this).inflate(R.layout.item_trave, parent, false);
            return new tVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull tVH holder, int position) {
            Plan plan = trave.result.data.get(0).plans.get(position);
            holder.title.setText(plan.from + "--" + plan.to);
            holder.jiage.setText("¥" + plan.price);
            holder.zuowei.setText(plan.legs.get(0).cabinname);
            holder.start.setText("出发时间 :" + plan.stime);
            holder.end.setText("抵达时间 :" + plan.etime);
            holder.no.setText("航班/车次 -" + plan.legs.get(0).no);
        }

        @Override
        public int getItemCount() {


            return trave == null ? 0 : trave.result.data.get(0).plans == null ? 0 : trave.result.data.get(0).plans.size();
        }
    }
}
