package com.fairhand.supernotepad.util;

import android.util.Log;

/**
 * Log统一管理类
 *
 * @author FairHand
 * @date 2018/10/19
 */
public class Logger {
    
    private Logger() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    public static boolean isDebug = true;
    private static final String TAG = "测试";
    
    /**
     * 默认测试TAG,打印重要数据,info级别
     */
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印调试信息,debug级别
     */
    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印错误信息,error级别
     */
    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印意义最小的数据,verbose级别
     */
    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印警告信息,warn级别
     */
    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }
    
    /**
     * 自定义TAG,打印重要数据,info级别
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印调试信息,debug级别
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印错误信息,error级别
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印意义最小的信息,verbose级别
     */
    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印警告信息,warn级别
     */
    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }
    
}
