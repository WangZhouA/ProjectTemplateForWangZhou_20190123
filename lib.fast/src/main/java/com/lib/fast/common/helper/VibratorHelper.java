package com.lib.fast.common.helper;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by siwei on 2018/6/30<br/>
 * uses-permission android:name="android.permission.VIBRATE"
 * 震动帮助类
 */
public class VibratorHelper {

    private Vibrator mVibrator;
    private int mRepeat;
    private long[] mTimes;

    private VibratorHelper(Context context, long[] times, int repeat) {
        mVibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        mTimes = times;
        mRepeat = repeat;
    }

    public static class Builder {
        private static final long DEFAULT_WAIT_TIME = 0;
        private static final long DEFAULT_VIBRATOR_TIME = 0;

        private List<Long> mVibratorMillsList;
        private int mRepeat = 1;

        public Builder() {
            mVibratorMillsList = new ArrayList<>();
        }

        /**
         * 重置
         */
        public void reset() {
            mVibratorMillsList.clear();
        }

        /**
         * 添加等待
         */
        public Builder addWait(long mills) {
            if (mVibratorMillsList.size() % 2 != 1) {
                //未设置震动
                addVibrator(DEFAULT_VIBRATOR_TIME);
            }
            mVibratorMillsList.add(mills);
            return this;
        }

        /**
         * 添加震动
         */
        public Builder addVibrator(long mills) {
            if (mVibratorMillsList.size() % 2 != 0) {
                //未设置等待
                addWait(DEFAULT_WAIT_TIME);
            }
            mVibratorMillsList.add(mills);
            return this;
        }

        /**
         * 设置重复类型 -1为无限循环
         */
        public Builder setRepeat(int repeat) {
            mRepeat = repeat;
            return this;
        }

        public VibratorHelper build(Context context) {
            long[] times = new long[mVibratorMillsList.size()];
            for (int i = 0; i < mVibratorMillsList.size(); i++) {
                times[i] = mVibratorMillsList.get(i);
            }
            return new VibratorHelper(context, times, mRepeat);
        }
    }

    /**
     * 是否支持震动
     */
    public boolean hasVibrator() {
        return mVibrator.hasVibrator();
    }

    /**
     * 取消震动
     */
    public void cancle() {
        if (mVibrator.hasVibrator()) {
            mVibrator.cancel();
        }
    }

    /**
     * 开始震动
     */
    public void vibrator() {
        if (hasVibrator()) {
            cancle();
            mVibrator.vibrate(mTimes, 0);
        }
    }

    private static Map<Integer, Builder> sVibratorHelperMap = new HashMap<>();

    public static final int TYPE_R_VW_VW_VW = 1;
    public static final int TYPE_R_VVW = 2;
    public static final int TYPE_VW = 3;

    static {
        sVibratorHelperMap.put(TYPE_R_VW_VW_VW, new Builder().addVibrator(500).addWait(200).setRepeat(-1));
        sVibratorHelperMap.put(TYPE_R_VVW, new Builder().addVibrator(300).addWait(100).addVibrator(300).addWait(300).setRepeat(-1));
        sVibratorHelperMap.put(TYPE_VW, new Builder().addVibrator(2000).addWait(500).addVibrator(1000).addWait(10));
    }


    @IntDef({TYPE_R_VW_VW_VW, TYPE_R_VVW, TYPE_VW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VibratorType {
    }

    /**
     * 按照预先预定的类型获取震动
     */
    public static VibratorHelper createVibrator(Context context, @VibratorType int type) {
        return sVibratorHelperMap.get(type).build(context);
    }

    /**
     * 按照预先预定的类型获取震动
     */
    public static void vibrate(Context context, long mills) {
        if (context != null) {
            Vibrator vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(Math.max(100, mills));
        }
    }

}
