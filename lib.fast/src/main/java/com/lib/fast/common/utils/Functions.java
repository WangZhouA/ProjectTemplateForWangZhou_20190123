package com.lib.fast.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Functions {

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Flag只有在使用了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
     * 并且没有使用 FLAG_TRANSLUCENT_STATUS的时候才有效，也就是只有在状态栏全透明的时候才有效。
     *
     * @param activity
     * @param bDark
     */
    public static void setStatusBarTextColor(Activity activity, boolean bDark) {
        //6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }


    //timeStample是聊天记录发生的时间
    public static String getTime(long timeStample) {
        //得到现在的时间戳
        long now = System.currentTimeMillis();
        //在java中,int类型的数进行除法运算,只能的整数,正是利用这一点,
        //在下列日期中,只要没过昨天24点,无论相差了1s还是23小时,除法得到的结果都是前一天,
        int day = (int) (now / 1000 / 60 / 60 / 60 - timeStample / 1000 / 60 / 60 / 60);
        switch (day) {
            //如果是0这则说明是今天,显示时间
            case 0:
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                return sdf.format(timeStample);
            //如果是1说明是昨天,显示昨天+时间
            case 1:
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                return "昨天" + sdf1.format(timeStample);
            //如果是1说明是前天,显示前天+时间
            case 2:
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                return "前天" + sdf2.format(timeStample);
            //结果大于2就只显示年月日
            default:
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy:MM:dd");
                return sdf3.format(timeStample);
        }
    }

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 2G,3G,4G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    @SuppressLint("NewApi")
    public static boolean isEmpty(String str) {
        return !(str != null && !str.trim().isEmpty());
    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }


    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View v, int resId) {
        return (T) v.findViewById(resId);
    }

    /**
     * 让ScrollView滚到顶端
     */
    public static void scrollToTop(final ScrollView mScrollView) {
        mScrollView.post(new Runnable() {
            // 让scrollview跳转到顶部，必须放在runnable()方法中
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }

        });
    }

    /**
     * 获取App当前版本号
     *
     * @return
     */
    public static int getAppVersionCode(Context ctx) {

        PackageManager manager = ctx.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取App当前版本名
     *
     * @return
     */
    public static String getAppVersionName(Context ctx) {

        PackageManager manager = ctx.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.2";
    }

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String processName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : lists) {
            if (info.processName.equals(processName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }


    /**
     * MD5加密字符串
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static int px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (px / density + 0.5f);
    }

    /**
     * dip转为px
     */
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 判断sd卡是否可用
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param intent
     * @return
     */
    public static Uri geturi(Context context, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/"
                            + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /**
     * 从相册得到的url转换为SD卡中图片路径
     */
    public static String getPath(Intent data, Context context) {
        if (data == null) {
            return null;
        }
        // Uri uri = data.getData();
        Uri uri = geturi(context, data);
        if (TextUtils.isEmpty(uri.getAuthority())) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) context).managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }


    /**
     * @param
     * @description 裁剪图片
     * @author ldm
     * @time 2016/11/30 15:19
     */
    public static void startPhotoZoom(Activity context, Uri uri, int REQUE_CODE_CROP) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //设置为不返回数据
        intent.putExtra("return-data", false);
        context.startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * @param
     * @description 把Uri转换为文件路径
     * @author ldm
     * @time 2016/11/30 15:22
     */
    public static String uriToFilePath(Activity context, Uri uri) {
        //获取图片数据
        String[] proj = {MediaStore.Images.Media.DATA};
        //查询
        Cursor cursor = context.managedQuery(uri, proj, null, null, null);
        //获得用户选择的图片的索引值
        int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        //返回图片路径
        return cursor.getString(image_index);
    }

    /**
     * 第0位是年,第一位是月,,以此内推
     *
     * @param time
     * @param position
     * @return
     */
    public static int formatTime(String time, int position) {
        int month = 0;
        String[] timeArray = time.split("-");
        month = Integer.parseInt(timeArray[position]);
        return month;
    }

    /**
     * 第0位是年,第一位是月,,以此内推
     *
     * @param position
     * @return
     */
    public static String getTime(String times, int position) {
        String bigTime = "";
        String date = formatTime(times, position) + "";//获取时间
        int timeNumber = formatTime(times, 3);//时间
        /**
         * 0-5傍晚
         * 6-10早上
         * 11-14中午
         * 15-18下午
         * 18-23 晚上
         */
        if (timeNumber <= 0 || timeNumber >= 5) {
            bigTime = date + "号傍晚";
        } else if (timeNumber <= 6 || timeNumber >= 10) {
            bigTime = date + "号早上";
        } else if (timeNumber <= 11 || timeNumber >= 14) {
            bigTime = date + "号中午";
        } else if (timeNumber <= 15 || timeNumber >= 18) {
            bigTime = date + "号下午";
        } else if (timeNumber <= 17 || timeNumber >= 23) {
            bigTime = date + "号晚上";
        }
        return bigTime;
    }

    public static String getOutTradeNo(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(time);//获取当前时间
        String str = format.format(curDate);
        return str;
    }

    /**
     * 获取这个礼拜的开始和结束时间
     *
     * @return
     */
    public static Map<String, String> getDayWeek() {
        Map<String, String> weekMap = new HashMap<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        //当前星期几
        int mWay = c.get(Calendar.DAY_OF_WEEK);
        Log.e("今天礼拜" ,""+ mWay);
        //当前时间戳
        long currentTime = System.currentTimeMillis();
        //一天的时间戳天数___减去礼拜几,,获取礼拜一是几号
        long weekStart = currentTime - ((1000 * 60 * 60 * 24) * (mWay - 1));//礼拜天
        long weekStop = currentTime + ((1000 * 60 * 60 * 24) * (7 - mWay));//礼拜六
        String startTime = getOutTradeNo(weekStart);
        String stopTime = getOutTradeNo(weekStop);
        Log.e("startTime" ,""+ startTime);
        Log.e("stopTime" ,""+ stopTime);
        weekMap.put("startTime", startTime);
        weekMap.put("stopTime", stopTime);
        return weekMap;
    }


    /**
     * 获取这个月的起始时间
     *
     * @return
     */
    public static String getMonth() {
        String month = "";
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        month = String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1); // 获取当前年份
        return month;
    }

    /**
     * 获取年份
     *
     * @return
     */
    public static String getYear() {
        String year = "";
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        return year;
    }


    public static String intToDateString(int value) {
        int hour = value / 3600;
        int min = value / 60 % 60;
        int second = value % 60;
        String hourString;
        String minString;
        String secondString;
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = min + "";
        }
        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = second + "";
        }
        return hourString + ":" + minString + ":" + secondString;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    // 返回单位是米___只是计算直线距离

    /**
     * @param longitude1 开始的
     * @param latitude1
     * @param longitude2 结束的
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
