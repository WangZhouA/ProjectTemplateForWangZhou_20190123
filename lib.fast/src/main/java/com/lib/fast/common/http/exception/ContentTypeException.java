package com.lib.fast.common.http.exception;

import java.io.IOException;

/**
 * created by siwei on 2018/5/23
 */
public class ContentTypeException extends IOException {

    public ContentTypeException(){
        super("response code:415, the http head Content-Type error");
    }
}
