package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.model;

import android.text.TextUtils;

import com.lib.fast.common.cache.SharePerferenceHelper;

/**
 * Created by 账户的数据层操作类 on 2018/12/28.
 */
public class AccountModel {

    public static AccountModel INSTANCE;

    private static final String SP_NAME = "account_sp_name";
    /**
     * 手机号的sp key
     */
    private static final String SP_KEY_PHONE_NUM = "sp_key_phone_num";

    private SharePerferenceHelper sharePerferenceHelper;

    public static AccountModel getInstance() {
        if (INSTANCE == null) {
            synchronized (AccountModel.class) {
                INSTANCE = new AccountModel();
            }
        }
        return INSTANCE;
    }

    private AccountModel() {
        sharePerferenceHelper = SharePerferenceHelper.createSharePerference(SP_NAME);
    }

    /**
     * 获取缓存手机号
     */
    public String getCachePhoneNum() {
        return sharePerferenceHelper.getString(SP_KEY_PHONE_NUM, "");
    }

    /**
     * 缓存手机号
     */
    public void cachePhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) return;
        sharePerferenceHelper.putString(SP_KEY_PHONE_NUM, phoneNum);
    }
}
