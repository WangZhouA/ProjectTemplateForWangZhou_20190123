package com.lib.fast.common.error;


import com.elvishew.xlog.XLog;
import com.lib.fast.common.error.http.HttpError;
import com.lib.fast.common.http.exception.ErrorStatus;
import com.lib.fast.common.http.exception.GatewayTimeoutException;
import com.lib.fast.common.http.exception.ServiceErrorException;

import org.apache.http.HttpException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLException;


/**
 * 项目：智能控制     SmartLock
 */
public class ErrorEngine {

    public static ErrorStatus handleHttpException(Throwable t) {
        XLog.tag("okhttp").d("handleHttpException t:%s", t);
        ErrorStatus apiException = new ErrorStatus();
        if (t instanceof HttpException) {
            apiException.setError(HttpError.HTTP_ERROR);
        } else if (t instanceof JSONException || t instanceof ParseException) {
            apiException.setError(HttpError.PARSE_ERROR);
        } else if (t instanceof ConnectException
                || t instanceof SocketTimeoutException
                || t instanceof GatewayTimeoutException) {
            apiException.setError(HttpError.NETWORD_ERROR);
        } else if (t instanceof UnknownHostException) {
            apiException.setError(HttpError.UNKNOW_HOST_ERROR);
        } else if (t instanceof SSLException) {
            apiException.setError(HttpError.SSL_ERROR);
        } else if(t instanceof ServiceErrorException){
            apiException.setError(HttpError.SERVICE_ERROR);
        }else {
            apiException.setError(HttpError.UNKNOW);
        }
        return apiException;
    }

    public static ErrorStatus handleCustomError(CustomError error){
        return new ErrorStatus(error);
    }

    public static ErrorStatus handleCustomError(int code, String msg){
        return new ErrorStatus(code, msg);
    }

}
