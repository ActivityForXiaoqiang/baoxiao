package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.IBaseView;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 申请流程view
 */

public interface IReimbursementView extends IBaseView {
    void onAlbumSuccess(ArrayList<AlbumFile> result);

    void onAlbumCancel(String result);

    void onShowPersonList(List<MyUser> result);
}
