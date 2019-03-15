package com.lib.fast.common.http.exception;

import java.io.IOException;

/**
 * created by siwei on 2018/5/23
 */
public class ServiceErrorException extends IOException {

    public ServiceErrorException(){
        super("response code:415, the Service interface exception");
    }
}
