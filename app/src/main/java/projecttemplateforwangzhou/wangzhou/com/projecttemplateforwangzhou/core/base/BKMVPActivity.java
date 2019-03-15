package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.base;


import com.lib.fast.common.activity.BaseMVPActivity;
import com.lib.fast.common.mvp.PresenterImpl;

import butterknife.ButterKnife;


/**
 * Created by siwei on 2018/3/16.
 */
public abstract class BKMVPActivity<P extends PresenterImpl> extends BaseMVPActivity<P> {


    @Override
    protected void initView() {
        super.initView();
        //修复ButterKnife框架在分model下开发无法注入的bug
        //https://github.com/JakeWharton/butterknife/issues/1127
        ButterKnife.bind(this);
    }



}
