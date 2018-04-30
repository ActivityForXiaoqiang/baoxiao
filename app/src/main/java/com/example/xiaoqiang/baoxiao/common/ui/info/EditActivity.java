package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.listener.OnItemClickListener;
import com.example.xiaoqiang.baoxiao.common.listener.RecyclerItemClickListener;

public class EditActivity extends MyBaseActivity {

    private String[] items = {"昵称", "性别", "xx"};

    private RecyclerView recyclerView;

    @Override
    public Integer getViewId() {
        return R.layout.activity_infoedit;
    }

    @Override
    public void init() {
        recyclerView = findViewById(R.id.info_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new eAdapter());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        }));
    }

    class eViewHodler extends RecyclerView.ViewHolder {

        TextView item, content;

        public eViewHodler(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.info_item_name);
            content = itemView.findViewById(R.id.info_item_content);
        }
    }


    class eAdapter extends RecyclerView.Adapter<eViewHodler> {

        @NonNull
        @Override
        public eViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.item_info, parent, false);
            return new eViewHodler(view);
        }

        @Override
        public void onBindViewHolder(@NonNull eViewHodler holder, int position) {
            holder.item.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }
}
