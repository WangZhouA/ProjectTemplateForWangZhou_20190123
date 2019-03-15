package com.lib.fast.common.http.exception;

import java.io.IOException;

/**
 * created by siwei on 2018/5/23
 */
public class UnKnowException extends IOException {

    public UnKnowException(int responseCode){
        super("response code:"+responseCode+", Undefined or unknown exception");
    }
}
