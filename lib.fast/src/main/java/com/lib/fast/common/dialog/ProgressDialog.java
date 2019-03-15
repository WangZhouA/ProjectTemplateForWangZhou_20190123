package com.lib.fast.common.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lib.fast.common.R;

/**
 * created by siwei on 2018/8/28
 */
public class ProgressDialog extends RemindDialog {

    private TextView mLeftTv, mRightTv;
    private ProgressBar mPb;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.progress_dialog);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mLeftTv = view.findViewById(R.id.left_tv);
        mRightTv = view.findViewById(R.id.right_tv);
        mPb = view.findViewById(R.id.pb);
    }

    public ProgressDialog setLeftTextColor(@ColorInt int color){
        mLeftTv.setTextColor(color);
        return this;
    }

    public ProgressDialog setLeftText(String text){
        mLeftTv.setText(text);
        return this;
    }

    public ProgressDialog setRightTextColor(@ColorInt int color){
        mRightTv.setTextColor(color);
        return this;
    }

    public ProgressDialog setRightText(String text){
        mRightTv.setText(text);
        return this;
    }

    public ProgressDialog setProgress(int progress){
        mPb.setProgress(progress);
        return this;
    }

    public ProgressDialog setMax(int max){
        mPb.setMax(max);
        return this;
    }

}
