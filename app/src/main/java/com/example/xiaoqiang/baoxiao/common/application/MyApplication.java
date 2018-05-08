package com.example.xiaoqiang.baoxiao.common.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;

import com.example.xiaoqiang.baoxiao.BuildConfig;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.AppImpl;
import com.example.xiaoqiang.baoxiao.common.fast.constant.FastConfig;
import com.example.xiaoqiang.baoxiao.common.fast.constant.entity.FastQuitConfigEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.entity.FastTitleConfigEntity;
import com.example.xiaoqiang.baoxiao.common.fast.constant.manager.LoggerManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SizeUtil;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.SpManager;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.Timber;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.ToastUtil;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private final String BMOB_APPID = "dab83fd5d3a1e9a37787615bca47e6e3";
    private static Context mContext;
    private String TAG = "baoxiao";
    private static int imageHeight = 0;
    private long start;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, BMOB_APPID);

        SpManager.getInstance().initSpManager(this);
        //初始化Logger日志打印
        if (BuildConfig.DEBUG) {
//            LoggerManager.init(TAG, BuildConfig.DEBUG);
            Timber.plant(new Timber.DebugTree());
        }
        start = System.currentTimeMillis();
        LoggerManager.d(TAG, "start:" + start);
        mContext = this;
        //初始化toast工具
        ToastUtil.init(mContext, true, ToastUtil.newBuilder()
                        .setRadius(SizeUtil.dp2px(6))
//                .setPaddingLeft(SizeUtil.dp2px(24))
//                .setPaddingRight(SizeUtil.dp2px(24))
//                .setTextSize(SizeUtil.dp2px(16))
                        .setGravity(Gravity.BOTTOM)
        );

        //主页返回键是否退回桌面(程序后台)
        boolean isBackTask = false;

        //全局配置参数
        AppImpl impl = new AppImpl(mContext);
        //全局标题栏TitleBarView设置--推荐先获取library里默认标题栏TitleBarView配置再按需修改的模式 FastTitleConfigEntity
        FastTitleConfigEntity titleConfig = FastConfig.getInstance(mContext).getTitleConfig();
        //全局主页面返回键操作设置--推荐先获取library里默认主页面点击返回键配置FastQuitConfigEntity配置再按需修改的模式 FastQuitConfigEntity
        FastQuitConfigEntity quitConfig = FastConfig.getInstance(mContext).getQuitConfig();
        FastConfig.getInstance(mContext)
                //设置Activity是否支持滑动返回-添加透明主题参考Demo样式;
                .setSwipeBackEnable(true, this)
                // 设置全局TitleBarView-其它属性请查看getInstance默认设置
                .setTitleConfig(titleConfig
                        //设置TitleBarView 所有TextView颜色
                        .setTitleTextColor(getResources().getColor(R.color.colorWhite))
                        //设置TitleBarView背景资源
                        .setTitleBackgroundResource(R.color.colorPrimary)
                        //设置是否状态栏浅色模式(深色状态栏文字及图标)
                        .setLightStatusBarEnable(false)
                        .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0)
                        //设置TitleBarView海拔高度
                        .setTitleElevation(mContext.getResources().getDimension(R.dimen.dp_elevation)))
                //设置Activity 点击返回键提示退出程序或返回桌面相关参数
                .setQuitConfig(quitConfig
                        //设置是否退回桌面否则直接退出程序
                        .setBackToTaskEnable(isBackTask)
                        //设置退回桌面是否有一次提示setBackToTaskEnable(true)才有意义
                        .setBackToTaskDelayEnable(isBackTask)
                        .setQuitDelay(2000)
                        .setQuitMessage(isBackTask ? getText(R.string.fast_back_home) : getText(R.string.fast_quit_app))
                        .setSnackBarBackgroundColor(Color.argb(220, 0, 0, 0))
                        .setSnackBarEnable(true)
                        .setSnackBarMessageColor(Color.WHITE))
                //设置Glide背景色
                .setPlaceholderColor(getResources().getColor(R.color.colorPlaceholder))
                //设置Glide圆角背景弧度
                .setPlaceholderRoundRadius(mContext.getResources().getDimension(R.dimen.dp_placeholder_radius))
                //设置Activity横竖屏模式
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //设置Activity或Fragment根布局背景资源
                .setContentViewBackgroundResource(R.color.colorWhite)
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置Retrofit全局异常处理-观察者必须为FastObserver及其子类
//                .setHttpErrorControl(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置虚拟导航栏控制
                .setNavigationBarControl(impl);
        LoggerManager.d(TAG, "total:" + (System.currentTimeMillis() - start));
    }

    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight() {
        imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        return imageHeight;
    }

    public static boolean isSupportElevation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Context getContext() {
        return mContext;
    }
}
