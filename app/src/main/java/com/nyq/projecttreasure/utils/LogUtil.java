package com.nyq.projecttreasure.utils;

import android.util.Log;

import com.nyq.projecttreasure.application.App;

/**
 * Created by jerry_lee on 2015/4/17.
 */
public class LogUtil {

    private LogUtil() {
        //私有化构造方法隐藏对象
    }

    public static void i(String tag, String message) {

        if (App.isDebug()) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (App.isDebug()) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (App.isDebug()) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (App.isDebug()) {
            Log.e(tag, message);
        }

    }

    public static void v(String tag, String message) {
        if (App.isDebug()) {
            Log.v(tag, message);
        }
    }
    public static void info(String tag, Exception e) {
        if (App.isDebug()) {
            Log.i(tag, e.getMessage());
        }
    }
}