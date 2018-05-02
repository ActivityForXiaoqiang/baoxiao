package com.example.xiaoqiang.baoxiao.common.fast.constant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpManager {
    private static SpManager manager;
    private static SharedPreferences preferences;
    protected final String userInfo = "UserInfo";
    public static final String No_FIRST = "no_first";
    public static Map<String, String> mPositionManager;
    public static List<String> mVehicleData;
    public static List<String> mAccountTypeData;

    static {
        mPositionManager = new HashMap<>();
        mPositionManager.put("0", "普通职员");
        mPositionManager.put("1", "财务职员");
        mPositionManager.put("2", "出纳");
        mPositionManager.put("3", "部门主管");
        mPositionManager.put("4", "财务主管");
        mPositionManager.put("5", "总经理");
        mVehicleData = new ArrayList<>();
        mVehicleData.add("飞机");
        mVehicleData.add("高铁");
        mVehicleData.add("火车");
        mVehicleData.add("船渡");
        mVehicleData.add("游艇·帆船");
        mVehicleData.add("巴士");
        mAccountTypeData = new ArrayList<>();
        mAccountTypeData.add("支付宝");
        mAccountTypeData.add("微信");
    }

    public static SpManager getInstance() {
        if (manager == null) {
            synchronized (SpManager.class) {
                if (manager == null) {
                    manager = new SpManager();
                }
            }
        }
        return manager;
    }

    public void saveInfo(String value) {
        Editor editor = preferences.edit();
        editor.putString(userInfo, value);
        editor.commit();
    }

    public String getInfo() {
        return preferences.getString(userInfo, null);
    }

    public void initSpManager(Context context) {
        preferences = context.getSharedPreferences(SPConstant.SP_FILE_NAME,
                Context.MODE_PRIVATE);
    }

    public void save(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void save(String key, Integer value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public Integer getInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean getBooleandDefaultTrue(String key) {
        return preferences.getBoolean(key, true);
    }

    public void removeByKey(String key) {
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearAll() {
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        editor.putBoolean(No_FIRST, true);
        editor.commit();
    }


    /**
     * 存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param object
     * @return
     */
    public static boolean put(Context context, String fileName, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, ((Integer) object).intValue());
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Float) {
            editor.putFloat(key, ((Float) object).floatValue());
        } else if (object instanceof Long) {
            editor.putLong(key, ((Long) object).longValue());
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putStringSet(key, (Set<String>) object);
        }
        return editor.commit();
    }

    /**
     * 获取存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param def
     * @return
     */
    public static Object get(Context context, String fileName, String key, Object def) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (def instanceof String) {
            return sp.getString(key, def.toString());
        } else if (def instanceof Integer) {
            return sp.getInt(key, ((Integer) def).intValue());
        } else if (def instanceof Boolean) {
            return sp.getBoolean(key, ((Boolean) def).booleanValue());
        } else if (def instanceof Float) {
            return sp.getFloat(key, ((Float) def).floatValue());
        } else if (def instanceof Long) {
            return sp.getLong(key, ((Long) def).longValue());
        } else if (def instanceof Set) {
            return sp.getStringSet(key, (Set<String>) def);
        }
        return def;
    }
}
