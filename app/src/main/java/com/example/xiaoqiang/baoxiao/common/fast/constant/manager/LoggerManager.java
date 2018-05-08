package com.example.xiaoqiang.baoxiao.common.fast.constant.manager;

import android.text.TextUtils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Function: logger日志管理类
 */
public abstract class LoggerManager {
    private static final int CHUNK_SIZE = 200;
    private static String TAG;
    private static boolean DEBUG = true;
    private static final int LOG_D = 0;
    private static final int LOG_E = 1;
    private static final int LOG_W = 2;
    private static final int LOG_I = 3;
    private static final int LOG_V = 4;
    private static final int LOG_JSON = 5;
    private static final int LOG_XML = 6;

    public static void init(String Tag) {
        init(Tag, true);
    }

    public static void init(String tag, final boolean isDebug) {
        LoggerManager.TAG = tag;
        setDebug(isDebug);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(3)
                .tag(TAG) // 全局tag
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isDebug;
            }
        });
    }

    public static void setDebug(boolean isDebug) {
        LoggerManager.DEBUG = isDebug;
    }

    public static void d(Object msg) {
        d(null, msg);
    }

    public static void d(String tag, Object msg) {
        if (isInit() && DEBUG) {
            Logger.t(tag).d(msg);
//            logContent(LOG_D, tag, msg);
        }
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).e(msg);
            logContent(LOG_E, tag, msg);
        }
    }

    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).w(msg);
            logContent(LOG_W, tag, msg);
        }
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).i(msg);
            logContent(LOG_I, tag, msg);
        }

    }

    public static void v(String message) {
        v(null, message);
    }

    public static void v(String tag, String message) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).v(message);
            logContent(LOG_V, tag, message);
        }
    }

    public static void json(String json) {
        json(null, json);
    }

    public static void json(String tag, String json) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).json(json);
            logContent(LOG_JSON, tag, json);
        }
    }

    public static void xml(String xml) {
        xml(null, xml);
    }

    public static void xml(String tag, String xml) {
        if (isInit() && DEBUG) {
//            Logger.t(tag).xml(xml);
            logContent(LOG_XML, tag, xml);
        }
    }

    private static void logContent(int logType, String tag, String msg) {
        if (logType == LOG_D) {
            Logger.t(tag).d(msg);
        } else if (logType == LOG_E) {
            Logger.t(tag).e(msg);
        } else if (logType == LOG_W) {
            Logger.t(tag).w(msg);
        } else if (logType == LOG_I) {
            Logger.t(tag).i(msg);
        } else if (logType == LOG_V) {
            Logger.t(tag).v(msg);
        } else if (logType == LOG_JSON) {
            Logger.t(tag).json(msg);
        } else if (logType == LOG_XML) {
            Logger.t(tag).xml(msg);
        }
    }

    private static boolean isInit() {
        if (TextUtils.isEmpty(TAG)) {
            init("LoggerManager");
        }
        return true;
    }
}
