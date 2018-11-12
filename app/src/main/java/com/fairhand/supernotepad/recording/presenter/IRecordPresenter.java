package com.fairhand.supernotepad.recording.presenter;

/**
 * Presenter层接口---控制Model层的数据操作及调用View层的UI操作来完成“中间人”工作
 *
 * @author FairHand
 * @date 2018/11/5
 */
public interface IRecordPresenter {
    
    /**
     * 录音
     *
     * @param isStart 判断开始停止录音
     */
    void record(boolean isStart);
    
    /**
     * 准备保存(先对话框提示)
     */
    void preSave();
    
    /**
     * 保存录音文件
     *
     * @param noteTitle 记事标题
     */
    void save(String noteTitle);
    
    /**
     * 丢弃文件
     */
    void drag();
    
}
