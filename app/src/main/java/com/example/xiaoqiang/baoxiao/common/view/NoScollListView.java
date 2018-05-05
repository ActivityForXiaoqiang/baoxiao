package com.example.xiaoqiang.baoxiao.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/4/13.
 */
public class NoScollListView extends ListView {
    public NoScollListView(Context context) {
        super(context);
    }
    public NoScollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
