package com.lib.fast.common.http;

import com.lib.fast.common.http.exception.ErrorStatus;

/**
 * Created by Administrator on 2018/3/19.
 */

public abstract class BaseResponseListener<T> implements ResponseListener<T>{

    @Override
    public void onComplete() {
    }

    @Override
    public void onResponse(T data) {
    }

    @Override
    public void onFaild(ErrorStatus e) {
    }
}
