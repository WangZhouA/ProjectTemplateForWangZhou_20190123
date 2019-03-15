package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou;

import android.content.Context;

import com.lib.fast.common.http.BaseHttpObserver;
import com.lib.fast.common.http.BaseResponse;
import com.lib.fast.common.http.BaseResponseListener;
import com.lib.fast.common.http.exception.ErrorStatus;
import com.lib.fast.common.mvp.ModelImpl;

import projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.http.service.HttpServiceApi;

;

/**
 * Created by 陈姣姣 on 2018/12/20.
 */
public class TestModel extends ModelImpl {
    public TestModel(Context context) {
        super(context);
    }


    public void sendCode(String phone, BaseResponseListener<Void> listener) {
        excuteRetorfitRequest(HttpServiceApi.instance().sendCode(phone), new BaseHttpObserver<Void>(getCompositeDisposable(), listener) {
            @Override
            public void onResponse(BaseResponse<Void> response) {
                if (response.isSuccess()) {
                    dispatchListenerResponse(response.getData());
                } else {
                    dispatchListenerFaild(new ErrorStatus(response.getCode(), response.getMessage()));
                }
            }
        });

    }
}
