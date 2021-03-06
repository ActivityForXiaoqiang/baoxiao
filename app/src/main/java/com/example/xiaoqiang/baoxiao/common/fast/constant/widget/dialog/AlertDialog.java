package com.example.xiaoqiang.baoxiao.common.fast.constant.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.fast.constant.util.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author : yhx
 * time   : 2018/4/26
 * desc   : AlertDialog
 */
public class AlertDialog extends BaseDialog {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_content)
    TextView mTvContent;

    @BindView(R.id.tv_positive)
    TextView mTvPositive;

    @BindView(R.id.tv_negative)
    TextView mTvNegative;

    @BindView(R.id.btn_divider)
    View mBtnDivider;

    /**
     * 标题属性
     */
    private String mTitle;
    private int mTitleGravity = -1;
    private boolean mTitleBold = true;

    private String mContent;
    private int mContentGravity = -1;

    private String mNegativeText;

    private String mPositiveText;

    private boolean mCanBackClose = true;
    private int mContentSize = -1;
    private View.OnClickListener mNegativeListener;

    private View.OnClickListener mPositiveListener;

    private OnDismissListener mOnDismissListener;

    private Context mContext;

    public AlertDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_alert;
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

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss();
                }
            }
        });

        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
            mTvTitle.setVisibility(View.VISIBLE);
            if (mTitleGravity != -1) {
                mTvTitle.setGravity(mTitleGravity);
                // 标题据左，重新设置边距
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvTitle.getLayoutParams();
                layoutParams.setMargins(DisplayUtil.dip2px(mContext, 20), DisplayUtil.dip2px(mContext, 20),
                        0, DisplayUtil.dip2px(mContext, 12));
                mTvTitle.setLayoutParams(layoutParams);
            }
            mTvTitle.getPaint().setFakeBoldText(mTitleBold);// 是否加粗标题
        } else {
            mTvTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mContent)) {
            mTvContent.setVisibility(View.VISIBLE);
            if (mContentSize != -1) {
                mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContentSize);
            }
            if (mContentGravity != -1) {
                mTvContent.setGravity(mContentGravity);
            }
            mTvContent.setText(mContent);
        } else {
            mTvContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mNegativeText)) {
            mTvNegative.setText(mNegativeText);
            mTvNegative.setVisibility(View.VISIBLE);
            mBtnDivider.setVisibility(View.VISIBLE);
        } else {
            mTvNegative.setVisibility(View.GONE);
            mBtnDivider.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mPositiveText)) {
            mTvPositive.setText(mPositiveText);
        }
    }

    @Override
    public int getWindowAnimations() {
        return R.style.AlertDialog_AnimationStyle;
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public int getWidth() {
        return (int) (DisplayUtil.getWindowWidth((Activity) mContext) * 0.75);
    }

    @OnClick({R.id.tv_negative, R.id.tv_positive})
    void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_negative:
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(view);
                }
                break;
            case R.id.tv_positive:
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(view);
                }
                break;
        }
    }


    /*@Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }*/

    public AlertDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public AlertDialog setTitleGravity(int gravity) {
        mTitleGravity = gravity;
        return this;
    }

    public AlertDialog setTitleTextStyle(boolean bold) {
        mTitleBold = bold;
        return this;
    }

    public AlertDialog setContent(String content) {
        mContent = content;
        return this;
    }

    public AlertDialog setContentGravity(int gravity) {
        mContentGravity = gravity;
        return this;
    }

    public AlertDialog setContentSize(int size) {
        mContentSize = size;
        return this;
    }

    public AlertDialog setNegativeText(String negativeText) {
        mNegativeText = negativeText;
        return this;
    }

    public AlertDialog setPositiveText(String positiveText) {
        mPositiveText = positiveText;
        return this;
    }

    public AlertDialog setNegativeListener(View.OnClickListener listener) {
        mNegativeListener = listener;
        return this;
    }

    public AlertDialog setPositiveListener(View.OnClickListener listener) {
        mPositiveListener = listener;
        return this;
    }

    public AlertDialog setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    public AlertDialog setCanBackClose(boolean canBackClose) {
        mCanBackClose = canBackClose;
        return this;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
