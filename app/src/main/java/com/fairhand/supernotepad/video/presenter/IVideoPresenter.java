package com.fairhand.supernotepad.video.presenter;

/**
 * @author FairHand
 * @date 2018/11/6
 */
public interface IVideoPresenter {
    
    /**
     * 准备开始录制
     */
    void preShooting();
    
    /**
     * 录制完成
     */
    void addComplete();
    
    /**
     * 保存录制文件
     *
     * @param noteTitle 记事标题
     */
    void save(String noteTitle);
    
    /**
     * 销毁
     */
    void onDestroy();
    
}
