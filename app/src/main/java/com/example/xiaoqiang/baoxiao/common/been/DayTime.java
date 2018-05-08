package com.example.xiaoqiang.baoxiao.common.been;

import cn.bmob.v3.datatype.BmobDate;

/**
 * author : yhx
 * time   : 2018/5/8
 * desc   :
 */

public class DayTime {
//    private Long minTime;
//    private Long maxTime;
    private BmobDate minDate;
    private BmobDate maxDate;

    public BmobDate getMinDate() {
        return minDate;
    }

    public void setMinDate(BmobDate minDate) {
        this.minDate = minDate;
    }

    public BmobDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(BmobDate maxDate) {
        this.maxDate = maxDate;
    }
}
