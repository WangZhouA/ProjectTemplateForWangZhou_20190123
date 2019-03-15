package com.lib.fast.common.widgets.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/3/19.
 */

@SuppressLint("AppCompatCustomView")
public class CountDownView extends TextView {

    private CountDownCallBack mCallback;

    private boolean hasStartCountDown;

    private int currentDown;

    private Handler mHandler = new Handler();

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setClickable(true);
    }

    /**
     * 开始倒计时
     *
     * @param maxSecound 最大倒计时时间,单位:秒
     * @param callBack   倒计时回调
     */
    public void startCountDown(int maxSecound, CountDownCallBack callBack) {
        if (hasStartCountDown) {
            mHandler.removeCallbacksAndMessages(null);
            hasStartCountDown = false;
        }
        mCallback = callBack;
        setEnabled(false);
        hasStartCountDown = true;
        currentDown = maxSecound;
        if (mCallback != null) {
            mCallback.onCountDown(currentDown);
        }
        mHandler.post(countDown);
    }

    Runnable countDown = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(countDown);
            if (currentDown > 0) {//继续倒计时
                if (mCallback != null) {
                    mCallback.onCountDown(currentDown);
                }
                mHandler.postDelayed(countDown, 1000);
            } else {//倒计时完成
                hasStartCountDown = false;
                setEnabled(true);
                if (mCallback != null) {
                    mCallback.onCountComplate();
                }
            }
            currentDown = currentDown - 1;
        }
    };

    /**
     * 停止倒计时
     */
    public void stopContDown() {
        mHandler.removeCallbacks(countDown);
        if (mCallback != null) {
            mCallback.onCountCancled();
        }
    }

    public interface CountDownCallBack {

        /**
         * 正在倒计时
         */
        void onCountDown(int second);

        /**
         * 倒计时完成
         */
        void onCountComplate();

        /**
         * 倒计时被取消
         */
        void onCountCancled();
    }

    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mCallback = null;
    }
}
