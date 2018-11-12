package com.fairhand.supernotepad.video.view;

/**
 * @author FairHand
 * @date 2018/11/6
 */
public interface IVideoView {
    
    /**
     * 保存成功
     */
    void saveSuccess();
    
    /**
     * 保存失败
     */
    void saveFailed();
    
    /**
     * 保存标题为空
     */
    void titleEmpty();
    
    /**
     * 开始录制
     */
    void startShooting();
    
    /**
     * 录制完成
     */
    void shootComplete();
    
}
