package com.lib.fast.common.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.fast.common.R;
import com.lib.fast.common.utils.StatusBarUtil;
import com.lib.fast.common.utils.UiUtil;
import com.lib.fast.common.widgets.text.CountDownView;

/**
 * created by siwei on 2018/7/10
 */
public class NavTitleBar extends RelativeLayout implements View.OnClickListener {

    private ImageButton leftBtn;
    private TextView leftTv;
    private LinearLayout mRightUserRl;
    private ImageView userIv;
    private TextView userTv;
    private TextView mTitle;
    private View mRootView;
    private View endLine;
    private TextView exitTv;
    private CountDownView countdownCdv;
    private boolean mAutoSetStatusBarColor = false;

    public void setClickListener(NavTitleBar.NavBarOnClickListener clickListener) {
        mClickListener = clickListener;
    }

    private NavTitleBar.NavBarOnClickListener mClickListener;


    public interface NavBarOnClickListener {
        /**左侧返回被点击*/
        void onLeftIconClick(View view);

        /**右侧用户被点击*/
        void onRightUserClick(View view);

        /**标题栏退出被点击*/
        void onExitClick(View view);
    }

    public NavTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public NavTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = LayoutInflater.from(context).inflate(R.layout.nav_title_bar, this, false);
        initView(context, attrs, mRootView);
        addView(mRootView);
    }


    private void initView(Context context, AttributeSet attrs, View rootView) {
        leftBtn = rootView.findViewById(R.id.left_btn);
        leftTv = rootView.findViewById(R.id.left_tv);
        mRightUserRl = rootView.findViewById(R.id.right_user_rl);
        userIv = rootView.findViewById(R.id.user_iv);
        userTv = rootView.findViewById(R.id.user_tv);
        mTitle = rootView.findViewById(R.id.bar_title);
        endLine = rootView.findViewById(R.id.bar_end_line);
        exitTv = rootView.findViewById(R.id.exit_tv);
        countdownCdv = rootView.findViewById(R.id.countdown_cdv);
        exitTv.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        leftTv.setOnClickListener(this);
        mRightUserRl.setOnClickListener(this);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavTitleBar);

            /*左侧Icon*/
            boolean showLeftBtn = typedArray.getBoolean(R.styleable.NavTitleBar_ntShowLeftIcon, true);
            leftBtn.setVisibility(showLeftBtn ? View.VISIBLE : View.INVISIBLE);
            leftTv.setVisibility(showLeftBtn ? VISIBLE : INVISIBLE);
            Drawable leftIcon = typedArray.getDrawable(R.styleable.NavTitleBar_ntLeftIcon);
            if (leftIcon != null) {
                leftBtn.setImageDrawable(leftIcon);
            }
            String leftText = typedArray.getString(R.styleable.NavTitleBar_ntLeftText);
            if(leftText != null){
                leftTv.setText(leftText);
            }
            int leftTextColor = typedArray.getColor(R.styleable.NavTitleBar_ntLeftTextColor, getResources().getColor(R.color.nbt_left_text_color));
            leftTv.setTextColor(leftTextColor);
            float leftTextSize = typedArray.getDimension(R.styleable.NavTitleBar_ntLeftTextSize, getResources().getDimension(R.dimen.nbt_left_text_size));
            leftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);

            /*标题*/
            boolean showTitle = typedArray.getBoolean(R.styleable.NavTitleBar_ntShowTitle, false);
            mTitle.setVisibility(showTitle ? View.VISIBLE : View.GONE);
            int titleColor = typedArray.getColor(R.styleable.NavTitleBar_ntTitleTextColor, getResources().getColor(R.color.nbt_title_text_color));
            mTitle.setTextColor(titleColor);
            float titleSize = typedArray.getDimension(R.styleable.NavTitleBar_ntTitleTextSize, getResources().getDimension(R.dimen.nbt_title_text_szie));
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            String centerTitle = typedArray.getString(R.styleable.NavTitleBar_ntTitleText);
            if (centerTitle != null) {
                mTitle.setText(centerTitle);
            }

            /**右侧用户图标*/
            Drawable userIco = typedArray.getDrawable(R.styleable.NavTitleBar_ntRightIcon);
            if (userIco != null) {
                userIv.setImageDrawable(userIco);
            }

            /**右侧用户文字*/
            String userText = typedArray.getString(R.styleable.NavTitleBar_ntRightText);
            if (userText != null) {
                userTv.setText(userText);
            }
            int userColor = typedArray.getColor(R.styleable.NavTitleBar_ntRightTxtColor, getResources().getColor(R.color.nbt_right_user_text_color));
            userTv.setTextColor(userColor);
            exitTv.setTextColor(userColor);

            /**是否显示右侧用户*/
            boolean isShowUser = typedArray.getBoolean(R.styleable.NavTitleBar_ntShowRightUser, false);
            mRightUserRl.setVisibility(isShowUser ? VISIBLE : INVISIBLE);


            int backGroundColor = typedArray.getColor(R.styleable.NavTitleBar_ntBackgroundColor, Color.WHITE);
            mRootView.setBackgroundColor(backGroundColor);
            mAutoSetStatusBarColor = typedArray.getBoolean(R.styleable.NavTitleBar_ntAutoSetStatusBarColor, false);
            if (mAutoSetStatusBarColor) {
                StatusBarUtil.setColor((Activity) getContext(), backGroundColor);
            }
            typedArray.recycle();
        }
    }

    public View getBackView() {
        return leftBtn;
    }

    public TextView getTitleView() {
        return mTitle;
    }


    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void hidden() {
        mRootView.setVisibility(View.GONE);
    }

    public void noEndLine() {
        endLine.setVisibility(View.GONE);
    }

    public void setTitle(CharSequence title) {
        int visible = TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE;
        UiUtil.setVisibility(mTitle, visible);
        mTitle.setText(title);
    }

    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    public void setTitleSize(float size) {
        if (size > 0) {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void hiddenTitle() {
        mTitle.setVisibility(View.INVISIBLE);
    }

    public void showTitle() {
        mTitle.setVisibility(View.VISIBLE);
    }

    public void setLeftIcon(Drawable leftIcon) {
        leftBtn.setImageDrawable(leftIcon);
    }

    public void setLeftIcon(Bitmap leftIcon) {
        leftBtn.setImageBitmap(leftIcon);
    }

    public void setLeftIcon(int resource) {
        leftBtn.setImageResource(resource);
    }

    public void hiddenLeftIcon() {
        leftBtn.setVisibility(View.INVISIBLE);
    }

    public void showLeftIcon() {
        leftBtn.setVisibility(View.VISIBLE);
    }

    public void setLeftTv(String text) {
        if (text != null) {
            leftTv.setText(text);
        }
    }

    public void setLeftTextColor(int color) {
        leftTv.setTextColor(color);
    }

    public void setLeftTextSize(int px) {
        leftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
    }

    public void showLeftText() {
        leftTv.setVisibility(VISIBLE);
    }

    public void hiddenLeftText() {
        leftTv.setVisibility(GONE);
    }

    public boolean isLeftBackShow() {
        return leftBtn != null && leftBtn.getVisibility() == View.VISIBLE;
    }

    public void setBackGroundColor(int color) {
        mRootView.setBackgroundColor(color);
        if (mAutoSetStatusBarColor) {
            StatusBarUtil.setColor((Activity) getContext(), color);
        }
    }

    public void setBackGroundColor(String color) {
        int backgroundColor = 0;
        try {
            backgroundColor = Color.parseColor(color);
        } catch (Exception e) {
            return;
        }
        if (mAutoSetStatusBarColor) {
            StatusBarUtil.setColor((Activity) getContext(), backgroundColor);
        }
        mRootView.setBackgroundColor(backgroundColor);
    }

    public View getRightUserView(){
        return mRightUserRl;
    }

    public void showRightUser() {
        mRightUserRl.setVisibility(VISIBLE);
    }

    public void hidenRightUser() {
        mRightUserRl.setVisibility(GONE);
    }

    public void showUserIcon(int res) {
        userIv.setImageResource(res);
    }

    public void setUserText(String text) {
        userTv.setText(text);
    }

    public void showRightExit(){
        exitTv.setVisibility(VISIBLE);
    }

    public void hidenRightExit(){
        exitTv.setVisibility(GONE);
    }

    public void showCountDown(int maxSecound, CountDownView.CountDownCallBack callBack){
        if(countdownCdv.getVisibility() == GONE){
            countdownCdv.setVisibility(VISIBLE);
            countdownCdv.startCountDown(maxSecound, callBack);
        }
    }

    public void hidenCountDown(){
        if(countdownCdv.getVisibility() == VISIBLE){
            countdownCdv.stopContDown();
            countdownCdv.setVisibility(GONE);
        }
    }

    /**设置倒计时显示内容*/
    public void setCountDownText(String text){
        if(countdownCdv.getVisibility() == VISIBLE){
            countdownCdv.setText(text);
        }
    }

    /**停止倒计时*/
    public void stopCountDown(){
        if(countdownCdv.getVisibility() == VISIBLE){
            countdownCdv.stopContDown();
        }
    }

    @Override
    public void onClick(View v) {
        if (mClickListener == null) {
            if (v.getVisibility() == View.VISIBLE) {
                //默认navbar未设置点击事件的时候,点左侧为退出
                if (v.getId() == R.id.left_btn || v.getId() == R.id.left_tv) {
                    Activity activity = (Activity) getContext();
                    activity.finish();
                }
            }
            return;
        }
        int vId = v.getId();
        if (vId == R.id.left_btn || vId == R.id.left_tv) {
            mClickListener.onLeftIconClick(v);
        } else if (vId == R.id.right_user_rl) {
            mClickListener.onRightUserClick(v);
        } else if(vId == R.id.exit_tv){
            mClickListener.onExitClick(v);
        }
    }

    public void release(){
        if(countdownCdv != null){
            countdownCdv.release();
            countdownCdv = null;
        }
    }
}
