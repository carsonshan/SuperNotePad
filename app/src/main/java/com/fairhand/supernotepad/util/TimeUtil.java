package com.fairhand.supernotepad.util;

import java.text.ParseException;
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date(now);
        return format.format(date);
    }
    
    public static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        Date d = new Date(time);
        return sf.format(d);
    }
    
    /**
     * 获取事件时间距离今天的天数
     *
     * @param affairDate  事件时间
     * @param currentDate 现在的时间
     * @return 天数
     */
    public static long getDays(String affairDate, String currentDate) {
        String tempAffair = affairDate.split(" ")[0];
        String tempCurrent = currentDate.split(" ")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        try {
            Date d1 = format.parse(tempCurrent);
            Date d2 = format.parse(tempAffair);
            long day = d2.getTime() - d1.getTime();
            return day / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}
