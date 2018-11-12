package com.fairhand.supernotepad.video.model;

/**
 * @author FairHand
 * @date 2018/11/6
 */
public interface IVideoModel {
    
    /**
     * 保存录屏文件
     *
     * @param onSaveCallBack 保存结果回调接口
     * @param filePath       保存文件的路径
     * @param noteTitle      记事标题
     */
    void save(VideoModelImpl.OnSaveCallBack onSaveCallBack, String filePath, String noteTitle);
    
}
