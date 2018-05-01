package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.GlideManager;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineActivity extends MyBaseActivity {
    private CircleImageView head;

    private ArrayList<AlbumFile> mAlbumFiles;

    @Override
    public Integer getViewId() {
        return R.layout.activity_mine;
    }

    @Override
    public void init() {
        head = findViewById(R.id.mine_head);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    void openAlbum() {

        Album.album(this)
                .multipleChoice()
                .selectCount(1)
                .camera(true)
                .columnCount(4)
                .checkedList(mAlbumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        Glide.with(MineActivity.this).load(result.get(0).getPath()).apply(GlideManager.getRequestOptions()).into(head);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();

    }
}
