package com.example.xiaoqiang.baoxiao.common.fast.constant.constant;


/**
 * Function: 全局常量
 */
public class FastConstant {
    public final static String EXCEPTION_NOT_INIT = "You've to call static method init() first in Application";
    public final static String EXCEPTION_EMPTY_URL = "You've configured an invalid url";
    public final static String EXCEPTION_FAST_CONFIG_CONTEXT_NOT_NULL = "You've set a null context";
    public final static String EXCEPTION_SWIPE_BACK_APPLICATION_NOT_NULL = "You've set a null application";
    public final static String EXCEPTION_ACTIVITY_NOT_NULL = "You've set a null activity";
    public final static String EXCEPTION_ACTIVITY_NOT_EXIST = "Activity is null or finishing";
    public final static String TIME_FORMAT_TYPE = "yyyy-MM-dd HH:mm";
    public final static int SELECT_DIALOG_USER_MODE = 0;//人员列表
    public final static int SELECT_DIALOG_VEHICLE_MODE = 1;//交通工具列表
    public final static int SELECT_DIALOG_RELATION_ACCOUNT_MODE = 2;//关联账号
    public final static int SELECT_DIALOG_ZHIWEI = 3;//职位列表
    public final static int SELECT_DIALOG_DEPARTMENT = 4;


    public final static int PROCESS_TYPE_ONE = 1;//流程类型1    012→3、4→5→2     一到四级到归档
    public final static int PROCESS_TYPE_TWO = 2;//流程类型2      3、4→5→2        一到三级到归档
    public final static int PROCESS_TYPE_THREE = 3;//流程类型3      5→4→2         一到三级到归档

    public final static int PROCESS_POINT_ONE = 101;//流程一級節點
    public final static int PROCESS_POINT_TWO = 201;//流程二級節點
    public final static int PROCESS_POINT_THREE = 301;//流程三級節點
    public final static int PROCESS_POINT_FOUR = 401;//流程四級節點
    public final static int PROCESS_POINT_FINISH = 501;//流程完成状态

    public final static int POINT_STATUS_ONE = 1;//未操作
    public final static int POINT_STATUS_TWO = 2;//已操作
}
