package com.lib.fast.common.http;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by siwei on 2018/5/23
 */
public class RetorfitServices {

    private static Map<Class, Object> serviceGroup = new HashMap<>();

    /**
     * 获取接口服务(提供接口缓存,避免重复创建接口服务)
     */
    public synchronized static <T> T getService(Class<T> service) {
        if (!serviceGroup.containsKey(service)) {
            Object serviceObject = HttpFactory.instance().createApiService(service);
            serviceGroup.put(service, serviceObject);
        }
        return (T) serviceGroup.get(service);
    }

    /**执行retorfit框架下的请求*/
    public static <T> void excuteRetorfitRequest(Observable<T> observable, Observer<T> observer){
        if(observable == null)return;
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
