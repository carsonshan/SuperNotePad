package com.fairhand.supernotepad.util;

import android.util.Log;

import static com.fairhand.supernotepad.app.Config.IS_DEBUG;

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
    
    private static final String TAG = "测试";
    
    /**
     * 默认测试TAG,打印重要数据,info级别
     */
    public static void i(String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印调试信息,debug级别
     */
    public static void d(String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印错误信息,error级别
     */
    public static void e(String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印意义最小的数据,verbose级别
     */
    public static void v(String msg) {
        if (IS_DEBUG) {
            Log.v(TAG, msg);
        }
    }
    
    /**
     * 默认测试TAG,打印警告信息,warn级别
     */
    public static void w(String msg) {
        if (IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }
    
    /**
     * 自定义TAG,打印重要数据,info级别
     */
    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印调试信息,debug级别
     */
    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印错误信息,error级别
     */
    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印意义最小的信息,verbose级别
     */
    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            Log.v(tag, msg);
        }
    }
    
    /**
     * 自定义TAG,打印警告信息,warn级别
     */
    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            Log.w(tag, msg);
        }
    }
    
}
