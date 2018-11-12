package com.fairhand.supernotepad.recording.view;

/**
 * View层接口---执行各种UI操作，定义的方法主要给Presenter调用
 *
 * @author FairHand
 * @date 2018/11/5
 */
public interface IRecordView {
    
    /**
     * 处理录音
     *
     * @param isStart 判断是开始还是停止
     */
    void handleRecord(boolean isStart);
    
    /**
     * 显示保存对话框
     */
    void showSaveDialog();
    
    /**
     * 保存成功
     */
    void saveSuccess();
    
    /**
     * 保存失败
     */
    void saveFailed();
    
    /**
     * 删除成功
     */
    void dragSuccess();
    
    /**
     * 删除失败
     */
    void dragFailed();
    
    /**
     * 找不到文件
     */
    void canNotFindFile();
    
}
