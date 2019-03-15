package com.lib.fast.common.http.interceptor;

import com.lib.fast.common.http.exception.ContentTypeException;
import com.lib.fast.common.http.exception.GatewayTimeoutException;
import com.lib.fast.common.http.exception.ServiceErrorException;
import com.lib.fast.common.http.exception.UnKnowException;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http 返回的错误码拦截器
 */
public class HttpCodeInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        checkCode(response.code());
        return response;
    }

    private void checkCode(int code) throws IOException {
        if (code == 404) {//找不到服务
            throw new UnknownHostException();
        } else if (code == 504) {//缓存读取过期
            throw new GatewayTimeoutException();
        } else if (code == 415) {//Content-Type不正确
            throw new ContentTypeException();
        } else if (code != 200) {//未定义或未知异常
            throw new UnKnowException(code);
        }
    }
}
