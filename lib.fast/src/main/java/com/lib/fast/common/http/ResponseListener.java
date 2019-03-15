package com.lib.fast.common.http;

import com.lib.fast.common.http.exception.ErrorStatus;

/**
 * Created by Administrator on 2018/3/19.
 */

public interface ResponseListener<T> {

    void onComplete();

    void onResponse(T data);

    void onFaild(ErrorStatus e);

}
