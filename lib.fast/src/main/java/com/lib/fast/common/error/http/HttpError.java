package com.lib.fast.common.error.http;

import com.lib.fast.common.R;
import com.lib.fast.common.activity.BaseApplication;
import com.lib.fast.common.error.CustomError;

/**
 * Http 自定义异常
 * code 1000-1100
 */
public enum HttpError implements CustomError {

    /**
     * 未知错误
     */
    UNKNOW(1000, R.string.UNKNOW),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, R.string.PARSE_ERROR),

    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, R.string.NETWORD_ERROR),

    /**
     * 协议出错
     */
    HTTP_ERROR(1003, R.string.HTTP_ERROR),

    /**
     * 证书出错
     */
    SSL_ERROR(1005, R.string.SSL_ERROR),

    /***/
    UNKNOW_HOST_ERROR(1008, R.string.UNKNOW_HOST_ERROR),

    /***/
    SERVICE_ERROR(1009, R.string.SERVICE_ERROR);

    private String msg;
    private int code;

    HttpError(int code, int strRes) {
        this.code = code;
        this.msg = BaseApplication.getInstance().getResources().getString(strRes);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
