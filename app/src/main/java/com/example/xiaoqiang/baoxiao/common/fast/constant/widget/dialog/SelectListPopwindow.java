package com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.adapter.SelectListAdapter;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.DisplayUtil;

import java.util.List;

/**
 * author : yhx
 * time   : 2018/5/17
 * desc   :
 */

public class SelectListPopwindow extends PopupWindow {
    private View mMenuView;
    private Context context;
    private int scrheight;
    private ListView mMyListView;
    private List<String> items;
    private SelectListAdapter mAdapter;
    private LinearLayout arrowTopL;
    private OnMyPopListener onMyPopListener;

    public SelectListPopwindow(Activity con, List<String> data, int scrheight, OnMyPopListener onMyPopListener) {
        super(con);
        this.context = con;
        this.items = data;
        this.scrheight = scrheight / 2;
        this.onMyPopListener = onMyPopListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = View.inflate(context, R.layout.pop_select_list, null);
        arrowTopL = mMenuView.findViewById(R.id.layout_arrow_top_l);
        mAdapter = new SelectListAdapter(context, items);
        mMyListView = mMenuView.findViewById(R.id.pop_select_listview);

        this.setContentView(mMenuView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        if (items.size() > 7) {
            ViewGroup.LayoutParams layoutParams = mMyListView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.scrheight;
            mMyListView.setLayoutParams(layoutParams);
//            this.setHeight(scrheight);
        } else {
        }

        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimalIntOut);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setOutsideTouchable(true);

        mMyListView.setAdapter(mAdapter);
        mMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                SelectListPopwindow.this.onMyPopListener.onItemClick(i, items.get(i));
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                SelectListPopwindow.this.onMyPopListener.onDismiss();
            }
        });
        this.update();
    }

    @Override
    public void showAsDropDown(View anchor) {
        //获取点击控件在屏幕中的水平位置
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        int x = location[0];
        super.showAsDropDown(anchor);
        //让小三角位于点击控件的中间
        x += anchor.getWidth() / 2;
        x -= DisplayUtil.dip2px(context, 10);
        arrowTopL.setPadding(x, 0, 0, 0);
        super.showAsDropDown(anchor);
    }

    public interface OnMyPopListener {
        void onItemClick(int position, String content);

        void onDismiss();
    }
}
