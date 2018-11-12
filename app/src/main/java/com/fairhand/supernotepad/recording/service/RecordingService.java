package com.fairhand.supernotepad.recording.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fairhand.supernotepad.app.Config;

import java.io.IOException;
import java.util.UUID;

/**
 * 录音服务类
 *
 * @author FairHand
 * @date 2018/9/7
 */
public class RecordingService extends Service {
    
    private static final String TAG = RecordingService.class.getSimpleName();
    
    /**
     * 录音文件地址
     */
    public static String mFilePath;
    
    private MediaRecorder mRecorder;
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mFilePath = Config.getRecording().getAbsolutePath() + UUID.randomUUID();
    }
    
    /**
     * 服务创建的时候调用
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        // START_STICKY：表示Service运行的进程被Android系统强制杀掉之后
        // Android系统会将该Service依然设置为started状态
        // onStartCommand方法虽然会执行但是获取不到intent信息
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }
    
    /**
     * 开始录音
     */
    public void startRecording() {
        // 创建一个MediaRecorder来录音
        mRecorder = new MediaRecorder();
        // 设置音频源(MediaRecorder.AudioSource.MIC 设定录音来源为主麦克风)
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 控制视频输出的格式(MPEG_4 指定录制的文件为mpeg-4格式，可以保护Audio和Video)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置输出文件的路径
        mRecorder.setOutputFile(mFilePath);
        // 设置Audio的编码格式(AAC 高级音频编码)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置录制的音频通道数
        mRecorder.setAudioChannels(1);
        // 设置录制的音频采样率
        mRecorder.setAudioSamplingRate(44100);
        // 设置录制的音频编码比特率
        mRecorder.setAudioEncodingBitRate(192000);
        try {
            mRecorder.prepare();// 使MediaRecorder进入准备状态
            mRecorder.start();// 开始录音
        } catch (IOException e) {
            Log.e(TAG, "录音工具准备失败");
        }
    }
    
    /**
     * 停止录音
     */
    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();// 停止MediaRecorder
            mRecorder.release();// 释放MediaRecorder
        }
    }
    
}
