package com.example.xiaoqiang.baoxiao.common.fast.constant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.been.StateUser;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.FastConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.constant.SPConstant;
import com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog.LoadingDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SpManager {
    private static SpManager manager;
    private static SharedPreferences preferences;
    protected final String USERINFO = "UserInfo";
    public static final String No_FIRST = "no_first";
    public static Map<Integer, String> mPositionManager;
    public static Map<Integer, String> mBumenManager;
    public static List<String> mVehicleData;
    public static List<String> mAccountTypeData;
    public static List<String> mPositionData;
    public static List<String> mBumenData;
    private IGetCurrentUser iGetCurrentUser = null;

    static {
        mPositionManager = new HashMap<>();
        mPositionManager.put(0, "普通职员");
        mPositionManager.put(1, "财务职员");
        mPositionManager.put(2, "出纳");
        mPositionManager.put(3, "部门主管");
        mPositionManager.put(4, "财务主管");
        mPositionManager.put(5, "总经理");
        mBumenManager = new HashMap<>();


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
        mPositionData = new ArrayList<>();
        mPositionData.add("普通职员");
        mPositionData.add("财务职员");
        mPositionData.add("出纳");
        mPositionData.add("部门主管");
        mPositionData.add("财务主管");
        mPositionData.add("总经理");
        mBumenData = new ArrayList<>();
        mBumenData.add("销售部门");
        mBumenData.add("人事部门");
        mBumenData.add("财务部门");
        mBumenData.add("设计部门");
        mBumenData.add("技术部门");
        mBumenData.add("生产部门");
        mBumenData.add("其他部门");
        for (int i = 0; i < mBumenData.size(); i++) {
            mBumenManager.put(i, mBumenData.get(i));
        }
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

    public void saveUserInfo(StateUser user) {
        if (user == null) {
            Timber.i("bmob:user is null");
            return;
        }
        String userInfo = new Gson().toJson(user);
        save(USERINFO, userInfo);
    }

    public StateUser getUserInfo() {
        String userInfo = getString(USERINFO);
        if (TextUtils.isEmpty(userInfo)) {
            Timber.i("bmob:user is null");
            return null;
        }

        return new Gson().fromJson(userInfo, StateUser.class);
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


    public void queryCurrentUser(Context context, MyUser user, IGetCurrentUser iGetCurrentUser) {
        this.iGetCurrentUser = iGetCurrentUser;
//        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user == null) {
            showUser(null);
            return;
        }
        showLoadingDialog(context);
        queryUserInfo(user);
    }

    private void queryUserInfo(MyUser user) {
        BmobQuery<StateUser> query = new BmobQuery<StateUser>();
        query.addWhereEqualTo("user", user);
        query.include("user,company");
        query.findObjects(new FindListener<StateUser>() {
            @Override
            public void done(List<StateUser> list, BmobException e) {
                dissmissLoadingDialog();

                if (e == null) {
                    if (list != null && list.size() > 0) {
                        saveUserInfo(list.get(0));
                        showUser(list.get(0));
                        Timber.i("bmob" + new Gson().toJson(list.get(0)));
                    }
                } else {
                    showUser(null);
                    Timber.e("bmob 失败：" + e.getMessage() + " ---");
                }

            }
        });
    }

    private void showUser(StateUser user) {
        if (iGetCurrentUser != null) {
            iGetCurrentUser.showMyUser(user);
            iGetCurrentUser = null;
        }
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

    /**
     * 获取下一个节点
     */
    public Integer getNextPoint(Integer point, Integer proccesType) {
        Map<Integer, Integer> nextMap = new HashMap<>();
        nextMap.put(FastConstant.PROCESS_POINT_ONE, FastConstant.PROCESS_POINT_TWO);
        nextMap.put(FastConstant.PROCESS_POINT_TWO, FastConstant.PROCESS_POINT_THREE);
        nextMap.put(FastConstant.PROCESS_POINT_THREE, FastConstant.PROCESS_POINT_FOUR);
        nextMap.put(FastConstant.PROCESS_POINT_FOUR, FastConstant.PROCESS_POINT_FINISH);
        Integer nextPoint = nextMap.get(point);
        if (proccesType == FastConstant.PROCESS_TYPE_TWO || proccesType == FastConstant.PROCESS_TYPE_THREE) {
            //需要跳过四级
            if (nextPoint == FastConstant.PROCESS_POINT_FOUR) {
                nextPoint = FastConstant.PROCESS_POINT_FINISH;
            }
        }
        return nextPoint;
    }

    /**
     * 获取当前节点信息
     */
    public String getPointInfo(Integer point, Integer proccesType) {
        String info = null;
        Map<Integer, String> nextMap1 = new HashMap<>();
        nextMap1.put(FastConstant.PROCESS_POINT_ONE, "自己");
        nextMap1.put(FastConstant.PROCESS_POINT_TWO, "部门主管");
        nextMap1.put(FastConstant.PROCESS_POINT_THREE, "总经理");
        nextMap1.put(FastConstant.PROCESS_POINT_FOUR, "财务结算");
        nextMap1.put(FastConstant.PROCESS_POINT_FINISH, "归档");
        Map<Integer, String> nextMap2 = new HashMap<>();
        nextMap2.put(FastConstant.PROCESS_POINT_ONE, "自己");
        nextMap2.put(FastConstant.PROCESS_POINT_TWO, "总经理");
        nextMap2.put(FastConstant.PROCESS_POINT_THREE, "财务结算");
        nextMap2.put(FastConstant.PROCESS_POINT_FINISH, "归档");
        Map<Integer, String> nextMap3 = new HashMap<>();
        nextMap3.put(FastConstant.PROCESS_POINT_ONE, "自己");
        nextMap3.put(FastConstant.PROCESS_POINT_TWO, "财务主管");
        nextMap3.put(FastConstant.PROCESS_POINT_THREE, "财务结算");
        nextMap3.put(FastConstant.PROCESS_POINT_FINISH, "归档");
        if (proccesType == FastConstant.PROCESS_TYPE_ONE) {
            info = nextMap1.get(point);
        } else if (proccesType == FastConstant.PROCESS_TYPE_TWO) {
            info = nextMap2.get(point);
        } else if (proccesType == FastConstant.PROCESS_TYPE_THREE) {
            info = nextMap3.get(point);
        }
        return info;
    }

    private LoadingDialog loadingDialog;

    public void showLoadingDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dissmissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public interface IGetCurrentUser {
        void showMyUser(StateUser user);
    }
}
