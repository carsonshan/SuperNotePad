package com.fairhand.supernotepad.recording.model;

/**
 * Model层接口---实现该接口的类负责实际的获取数据操作，如数据库读取、网络加载
 *
 * @author FairHand
 * @date 2018/11/5
 */
public interface IRecordModel {
    
    /**
     * 保存录音文件
     *
     * @param onSaveCallBack 保存结果回调接口
     * @param filePath       保存文件路径
     * @param noteTitle      记事标题
     */
    void save(RecordModelImpl.OnSaveCallBack onSaveCallBack, String filePath, String noteTitle);
    
    
    /**
     * 丢弃录音文件
     *
     * @param onDragCallBack 删除结果回调接口
     * @param filePath       文件路径
     */
    void drag(RecordModelImpl.OnDragCallBack onDragCallBack, String filePath);
    
}
