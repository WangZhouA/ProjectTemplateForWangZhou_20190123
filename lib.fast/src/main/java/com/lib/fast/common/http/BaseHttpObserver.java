package com.lib.fast.common.http;

import com.lib.fast.common.error.CustomError;
import com.lib.fast.common.http.exception.ErrorStatus;
import com.lib.fast.common.error.ErrorEngine;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/19.
 */

public abstract class BaseHttpObserver<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "OkHttp_BaseHttpObserval ";

    private ResponseListener<T> mResponseListener;
    private CompositeDisposable compositeDisposable;
    private Disposable disposable;

    public BaseHttpObserver(ResponseListener<T> responseListener){
        this.mResponseListener = responseListener;
    }

    public BaseHttpObserver(CompositeDisposable compositeDisposable, ResponseListener<T> responseListener){
        this.mResponseListener = responseListener;
        this.compositeDisposable = compositeDisposable;
    }

    public ResponseListener<T> getResponseListener() {
        return mResponseListener;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if(compositeDisposable != null){
            compositeDisposable.add(d);
        }
    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        onResponse(tBaseResponse);
        //Next执行完成则任务任务被执行完成了
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if(mResponseListener != null){
            final ErrorStatus apiException = ErrorEngine.handleHttpException(e);
            e.printStackTrace();
            Observable.empty().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Object o) {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    mResponseListener.onFaild(apiException);
                }
            });

        }
        unSubscrible();
    }

    @Override
    public void onComplete() {
        unSubscrible();
    }

    protected void dispatchListenerResponse(T data){
        if(mResponseListener != null){
            mResponseListener.onResponse(data);
        }
    }

    protected void dispatchListenerFaild(CustomError e){
        dispatchListenerFaild(ErrorEngine.handleCustomError(e));
    }

    protected void dispatchListenerFaild(ErrorStatus e){
        if(mResponseListener != null){
            mResponseListener.onFaild(e);
        }
    }

    public abstract void onResponse(BaseResponse<T> response);

    /**解除订阅*/
    protected void unSubscrible(){
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
            if(compositeDisposable != null){
                compositeDisposable.remove(disposable);
            }
        }
        if(mResponseListener != null){
            mResponseListener.onComplete();
        }
    }


}
