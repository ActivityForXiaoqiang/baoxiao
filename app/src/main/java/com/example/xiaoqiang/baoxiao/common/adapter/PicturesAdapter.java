package com.example.xiaoqiang.baoxiao.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by yhx
 * data: 2018/4/30.
 */

public class PicturesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AlbumFile> listUrls;

    public PicturesAdapter(Context mContext, ArrayList<AlbumFile> listUrls) {
        this.mContext = mContext;
        this.listUrls = listUrls;
        if (listUrls.size() == 7) {
            listUrls.remove(listUrls.size() - 1);
        }
    }

    public int getCount() {
        return listUrls.size();
    }

    @Override
    public String getItem(int position) {
        return listUrls.get(position).getPath();
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
        holder.image.setImageResource(R.drawable.add_img);
        final String path = listUrls.get(position).getPath();
        Timber.i(listUrls.size() + "listUrls---" + path);
        if (TextUtils.equals("000000", path)) {
            GlideManager.loadImg(R.drawable.add_img, holder.image);
        } else {
            GlideManager.loadImg(path, holder.image);
//            Glide.with(mContext).load(path).apply(GlideManager.getRequestOptions()).into(holder.image);

//            Glide.with(this)
//                    .load(path)
//                    .placeholder(R.mipmap.default_error)
//                    .error(R.mipmap.default_error)
//                    .centerCrop()
//                    .crossFade()
//                    .into(holder.image);

        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
