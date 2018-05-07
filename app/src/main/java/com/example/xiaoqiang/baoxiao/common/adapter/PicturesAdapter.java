package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhx
 * data: 2018/4/30.
 */

public class PicturesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AlbumFile> listUrls;
    private List<String> mImgs;

    public PicturesAdapter(Context mContext, ArrayList<AlbumFile> listUrls) {
        this.mContext = mContext;
        this.listUrls = listUrls;
        if (listUrls.size() == 7) {
            listUrls.remove(listUrls.size() - 1);
        }
    }

    public PicturesAdapter(Context mContext, List<String> mImgs) {
        this.mContext = mContext;
        this.mImgs = mImgs;
        if (mImgs.size() == 7) {
            mImgs.remove(mImgs.size() - 1);
        }
    }

    public int getCount() {
        return listUrls == null ? mImgs.size() : listUrls.size();
    }

    @Override
    public String getItem(int position) {
        return listUrls == null ? mImgs.get(position) : listUrls.get(position).getPath();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_image, null);
            holder.image = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.image.setImageResource(R.drawable.add_img);
        final String path = listUrls == null ? mImgs.get(position) : listUrls.get(position).getPath();
        if (TextUtils.equals("000000", path)) {
            RequestOptions requestOptions = new RequestOptions()
                    .centerInside() // 填充方式
                    .priority(Priority.HIGH) //优先级
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//缓存策略
            Glide.with(mContext).load(R.drawable.add_img).apply(requestOptions).into(holder.image);
//            GlideManager.loadImg(R.drawable.add_img, holder.image);
        } else {
            GlideManager.loadImg(path, holder.image);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
