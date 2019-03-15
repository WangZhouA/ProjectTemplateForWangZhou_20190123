package com.lib.fast.common.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.lib.fast.common.utils.LifecycleUtils;

/**
 * Created by siwei on 2018/3/13.
 */
public abstract class PresenterImpl<V extends IView, M extends IModel> implements IPresenter<V>, LifecycleObserver {

    private V mIView;
    private M mIModel;
    private Handler mHandler;
    private Context mContext;

    public PresenterImpl(Context context) {
        this.mContext = context;
        mIModel = initModel(context);
        mHandler = new Handler();
    }

    public abstract M initModel(Context context);

    @Override
    public void attachView(V view) {
        mIView = view;
        Lifecycle lifecycle = LifecycleUtils.getLifecycle(mIView);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public void detatchView() {
        Lifecycle lifecycle = LifecycleUtils.getLifecycle(mIView);
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
        }
        release();
    }

    private void release() {
        //Presenter层数据释放
        onRelease();
        mIView = null;
        if (mIModel != null) {
            //Model层数据释放
            mIModel.onRelease();
            mIModel = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mContext = null;
        mIModel = null;
    }

    @Override
    public void onRelease() {
    }

    public V getView() {
        return mIView;
    }

    public M getModel() {
        return mIModel;
    }

}
