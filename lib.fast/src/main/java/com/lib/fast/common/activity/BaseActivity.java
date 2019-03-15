package com.lib.fast.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;

import com.bugtags.library.Bugtags;
import com.lib.fast.common.R;
import com.lib.fast.common.activity.tool.InputMethodHelper;
import com.lib.fast.common.activity.tool.LoadingProgressHelper;
import com.lib.fast.common.activity.view.NavBar;
import com.lib.fast.common.cache.ACache;
import com.lib.fast.common.event.EventAction;
import com.lib.fast.common.http.exception.ErrorStatus;
import com.lib.fast.common.utils.StatusBarUtil;
import com.lib.fast.common.utils.ToastUtils;
import com.lib.fast.common.widgets.loadingdialog.LoadingStatusDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by siwei on 2015/6/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    private CompositeDisposable mCompositeDisposable;

    protected Context mContext;
    private InputMethodHelper mInputMethodHelper;

    protected NavBar mTitleBar;
    private ViewStub bodyStub;
    private View bodyView;

    //标识是否显示对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        mInputMethodHelper = new InputMethodHelper(this);
        super.setContentView(R.layout.activity_base);
        mTitleBar = findViewById(R.id.title_nb);
        bodyStub = findViewById(R.id.body_vs);
        mCompositeDisposable = new CompositeDisposable();

    }

    @Override
    public void setContentView(int layoutResID) {
        bodyStub.setLayoutResource(layoutResID);
        bodyView = bodyStub.inflate();
        setStatusBarColor(getStatusbarColor());
        initView();
        initData();
        initListener();

    }


    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor(@ColorInt int color) {
        if (enableSetStatusBarColor()) {
            StatusBarUtil.setColor(this, color);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    /**
     * 允许设置状态栏颜色
     */
    protected boolean enableSetStatusBarColor() {
        return false;
    }

    /**
     * 获取状态栏颜色
     */
    protected @ColorInt int getStatusbarColor() {
        return getResources().getColor(R.color.default_status_bar_color);
    }

    protected void hiddenTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    protected View getBodyStub() {
        return bodyView;
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    protected void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }


    protected void openActivity(Class<? extends Activity> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void openActivityForResult(Class<? extends Activity> pClass, int requestCode) {
        openActivityForResult(pClass, null, requestCode);
    }

    protected void openActivityForResult(Class<? extends Activity> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public NavBar getTitleBar() {
        return mTitleBar;
    }

    /**
     * 退出Activity
     */
    public void back() {
        finish();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    /**
     * 获取缓存
     */
    public ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    /**
     * 关闭软键盘
     */
    protected void closeSoftInput() {
        mInputMethodHelper.closeSoftInput(this);
    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput(EditText et) {
        mInputMethodHelper.showSoftInput(et);
    }

    /**
     * 注册EventBus,覆盖onMessageEvent()方法去接收
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    /**
     * event bus事件接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventAction event) {
        /* Do something */
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancelToast();
        if (mLoadingHelper != null) {
            mLoadingHelper.destory();
        }
        dismissProgressDialog();
        mInputMethodHelper.fixInputMethodManagerLeak(this);
        mInputMethodHelper = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
        //取消订阅关系
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
        mCompositeDisposable = null;
    }

    /**
     * 发送event
     */
    protected void send(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * loading帮助类
     */
    private LoadingProgressHelper mLoadingHelper;


    /**
     * 显示loading
     */
    protected LoadingProgressHelper showLoading() {
        dismissProgressDialog();
        LoadingProgressHelper loadingProgressHelper = new LoadingProgressHelper(this);
        return showProgress(loadingProgressHelper);
    }

    //显示弹窗
    private @Nullable
    LoadingProgressHelper showProgress(LoadingProgressHelper progressHelper) {
        dismissProgressDialog();
        if (progressHelper != null) {
            mLoadingHelper = progressHelper;
            mLoadingHelper.showProgress();
        }
        return progressHelper;
    }


    /**
     * 创建自定义的弹窗,创建后之前的弹窗会自动消失
     */
    protected LoadingProgressHelper showCustomLoading(LoadingStatusDialog.LoadingStatus defaultStatus) {
        dismissProgressDialog();
        LoadingProgressHelper loadingProgressHelper = new LoadingProgressHelper(this, defaultStatus);
        return showProgress(loadingProgressHelper);
    }

    /**
     * 显示自定义提醒信息的loading
     */
    protected LoadingProgressHelper showCustomLoading(String msg) {
        LoadingStatusDialog.LoadingStatus status = new LoadingStatusDialog.LoadingStatus(LoadingStatusDialog.LEVEL_LOADING, msg, true);
        return showCustomLoading(status);
    }


    protected void delayDo(int second, final Consumer<? super Long> consumer) {
        Observable.timer(second, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        try {
                            consumer.accept(aLong);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 消失loading
     */
    protected void dismissProgressDialog() {
        if (mLoadingHelper != null) {
            mLoadingHelper.dismissProgress();
        }
    }

    public void toast(String msg) {
        if (mContext != null) {
            ToastUtils.showToast(mContext.getApplicationContext(), msg);
        }
    }

    public void toastErrorMsg(ErrorStatus e) {
        dismissProgressDialog();
        toast(e.msg);
    }

}
