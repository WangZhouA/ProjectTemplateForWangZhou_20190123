package com.lib.fast.common.xlog;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.flattener.Flattener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by siwei on 2018/9/6
 */
public class TimeFlatter implements Flattener {

    private SimpleDateFormat timeFormatter;
    private Date mDate;

    public TimeFlatter(){
        String format = "yyyy-MM-dd HH:mm:ss.SS";
        timeFormatter = new SimpleDateFormat(format);
        mDate = new Date();
    }

    @Override
    public CharSequence flatten(int logLevel, String tag, String message) {
        mDate.setTime(System.currentTimeMillis());
        return timeFormatter.format(mDate)+ '|' + LogLevel.getShortLevelName(logLevel)
                + '|' + tag
                + '|' + message;
    }
}
