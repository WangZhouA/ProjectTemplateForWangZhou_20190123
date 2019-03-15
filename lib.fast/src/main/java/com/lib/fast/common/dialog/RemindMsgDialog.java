package com.lib.fast.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.lib.fast.common.R;


/**
 * 只显示提醒消息的弹窗
 */
public class RemindMsgDialog extends RemindDialog {

    TextView mTitleTv;

    TextView mMsgTv;

    public RemindMsgDialog(@NonNull Context context) {
        super(context);
    }

    public RemindMsgDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RemindMsgDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mTitleTv = view.findViewById(R.id.title_tv);
        mMsgTv = view.findViewById(R.id.msg_tv);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.remind_msg_dialog);
    }

    public RemindMsgDialog setTitleText(CharSequence text){
        mTitleTv.setText(text);
        return this;
    }

    public RemindMsgDialog setMsgText(CharSequence text){
        mMsgTv.setText(text);
        return this;
    }

    public RemindMsgDialog showTitle(){
        return setViewVisibility(mTitleTv, View.VISIBLE);
    }

    public RemindMsgDialog hidenTitle(){
        return setViewVisibility(mTitleTv, View.GONE);
    }

    public RemindDialog showMsg(){
        return setViewVisibility(mMsgTv, View.VISIBLE);
    }

    public RemindMsgDialog hidenMsg(){
        return setViewVisibility(mMsgTv, View.GONE);
    }
}
