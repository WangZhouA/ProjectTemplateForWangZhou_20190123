package com.lib.fast.common.widgets.ezlistener;

import android.support.v4.view.ViewPager;

/**
 * ViewPager.OnPageChangeListener的简化版,只需要实现onPageSelected即可
 */
public abstract class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
