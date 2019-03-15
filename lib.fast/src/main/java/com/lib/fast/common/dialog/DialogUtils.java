package com.lib.fast.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.fast.common.R;


/**
 * 〈Dialog工具类〉
 *
 * @author Huyongqing
 * @version [版本号, 2017/12/26]
 * @since [产品/模块版本]
 */

public class DialogUtils {

    /**
     * 显示提示框
     *
     * @param context
     */
    public static void showDialog(final Context context, int content, int btn_name) {
        //判断如果activity已经关闭，不运行dialog
        if (((Activity) context).isFinishing())
            return;
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_text);
        //提示内容
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
        //确定按钮
        TextView btn_sure = (TextView) dialog.findViewById(R.id.btn_sure);
        //设置按钮名称
        btn_sure.setText(btn_name);
        dialog.setCanceledOnTouchOutside(false);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public static void showDialogEditText(final Context context, int content,final DialogListener listener,int typeFlag) {
        //判断如果activity已经关闭，不运行dialog
        if (((Activity) context).isFinishing())
            return;
        final Dialog dialog = new Dialog(context, R.style.MyDialog);

        dialog.setContentView(R.layout.item_dialog_exit);
//            //提示内容
        TextView tv_content = (TextView) dialog.findViewById(R.id.for_tv_s);
       final EditText ed = dialog.findViewById(R.id.for_ed_s);
        tv_content.setText(content);
//            //确定按钮
        if (typeFlag==1) {
            ed.setInputType(EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        Button btn_sure =  dialog.findViewById(R.id.btn_no_o);
        Button btn_No =  dialog.findViewById(R.id.btn_yes_o);
        dialog.setCanceledOnTouchOutside(false);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ed.getText().toString().trim())){
                    Toast.makeText(context, "请填写输入信息", Toast.LENGTH_SHORT).show();
                }else {
                    listener.onCompleteDate(ed.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });
        btn_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }










    /**
     * 显示提示框点击确定触发的事件
     *
     * @param context
     * @param ok_btn_text 确认按钮文字
     * @param titleStr    标题      不需要标题，可以传入（NUll）或者 （空双引号“”）
     * @param str         文字提示
     */
    public static void showIsOkDialog(final Context context, String ok_btn_text, String close_btn_text, String titleStr, String str, final DialogListener listener) {
        //判断如果activity已经关闭，不运行dialog
        if (((Activity) context).isFinishing())
            return;
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_is_confirm);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView text = (TextView) dialog.findViewById(R.id.text);
        Button close_btn = (Button) dialog.findViewById(R.id.close_btn);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        if (TextUtils.isEmpty(titleStr)) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleStr);
        }
        if (TextUtils.isEmpty(str)) {
            text.setVisibility(View.GONE);
        } else {
            text.setText(str);
        }
        ok_btn.setText(ok_btn_text);
        close_btn.setText(close_btn_text);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onComplete();
                dialog.cancel();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onFail();
                dialog.cancel();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void showIsOkDialog(Context context, String ok, String cancel, String message, DialogListener listener) {
        showIsOkDialog(context, ok, cancel, message, "", listener);
    }

    /**
     * 显示提示框点击确定触发的事件
     *
     * @param context
     */
    public static Dialog showProgressDialog(Context context) {
        if (((Activity) context).isFinishing())
            return null;
        Dialog dialog = new Dialog(context, R.style.progress_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 状态提醒弹窗
     *
     * @param context
     * @param tip_img 提醒图片
     * @param tips    提醒文字
     * @return
     */
    public static void showStateAlertDialog(Context context, int tip_img, int tips) {
        if (((Activity) context).isFinishing())
            return;
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_state_alert, null);
        dialog.setContentView(view);
        //提示图片
        ImageView iv_tip_img = (ImageView) view.findViewById(R.id.iv_tip_img);
        //设置显示提示图片
        iv_tip_img.setBackgroundResource(tip_img);
        //提示文字
        TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        //设置显示提示文字
        tv_tip.setText(tips);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


}
