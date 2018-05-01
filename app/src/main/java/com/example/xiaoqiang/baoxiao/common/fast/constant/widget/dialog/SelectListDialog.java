package com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.DisplayUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author : yhx
 * time   : 2018/4/26
 * desc   :选择框
 */

public class SelectListDialog extends BaseDialog {
    @BindView(R.id.dialog_select_list)
    ListView listView;
    @BindView(R.id.dialog_select_list_title)
    TextView mTvTitle;
    private ArrayList<String> contents;
    private boolean mCanBackClose = true;
    private boolean mCancelable = true;

    private ItemSelect mItemSelect;

    private AlertDialog.OnDismissListener mOnDismissListener;

    private Context mContext;
    private String title;
    private String mSelectId;

    public SelectListDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public SelectListDialog(@NonNull Context context, ArrayList<String> contents, String title) {
        super(context);
        this.mContext = context;
        this.contents = contents;
        this.title = title;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_select_list;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mCanBackClose) {
                        dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        initHeight(v);
        if (TextUtils.isEmpty(title)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setText(title);
        }
        listView.setAdapter(new SelectListAdapter(mContext, contents));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                mItemSelect.onItemSelect(contents.get(position));
            }
        });
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss();
                }
            }
        });

    }

    private void initHeight(View view) {
        //这种设置宽高的方式也是好使的！！！-- show 前调用，show 后调用都可以！！！
        //设置dialog最大高度
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                int contentHeight = v.getHeight();

                int maxHeight = (int) (DisplayUtil.getWindowHeight((Activity) mContext) * 0.75);

                if (contentHeight > maxHeight) {
                    //注意：这里的 LayoutParams 必须是 FrameLayout的！！
                    v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            maxHeight));
                }
            }
        });
    }

    @Override
    public int getWindowAnimations() {
        return R.style.AlertDialog_AnimationStyle;
    }


    @Override
    public boolean getCancelOutside() {
        return mCancelable;
    }

    @Override
    public int getWidth() {
        return (int) (DisplayUtil.getWindowWidth((Activity) mContext) * 0.75);
    }

    public SelectListDialog setItemSelectListener(ItemSelect listener) {
        mItemSelect = listener;
        return this;
    }

    public SelectListDialog setOnDismissListener(AlertDialog.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    public SelectListDialog setCanBackClose(boolean canBackClose) {
        mCanBackClose = canBackClose;
        return this;
    }

    public SelectListDialog setmCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
        return this;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface ItemSelect {
        void onItemSelect(String content);
    }

    public class SelectListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> contents;

        public SelectListAdapter(Context mContext, ArrayList<String> contents) {
            this.mContext = mContext;
            this.contents = contents;
        }

        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public Object getItem(int position) {
            return contents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(this.mContext, R.layout.item_select_list, null);
                vh.mTvContent = convertView.findViewById(R.id.item_select_list_content);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTvContent.setText(contents.get(position));
            return convertView;
        }

        private class ViewHolder {
            private TextView mTvContent;
        }
    }
}
