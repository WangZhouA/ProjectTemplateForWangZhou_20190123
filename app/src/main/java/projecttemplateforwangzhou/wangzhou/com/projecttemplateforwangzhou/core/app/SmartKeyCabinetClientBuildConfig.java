package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.app;

import android.content.Context;

import com.lib.fast.common.BuildConfig;
import com.lib.fast.common.config.IBuildConfig;
import com.lib.fast.common.utils.StringUtils;

import projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.R;


/**
 * created by siwei on 2018/11/3
 */
public class SmartKeyCabinetClientBuildConfig implements IBuildConfig {

    private static final String BASE_DEBUG_URL = "http://39.108.55.73:8075/aisha";

    private static final String BASE_RELEASE_URL = "http://39.108.55.73:8075/aisha";

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getHttpBaseUrl() {
        return isDebug() ? BASE_DEBUG_URL : BASE_RELEASE_URL;
    }

    @Override
    public int getVersionCode(Context context) {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public String getVersionName(Context context) {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppName(Context context) {
        return StringUtils.getStringByResource(R.string.app_name);
    }
}
