package com.lib.fast.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lib.fast.common.R;
import com.lib.fast.common.utils.UiUtil;


/**
 * 密码输入或文本输入弹窗
 */
public class InputPwdDialog extends RemindDialog {

    TextView mTitleTv;

    TextView mMsgTv;

    EditText mPwdEt;

    public InputPwdDialog(@NonNull Context context) {
        super(context);
    }

    public InputPwdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected InputPwdDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mTitleTv = view.findViewById(R.id.title_tv);
        mMsgTv = view.findViewById(R.id.msg_tv);
        mPwdEt = view.findViewById(R.id.pwd_et);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.input_pwd_dialog);
    }

    public InputPwdDialog setTitleText(String title){
        mTitleTv.setText(title);
        return this;
    }

    public InputPwdDialog setMsgText(String msg){
        mMsgTv.setText(msg);
        return this;
    }

    public InputPwdDialog showTitle(){
        return setViewVisibility(mTitleTv, View.VISIBLE);
    }

    public InputPwdDialog hidenTitle(){
        return setViewVisibility(mTitleTv, View.GONE);
    }

    public InputPwdDialog showMsg(){
        return setViewVisibility(mMsgTv, View.VISIBLE);
    }

    public InputPwdDialog hidenMsg(){
        return setViewVisibility(mMsgTv, View.GONE);
    }

    public InputPwdDialog setInputMaxLength(int maxLength) {
        UiUtil.setTextMaxLength(mPwdEt, maxLength);
        return this;
    }

    public EditText getEditText() {
        return mPwdEt;
    }

    public InputPwdDialog setInputHintText(String hintText){
        mPwdEt.setHint(hintText);
        return this;
    }

    public InputPwdDialog setInputType(int inputType){
        mPwdEt.setInputType(inputType);
        return this;
    }

    public InputPwdDialog setInputText(String inputText){
        if (inputText == null) inputText = "";
        mPwdEt.setText(inputText);
        mPwdEt.setSelection(inputText.length());
        return this;
    }

    /**获取输入框输入的密码*/
    public String getInputText(){
        if(mPwdEt != null){
            return mPwdEt.getText().toString();
        }
        return "";
    }
}
