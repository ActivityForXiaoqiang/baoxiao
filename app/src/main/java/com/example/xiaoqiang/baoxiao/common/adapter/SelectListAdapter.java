package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.google.gson.Gson;

import java.util.List;

/**
 * author : yhx
 * time   : 2018/5/17
 * desc   :
 */

public class SelectListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mData;

    public SelectListAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        Timber.i("----" + new Gson().toJson(mData));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_select_list, null);
            vh.mTvContent = view.findViewById(R.id.item_select_list_content);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.mTvContent.setText(mData.get(i));
        return view;
    }

    class ViewHolder {
        TextView mTvContent;
    }
}
