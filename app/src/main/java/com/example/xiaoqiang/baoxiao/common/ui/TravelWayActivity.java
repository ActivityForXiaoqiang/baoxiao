package com.example.xiaoqiang.baoxiao.common.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.AsyncRequestExecutor;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

public class TravelWayActivity extends MyBaseActivity {
    String appkey = "3d1580255eed96ac2fa1368adbc4ae4c";
    String url = "http://v.juhe.cn/multway/plans?from=" + "上海" + "&to=南京&date=2018-05-17&mode=2&type=3&key=" + appkey;

    private RecyclerView recyclerView;

    @Override
    public Integer getViewId() {
        return R.layout.activity_trave;
    }

    @Override
    public void init() {
        recyclerView=findViewById(R.id.trave_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StringRequest request = new StringRequest(url, RequestMethod.GET);


        AsyncRequestExecutor.INSTANCE.execute(0, request, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.e("xiaoqiang", response.get());
                super.onSucceed(what, response);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.e("xiaoqiang", "?" + response.responseCode());
                super.onFailed(what, response);
            }
        });
    }

    class tVH extends RecyclerView.ViewHolder{

        public tVH(View itemView) {
            super(itemView);
        }
    }

    class tAdapter extends RecyclerView.Adapter<tVH>{

        @NonNull
        @Override
        public tVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(TravelWayActivity.this).inflate(R.layout.item_trave,parent,false);
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull tVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
