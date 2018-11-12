package com.fairhand.supernotepad.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author FairHand
 * @date 2018/10/20
 */
public class RegularExpressionUtil {
    
    private RegularExpressionUtil() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }
    
    /**
     * 验证手机号（简单）
     */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    
    /**
     * 验证手机号（精确）
     * <p>
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * <p>联通：130、131、132、145、155、156、175、176、185、186
     * <p>电信：133、153、173、177、180、181、189
     * <p>全球星：1349
     * <p>虚拟运营商：170
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-8])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    
    /**
     * 验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx/
     */
    private static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    
    /**
     * 验证邮箱
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    
    /**
     * 验证url
     */
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    
    /**
     * 验证汉字
     */
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    
    /**
     * 验证IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    
    /**
     * 判断是否为手机号（简单）
     *
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(REGEX_MOBILE_SIMPLE, string);
    }
    
    /**
     * 判断是否为手机号（精确）
     *
     * @param string 待验证文本
     * @return 是否符合手机号（精确）格式
     */
    public static boolean isMobileExact(String string) {
        return isMatch(REGEX_MOBILE_EXACT, string);
    }
    
    /**
     * 判断是否为座机号码
     *
     * @param string 待验证文本
     * @return 是否符合座机号码格式
     */
    public static boolean isTel(String string) {
        return isMatch(REGEX_TEL, string);
    }
    
    /**
     * 判断是否为邮箱
     *
     * @param string 待验证文本
     * @return 是否符合邮箱格式
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL, string);
    }
    
    /**
     * 判断是否为网址
     *
     * @param string 待验证文本
     * @return 是否符合网址格式
     */
    public static boolean isURL(String string) {
        return isMatch(REGEX_URL, string);
    }
    
    /**
     * 判断是否为汉字
     *
     * @param string 待验证文本
     * @return 是否符合汉字
     */
    public static boolean isHanzi(String string) {
        return isMatch(REGEX_CHZ, string);
    }
    
    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }
    
}
