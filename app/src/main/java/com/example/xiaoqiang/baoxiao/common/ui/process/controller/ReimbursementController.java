package com.example.xiaoqiang.baoxiao.common.ui.process.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.ProcessEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.basis.BaseController;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by yhx
 * data: 2018/4/30.
 * 申请流程Controller
 */

public class ReimbursementController extends BaseController<IReimbursementView> {
    private Context mContext;


    public ReimbursementController setmContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    /**
     * 获取公司级别
     */
    public void getPositionByCompanyId() {

    }

    /**
     * 根据公司id获取 人员列表
     */
    public void getPersonListByCompanyId(String companyId) {
        //最后组装完整的and条件
        showLoadingDialog(mContext);
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.order("-position");// 根据职位字段降序显示数据
        // companyId== companyId
        query.addWhereEqualTo("companyId", companyId);
        query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> object, BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    getView().onShowPersonList(object);
                } else {
                    Timber.d("showError:" + e.toString());
//                    getView().showError(e.getMessage().toString());
                }
            }

        });
    }

    /**
     * 上传照片
     */
    public void upDataFiles(ArrayList<AlbumFile> alist, final ProcessEntity pe) {
        showLoadingDialog(mContext);
        final String[] filePaths = new String[alist.size()];
        for (int i = 0; i < alist.size(); i++) {
            filePaths[i] = alist.get(i).getPath();
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    pe.setImgs(urls);
                    saveProcess(pe);
                } else {
                    ToastUtil.show("提交失败，请检查网络是否良好");
                    dissmissLoadingDialog();
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                ToastUtil.show("提交失败，请检查网络是否良好");
                Timber.d("onProgress:" + "错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                Timber.d("onProgress:" + curIndex);
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    /**
     * 保存流程
     */
    public void saveProcess(ProcessEntity pe) {
        showLoadingDialog(mContext);
        pe.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                dissmissLoadingDialog();
                if (e == null) {
                    ToastUtil.show("提交成功");
                } else {
                    Timber.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void openAlbum(Context context, ArrayList<AlbumFile> mAlbumFiles) {
        Album.album(context)
                .multipleChoice()
                .selectCount(6)
                .camera(true)
                .columnCount(3)
                .checkedList(mAlbumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            Timber.i(result.size() + "-----" + result.get(i).getPath());
                        }
                        getView().onAlbumSuccess(result);
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
