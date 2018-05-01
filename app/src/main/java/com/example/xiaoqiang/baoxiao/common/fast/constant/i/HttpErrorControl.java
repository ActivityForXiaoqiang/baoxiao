package com.example.xiaoqiang.baoxiao.common.fast.constant.i;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * Function: 用于全局设置网络请求错误回调处理
 */
public interface HttpErrorControl {

    /**
     * {@link FastObserver#_onError(int, int, Throwable)}
     *
     * @param errorRes  错误类型library里处理的错误Res资源
     * @param errorCode 错误码{@link FastError}
     * @param e         Throwable 异常
     * @param context
     * @param args      初始化 {@link FastObserver#FastObserver(Context, Object...)}用作全局处理如
     * @return 返回true表示不再将结果传递_onError(reason, code, e)
     */
    boolean createHttpErrorControl(int errorRes, int errorCode, @NonNull Throwable e, Context context, Object... args);
}
