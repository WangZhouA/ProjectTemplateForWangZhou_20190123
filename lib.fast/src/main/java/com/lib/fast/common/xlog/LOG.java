package com.lib.fast.common.xlog;

import android.content.Context;
import android.text.TextUtils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.lib.fast.common.utils.FileUtils;


/**
 * created by siwei on 2018/5/18
 * 日志操作类
 */
public class LOG {

    private static final String DEFAULT_XLOG_DIR_NAME = "Fast-XLog";

    private static final String LOG_TAG_DEFAULT = "Xlog";

    /**
     * okhttp框架的log输出tag
     */
    private static final String LOG_TAG_OKHTTP = "okhttp";

    /**
     * okhttp日志输出
     */
    public static Logger OkHttp;

    /**
     * 初始化XLog
     *
     * @param context
     * @param isDebug    是否是debug模式
     * @param defaultTag 默认TAG
     */
    public static void initXlog(Context context, boolean isDebug, String defaultTag) {
        initXlog(context, isDebug, defaultTag, DEFAULT_XLOG_DIR_NAME);
    }

    /**
     * 初始化XLog
     *
     * @param context
     * @param isDebug           是否是debug模式
     * @param defaultTag        默认TAG
     * @param defaultLogDirName 默认日志文件的目录名
     */
    public static void initXlog(Context context, boolean isDebug, String defaultTag, String defaultLogDirName) {
        if (TextUtils.isEmpty(defaultLogDirName)) defaultLogDirName = DEFAULT_XLOG_DIR_NAME;
        if (TextUtils.isEmpty(defaultTag)) defaultTag = LOG_TAG_DEFAULT;
        LogConfiguration configuration = new LogConfiguration.Builder()
                .logLevel(isDebug ? LogLevel.ALL : LogLevel.NONE)
                .tag(defaultTag).build();
        Printer androidPrinter = new AndroidPrinter();
        Printer filePrinter = new FilePrinter// 打印日志到文件的打印器
                .Builder(FileUtils.getLoggerDir(context, defaultLogDirName).getPath()) // 指定保存日志文件的路径
                .logFlattener(new TimeFlatter())
                .build();
        XLog.init(configuration, androidPrinter, filePrinter);
        initTagLog();
    }

    private static void initTagLog() {
        OkHttp = XLog.tag(LOG_TAG_OKHTTP).build();
    }

}
