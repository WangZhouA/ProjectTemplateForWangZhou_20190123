package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.base;

import android.view.View;

import com.lib.fast.common.activity.BaseMVPFragment;
import com.lib.fast.common.mvp.PresenterImpl;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/16.
 */

public abstract class BKMVPFragment<P extends PresenterImpl> extends BaseMVPFragment<P> {

    private Unbinder mUnBinder;

    @Override
    protected void initView(View view) {
        //修复ButterKnife框架在分model下开发无法注入的bug
        //https://github.com/JakeWharton/butterknife/issues/1127
        mUnBinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnBinder != null){
            mUnBinder.unbind();
        }
    }
}
