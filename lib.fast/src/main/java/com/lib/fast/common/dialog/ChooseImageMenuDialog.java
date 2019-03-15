package com.lib.fast.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.lib.fast.common.R;


/**
 * 编辑头像菜单弹窗
 */
public class ChooseImageMenuDialog extends MenuDialog implements View.OnClickListener {

    /**
     * 点击取消
     */
    public static final int WHICH_CANCLE = 1;
    /**
     * 拍照
     */
    public static final int WHICH_TAKE_PHOTO = 2;
    /**
     * 选择头像
     */
    public static final int WHICH_SELECT_PHOTO = 3;

    TextView takePhotoTv;

    TextView selectPhotoTv;

    TextView cancleTv;

    public ChooseImageMenuDialog(@NonNull Context context) {
        super(context);
    }

    public ChooseImageMenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChooseImageMenuDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.choose_image_menu_dialog);
    }


    @Override
    protected void initView(View view) {
        takePhotoTv = view.findViewById(R.id.take_photo_tv);
        selectPhotoTv = view.findViewById(R.id.select_photo_tv);
        cancleTv = view.findViewById(R.id.cancle_tv);
        takePhotoTv.setOnClickListener(this);
        selectPhotoTv.setOnClickListener(this);
        cancleTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
        int i = v.getId();
        if (i == R.id.cancle_tv) {
            onWhichOneClick(WHICH_CANCLE);
        } else if (i == R.id.take_photo_tv) {
            onWhichOneClick(WHICH_TAKE_PHOTO);
        } else if (i == R.id.select_photo_tv) {
            onWhichOneClick(WHICH_SELECT_PHOTO);
        }
    }

}
