package com.fairhand.supernotepad.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author FairHand
 * @date 2018/11/3
 */
public class TimeUtil {
    
    /**
     * 获取格式化时间
     */
    public static String getFormatTime() {
        long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date(now);
        return format.format(date);
    }
    
}
