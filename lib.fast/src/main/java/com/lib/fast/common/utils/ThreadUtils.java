package com.lib.fast.common.utils;

import android.os.Build;
import android.os.HandlerThread;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程工具类
 */
public class ThreadUtils {

    /**
     * 安全退出HandlerThread
     */
    public static void quitSafely(HandlerThread thread) {
        if (thread != null && thread.isAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                thread.quitSafely();
            } else {
                thread.quit();
            }
        }
    }

    /**执行retorfit框架下的请求*/
    public static <T> void excuteRetorfitRequest(Observable<T> observable, Observer<T> observer){
        if(observable == null)return;
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
