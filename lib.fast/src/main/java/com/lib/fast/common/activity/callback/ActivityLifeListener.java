package com.lib.fast.common.activity.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * Created by siwei on 2018/3/14.
 */

public class ActivityLifeListener implements Application.ActivityLifecycleCallbacks {


    private LinkedList<Activity> mActivities = new LinkedList<>();

    /**退出APP*/
    public void exitApp() {
        synchronized (mActivities) {
            for (Activity activity : mActivities) {
                activity.finish();
            }
            mActivities.clear();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }

}
