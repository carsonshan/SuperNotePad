package com.fairhand.supernotepad.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.fairhand.supernotepad.entity.Note;
import com.fairhand.supernotepad.util.CacheUtil;
import com.mob.MobSDK;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
     * 记事类型(事项)
     */
    public static final int TYPE_AFFAIR = 2;
    /**
     * 记事类型(照片)
     */
    public static final int TYPE_PICTURE = 3;
    /**
     * 记事类型(录音)
     */
    public static final int TYPE_RECORDING = 4;
    /**
     * 记事类型(拼图)
     */
    public static final int TYPE_PUZZLE = 5;
    /**
     * 记事类型(摄像)
     */
    public static final int TYPE_VIDEO = 6;
    
    /**
     * 保存的所有记事
     */
    public static ArrayList<Note> notes = new ArrayList<>();
    
    /**
     * 用户账号
     */
    public static String userAccount;
    
    /**
     * 判断是否已登录
     */
    public static boolean isLogin;
    
    /**
     * 47.107.98.198 阿里公网
     */
    private static final String IP = "47.107.98.198:80";
    
    /**
     * BASE_URL
     */
    public static final String BASE_URL = "http://" + IP + "/imooo/";
    
    /**
     * 手动创建一个线程池<br/>
     * corePoolSize:核心线程数<br/>
     * maximumPoolSize:最大线程数<br/>
     * keepAliveTime:非核心线程最多存活事件<br/>
     * unit:keepAliveTime的单位<br/>
     * workQueue:线程池的任务队列<br/>
     * threadFactory:给线程起个名字<br/>
     * handler:抛异常
     */
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            10,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private final AtomicInteger mCount = new AtomicInteger(1);
                
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
                }
            },
            new ThreadPoolExecutor.AbortPolicy());
    
    /**
     * SharedPreferences保存是否已登录的KEY
     */
    public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    /**
     * 保存是否已登录的SharedPreferences
     */
    public static final String SAVE_IS_LOGIN = "SAVE_IS_LOGIN";
    
    /**
     * 判断是否是查看保存的记录
     */
    public static final String KEY_FROM_READ = "KEY_FROM_READ";
    
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
    
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化MobSDK
        MobSDK.init(this);
        
        // 初始化Realm数据库框架
        Realm.init(this);
        // DIY配置Realm
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                                                   // 指定数据库的名字
                                                   .name("superNote.realm")
                                                   .build();
        // 设置DIY配置为默认的配置
        Realm.setDefaultConfiguration(configuration);
        
        initFile();
        
        // 获取用户是否已登录
        isLogin = CacheUtil.isLoginYet(this);
        // 获取用户名
        userAccount = CacheUtil.getUser(this);
        // 获取密码
        mSecret = CacheUtil.getPassword(this);
        // 获取当前记事本
        currentPad = CacheUtil.getCurrentPad(this);
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
     * 初始化保存文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initFile() {
        // null:根目录
        File rootFile = new File(getExternalFilesDir(null), "SUPER_NOTE");
        handPaint = new File(rootFile, "handPaint");
        recording = new File(rootFile, "record");
        videoThumb = new File(rootFile, "videoThumb");
        if (!rootFile.exists()) {
            // 不存在，创建
            rootFile.mkdir();
            handPaint.mkdir();
            recording.mkdir();
            videoThumb.mkdir();
        }
    }
    
}
