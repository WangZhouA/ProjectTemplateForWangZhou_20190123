package com.lib.fast.common.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * Created by siwei on 2018/3/17.
 * 有选中状态的TextView
 */
@SuppressLint("AppCompatCustomView")
public class CheckTextView extends TextView implements Checkable {

    boolean isChecked = false;
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

    public CheckTextView(Context context) {
        super(context);
    }

    public CheckTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CheckTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if(isChecked){
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean b) {
        if(isChecked != b){
            isChecked = b;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
