package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.app;

import com.bugtags.library.Bugtags;
import com.lib.fast.common.activity.BaseApplication;
import com.lib.fast.common.config.IBuildConfig;

/**
 * created by siwei on 2018/11/3
 */
public class SmartKeyCabinetClientApplication extends BaseApplication {


    @Override
    public IBuildConfig getBuildConfig() {
         return new SmartKeyCabinetClientBuildConfig();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //在这里初始化
        // 这里是要改变的，每次重新添加新的时候记得换成新的
        Bugtags.start("4a79363bddeb044d4d02c99f07ac4d43", this, Bugtags.BTGInvocationEventNone);
    }
}
