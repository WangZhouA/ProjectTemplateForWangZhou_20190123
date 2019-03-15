package com.lib.fast.common.activity.view;

import android.app.Activity;
import android.view.View;

/**
 * created by siwei on 2018/12/20
 */
public class SimpleNavBarOnClickListener implements NavBar.NavBarOnClickListener {

    private Activity mActivity;

    public SimpleNavBarOnClickListener(Activity activity) {
        mActivity = activity;
    }

    public SimpleNavBarOnClickListener() {
    }

    @Override
    public void onLeftIconClick(View view) {
        //默认左边图标是退出按钮,点击界面就退出了
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    @Override
    public void onLeftSenIconClick(View view) {

    }

    @Override
    public void onRightIconClick(View view) {

    }

    @Override
    public void onRightTxtClick(View view) {

    }
}
