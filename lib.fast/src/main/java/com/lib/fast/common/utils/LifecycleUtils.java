package com.lib.fast.common.utils;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * created by siwei on 2018/10/8
 */
public class LifecycleUtils {

    /**获取变量的生命周期,如果没有就返回null*/
    public static Lifecycle getLifecycle(Object object) {
        Lifecycle lifecycle = null;
        if (object == null) return lifecycle;
        if (object instanceof AppCompatActivity) {
            lifecycle = getLifecycleFromAppCompactActivity(object);
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            lifecycle = fragment.getLifecycle();
        } else if (object instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) object;
            Activity activity = fragment.getActivity();
            lifecycle = getLifecycleFromAppCompactActivity(activity);
        } else if (object instanceof View) {
            View view = (View) object;
            Context context = view.getContext();
            lifecycle = getLifecycleFromAppCompactActivity(context);
        }
        return lifecycle;
    }

    private static Lifecycle getLifecycleFromAppCompactActivity(Object o) {
        if (o != null && o instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) o;
            return appCompatActivity.getLifecycle();
        }
        return null;
    }
}
