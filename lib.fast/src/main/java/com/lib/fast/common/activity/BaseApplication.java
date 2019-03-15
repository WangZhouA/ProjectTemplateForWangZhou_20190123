package com.lib.fast.common.activity;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.bugtags.library.Bugtags;
import com.lib.fast.common.activity.callback.ActivityLifeListener;
import com.lib.fast.common.cache.ACache;
import com.lib.fast.common.config.IBuildConfig;
import com.lib.fast.common.http.HttpFactory;
import com.lib.fast.common.xlog.LOG;
import com.squareup.leakcanary.LeakCanary;

import okhttp3.Interceptor;


/**
 * Created by siwei on 2015/11/2.
 */
public abstract class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    private ActivityLifeListener mLifeListener;

    private static BaseApplication instance;

    public synchronized static BaseApplication getInstance() {
        return instance;
    }

    private static final String TAG = "BaseApplication";

    /**
     * 返回app配置项
     */
    public abstract @NonNull
    IBuildConfig getBuildConfig();

    @Override
    public void onCreate() {
        super.onCreate();
        onAppInit();


    }

    public static Context getContext(){
        return instance;
    }

    //App进来进行的一些初始化操作
    private void onAppInit() {
        instance = this;
        LOG.initXlog(this, getBuildConfig().isDebug(), getBuildConfig().getAppName(this));

        //网络框架初始化
        HttpFactory.initFactory(this, getBuildConfig().getHttpBaseUrl(), initOkHttpIntercept());

        //Activity生命周期监听
        mLifeListener = new ActivityLifeListener();
        registerActivityLifecycleCallbacks(mLifeListener);

        //异常捕获
        Thread.setDefaultUncaughtExceptionHandler(this);

        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 返回Okhttp的拦截器
     */
    protected Interceptor[] initOkHttpIntercept() {
        return null;
    }


    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public ACache getCache() {
        return ACache.get(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!getBuildConfig().isDebug()) {
            System.exit(0);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mLifeListener);
        mLifeListener = null;
    }

    /**
     * 退出APP
     */
    public void exitApp() {
        if (mLifeListener != null) {
            mLifeListener.exitApp();
        }
    }
}
