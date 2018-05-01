package com.example.xiaoqiang.baoxiao.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NoScollGridView extends GridView {
    public NoScollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScollGridView(Context context) {
        super(context);
    }

    public NoScollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
