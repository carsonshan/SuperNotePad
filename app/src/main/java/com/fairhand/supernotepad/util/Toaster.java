package com.fairhand.supernotepad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 *
 * @author FairHand
 * @date 2018/10/19
 */
@SuppressLint("ShowToast")
public class Toaster {
    
    private Toaster() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public static boolean isShow = true;
    
    private static Toast toast;
    
    /**
     * 短时间显示Toast
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
    /**
     * 短时间显示Toast
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
    /**
     * 长时间显示Toast
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
    /**
     * 长时间显示Toast
     */
    public static void showLong(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
    /**
     * 自定义显示Toast时间
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, duration);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
    /**
     * 自定义显示Toast时间
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, duration);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }
    
}
