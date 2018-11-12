package com.fairhand.supernotepad.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fairhand.supernotepad.app.Config;

/**
 * 缓存工具类
 *
 * @author FairHand
 * @date 2018/9/22
 */
public class CacheUtil {
    
    /**
     * 获取是否已登录
     */
    public static boolean isLoginYet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Config.SAVE_IS_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.KEY_IS_LOGIN, false);
    }
    
    /**
     * 设置是否已登录
     */
    public static void putLoginYet(Context context, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                Config.SAVE_IS_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Config.KEY_IS_LOGIN, value).apply();
    }
    
    /**
     * 获取用户名
     */
    public static String getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "SAVE_USER", Context.MODE_PRIVATE);
        return sharedPreferences.getString("KEY_USER", null);
    }
    
    /**
     * 设置用户名
     */
    public static void putUser(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "SAVE_USER", Context.MODE_PRIVATE).edit();
        editor.putString("KEY_USER", value).apply();
    }
    
    /**
     * 获取密码
     */
    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "SAVE_PASSWORD", Context.MODE_PRIVATE);
        return sharedPreferences.getString("KEY_PASSWORD", null);
    }
    
    /**
     * 设置密码
     */
    public static void putPassword(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "SAVE_PASSWORD", Context.MODE_PRIVATE).edit();
        editor.putString("KEY_PASSWORD", value).apply();
    }
    
    /**
     * 获取当前记事本
     */
    public static String getCurrentPad(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "SAVE_CURRENT_PAD", Context.MODE_PRIVATE);
        return sharedPreferences.getString("KEY_CURRENT_PAD", Config.DEFAULT_PAD);
    }
    
    /**
     * 设置当前记事本
     */
    public static void putCurrentPad(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "SAVE_CURRENT_PAD", Context.MODE_PRIVATE).edit();
        editor.putString("KEY_CURRENT_PAD", value).apply();
    }
    
}
