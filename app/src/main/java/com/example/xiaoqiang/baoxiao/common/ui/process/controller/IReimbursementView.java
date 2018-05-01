package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.IBaseView;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 申请流程view
 */

public interface IReimbursementView extends IBaseView {
    void onAlbumSuccess(ArrayList<AlbumFile> result);

    void onAlbumCancel( String result);
}
