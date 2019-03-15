package com.lib.fast.common.mvp;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.lib.fast.common.activity.BaseApplication;
import com.lib.fast.common.cache.ACache;
import com.lib.fast.common.http.RetorfitServices;
import com.lib.fast.common.utils.LifecycleUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by siwei on 2018/3/13.
 * Model层封装，后续会封装一些数据访问的工具类在其中，方便数据层去访问数据
 */
public class ModelImpl extends ViewModel implements IModel, LifecycleObserver {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context mContext;

    public ModelImpl(Context context) {
        mContext = context;
        if (mContext != null && mContext instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) mContext;
            appCompatActivity.getLifecycle().addObserver(this);
        }
    }

    protected Context getContext() {
        return mContext;
    }

    protected <T> T getRetorfitService(Class<T> service) {
        return RetorfitServices.getService(service);
    }

    protected <T> void excuteRetorfitRequest(Observable<T> observable, Observer<T> observer) {
        if (observable == null) return;
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    /**
     * add Disposable
     */
    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    /**
     * add Disposable
     */
    protected void addAllDisposable(Disposable... disposables) {
        compositeDisposable.addAll(disposables);
    }

    /**
     * 取消掉所有的订阅
     */
    private void disposedAll() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        compositeDisposable.clear();
    }

    /**
     * 获取缓存
     */
    protected ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onLifecycleDestory(){
        Lifecycle lifecycle = LifecycleUtils.getLifecycle(mContext);
        if (lifecycle != null) {
            lifecycle.removeObserver(ModelImpl.this);
        }
        onRelease();
    }

    @Override
    public void onRelease() {
        disposedAll();
    }

}
