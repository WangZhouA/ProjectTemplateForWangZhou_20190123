package com.lib.fast.common.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.lib.fast.common.R;
import com.lib.fast.common.utils.UiUtil;


/**
 * 有确定或取消的弹窗
 */
public class RemindDialog extends BaseDialog implements View.OnClickListener{

    /**取消按钮*/
    public static final int WHICH_CANCLE = 10;

    /**确定按钮*/
    public static final int WHICH_COMPLATE = 11;

    private FrameLayout mConstansyFl;

    private View mDialogView;

    TextView mCancleTv;

    TextView mComplateTv;

    View mLine;

    View mTopLine;

    public RemindDialog(@NonNull Context context) {
        super(context, R.style.dialog);
    }

    public RemindDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog);
    }

    protected RemindDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        mDialogView = View.inflate(getContext(), R.layout.remind_dialog_layout, null);
        mConstansyFl = mDialogView.findViewById(R.id.content_fl);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mCancleTv = view.findViewById(R.id.cancle_tv);
        mComplateTv = view.findViewById(R.id.complate_tv);
        mLine = view.findViewById(R.id.line);
        mTopLine = view.findViewById(R.id.topline);
        mCancleTv.setOnClickListener(this);
        mComplateTv.setOnClickListener(this);
    }

    @Override
    public void setContentView(@NonNull View view) {
        mConstansyFl.addView(view);
        super.setContentView(mDialogView);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(getContext(), layoutResID, null);
        mConstansyFl.addView(view);
        super.setContentView(mDialogView);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        mConstansyFl.addView(view, params);
        super.setContentView(mDialogView);
    }

    public <T extends RemindDialog> T showComplate() {
        UiUtil.setVisibility(mComplateTv, View.VISIBLE);
        UiUtil.setVisibility(mLine, View.VISIBLE);
        return (T) this;
    }

    public <T extends RemindDialog> T hidenComplate() {
        UiUtil.setVisibility(mComplateTv, View.GONE);
        UiUtil.setVisibility(mLine, View.GONE);
        if(mCancleTv.getVisibility() == View.GONE){
            UiUtil.setVisibility(mTopLine, View.GONE);
        }
        return (T) this;
    }

    public <T extends RemindDialog> T showCancle() {
        UiUtil.setVisibility(mCancleTv, View.VISIBLE);
        UiUtil.setVisibility(mLine, View.VISIBLE);
        return (T) this;
    }

    public <T extends RemindDialog> T hidenCancle() {
        UiUtil.setVisibility(mCancleTv, View.GONE);
        UiUtil.setVisibility(mLine, View.GONE);
        if(mComplateTv.getVisibility() == View.GONE){
            UiUtil.setVisibility(mTopLine, View.GONE);
        }
        return (T) this;
    }

    public <T extends RemindDialog> T setComplateText(String text) {
        mComplateTv.setText(text);
        return (T) this;
    }

    public <T extends RemindDialog> T setCancleText(String text) {
        mCancleTv.setText(text);
        return (T) this;
    }

    public <T extends RemindDialog> T setCancleTextColor(@ColorInt int color) {
        mCancleTv.setTextColor(color);
        return (T) this;
    }

    public <T extends RemindDialog> T setCancleTextColorRes(@ColorRes int colorRes) {
        mCancleTv.setTextColor(getContext().getResources().getColor(colorRes));
        return (T) this;
    }

    public <T extends RemindDialog> T setComplateTextColor(@ColorInt int color) {
        mComplateTv.setTextColor(color);
        return (T) this;
    }

    public <T extends RemindDialog> T setComplateTextColorRes(@ColorRes int colorRes) {
        mComplateTv.setTextColor(getContext().getResources().getColor(colorRes));
        return (T) this;
    }

    protected <T extends RemindDialog> T setViewVisibility(View view, int visibility){
        UiUtil.setVisibility(view, visibility);
        return (T) this;
    }


    @Override
    public void onClick(View v) {
        dismiss();
        int i = v.getId();
        if (i == R.id.complate_tv) {
            onWhichOneClick(WHICH_COMPLATE);
        } else if (i == R.id.cancle_tv) {
            onWhichOneClick(WHICH_CANCLE);
        }
    }
}
