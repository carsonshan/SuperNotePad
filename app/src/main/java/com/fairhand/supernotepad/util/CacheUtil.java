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
     * 获取是否游客登陆
     */
    public static boolean isTouristYet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "SAVE_IS_LOGIN", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("KEY_IS_LOGIN", false);
    }
    
    /**
     * 设置游客登陆
     */
    public static void putTouristYet(Context context, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "SAVE_IS_LOGIN", Context.MODE_PRIVATE).edit();
        editor.putBoolean("KEY_IS_LOGIN", value).apply();
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
    
    public static boolean getFirstUse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "SAVE_FIRST_USE", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("KEY_FIRST_USE", true);
    }
    
    public static void putFirstUse(Context context, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "SAVE_FIRST_USE", Context.MODE_PRIVATE).edit();
        editor.putBoolean("KEY_FIRST_USE", value).apply();
    }
    
}
