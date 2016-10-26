/*
 * Copyright 2014-2024 Helome. All rights reserved. 
 */
package com.yzm.sleep.utils;

import android.util.Log;

public class LogUtil {

    /**
     * 控制需要打的log
     * 
     * 6 全部显示
     * 
     * 0不显示
     */
    private static int LogGrade = 6;

    /**
     * 打印info日志
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (LogGrade > 1) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印debug日志
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (LogGrade > 2) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印warn日志
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (LogGrade > 3) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印error日志
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (LogGrade > 4) {
            Log.e(tag, msg);
        }
    }

    /**
     * 打印verbose日志
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (LogGrade > 5) {
            Log.v(tag, msg);
        }
    }
}
