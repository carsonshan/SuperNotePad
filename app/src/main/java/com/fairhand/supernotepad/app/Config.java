package com.fairhand.supernotepad.app;

import android.app.Application;

import com.fairhand.supernotepad.http.entity.User;
import com.fairhand.supernotepad.util.CacheUtil;
import com.mob.MobSDK;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Application类<br/>
 * 配置参数类
 *
 * @author FairHand
 * @date 2018/10/31
 */
public class Config extends Application {
    
    public static User user;
    
    public static final boolean IS_DEBUG = true;
    
    /**
     * 私密密码
     */
    public static String mSecret;
    
    /**
     * 记事本(默认、私密)
     */
    public static final String DEFAULT_PAD = "DEFAULT_PAD";
    public static final String SECRET_PAD = "SECRET_PAD";
    
    /**
     * 当前所处记事本(默认为默认记事本)
     */
    public static String currentPad = DEFAULT_PAD;
    
    /**
     * 记事类型(普通)
     */
    public static final int TYPE_COMMON = 0;
    /**
     * 记事类型(手绘)
     */
    public static final int TYPE_HAND_PAINT = 1;
    /**
     * 记事类型(照片)
     */
    public static final int TYPE_PICTURE = 2;
    /**
     * 记事类型(录音)
     */
    public static final int TYPE_RECORDING = 3;
    /**
     * 记事类型(拼图)
     */
    public static final int TYPE_AFFIX = 4;
    /**
     * 记事类型(摄像)
     */
    public static final int TYPE_VIDEO = 5;
    
    /**
     * 判断是否为游客登陆
     */
    public static boolean isTourist;
    
    /**
     * 判断是否第一次使用
     */
    public static boolean isFirstUse = true;
    
    // private static final String ALI_IP = "47.107.98.198:80";
    
    /**
     * 本地服务器IP
     */
    private static final String MY_TEST_IP = "192.168.1.102:8080";
    
    /**
     * BASE_URL
     */
    public static final String BASE_URL = "http://" + MY_TEST_IP + "/";
    
    /**
     * 手绘记事保存文件夹
     */
    private static File handPaint;
    
    /**
     * 录音记事保存文件夹
     */
    private static File recording;
    
    /**
     * 摄像记事缩略图保存文件夹
     */
    private static File videoThumb;
    
    /**
     * 拼图保存文件夹
     */
    private static File photoAffix;
    
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化MobSDK
        MobSDK.init(this);
        // 初始化Realm数据库框架
        Realm.init(this);
        // DIY配置Realm
        RealmConfiguration diyConfiguration = new RealmConfiguration.Builder()
                                                      // 指定数据库的名字
                                                      .name("superNote.realm")
                                                      // 版本号（数据库有更新时往上增加）
                                                      .schemaVersion(3)
                                                      .migration(new Migration())
                                                      .build();
        // 设置DIY配置为默认的配置
        Realm.setDefaultConfiguration(diyConfiguration);
        Realm realm = Realm.getDefaultInstance();
        
        initFile();
        
        // 获取当前用户
        user = realm.where(User.class).findFirst();
        // 获取当前是否为游客登录
        isTourist = CacheUtil.isTouristYet(this);
        // 获取私密密码
        mSecret = CacheUtil.getPassword(this);
        // 获取当前记事本
        currentPad = CacheUtil.getCurrentPad(this);
        isFirstUse = CacheUtil.getFirstUse(this);
    }
    
    /**
     * 获取手绘保存文件夹
     */
    public static File getHandPaint() {
        return handPaint;
    }
    
    /**
     * 获取录音保存文件夹
     */
    public static File getRecording() {
        return recording;
    }
    
    /**
     * 获取视频缩略图保存文件夹
     */
    public static File getVideoThumb() {
        return videoThumb;
    }
    
    /**
     * 获取拼图保存文件夹
     */
    public static File getPhotoAffix() {
        return photoAffix;
    }
    
    /**
     * 初始化保存文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initFile() {
        // null:根目录
        File rootFile = new File(getExternalFilesDir(null), "SUPER_NOTE");
        handPaint = new File(rootFile, "handPaint");
        recording = new File(rootFile, "record");
        videoThumb = new File(rootFile, "videoThumb");
        photoAffix = new File(rootFile, "photoAffix");
        if (!rootFile.exists()) {
            // 不存在，创建
            rootFile.mkdir();
            handPaint.mkdir();
            recording.mkdir();
            videoThumb.mkdir();
            photoAffix.mkdir();
        }
    }
    
}
