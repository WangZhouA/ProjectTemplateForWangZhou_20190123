package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou;

import android.content.Context;
import android.text.TextUtils;

import com.lib.fast.common.http.BaseResponseListener;
import com.lib.fast.common.http.exception.ErrorStatus;
import com.lib.fast.common.mvp.PresenterImpl;
import com.lib.fast.common.utils.StringUtils;

/**
 * Created by 陈姣姣 on 2018/12/20.
 */
public class TestPerstener extends PresenterImpl<MainActivity,TestModel> {

    public TestPerstener(Context context) {
        super(context);
    }

    @Override
    public TestModel initModel(Context context) {
        return new TestModel(context);
    }

    public boolean getCode(String phone) {

        if (TextUtils.isEmpty(phone) || !StringUtils.isMobileNum(phone)) {
            getView().toast("请输入正确的手机号");
            return false;
        } else {

            getModel().sendCode(phone, new BaseResponseListener<Void>() {
                @Override
                public void onResponse(Void data) {
                    super.onResponse(data);
                    getView().toast("获取成功");

                }

                @Override
                public void onFaild(ErrorStatus e) {
                    super.onFaild(e);
                    getView().toast(e.msg);
                }
            });
        }
        return true;
    }

}
