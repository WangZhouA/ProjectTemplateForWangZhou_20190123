package com.lib.fast.common.dialog;

/**
 *〈dialog弹窗监听〉
 *
 * @version [版本号, 2017/12/26]
 *
 * @author Huyongqing
 *
 * @since [产品/模块版本]
 */

public interface DialogListener {

    void onComplete();

    void onFail();

    void onCompleteDate(String ed);

}
