package com.fairhand.supernotepad.recording.presenter;

import com.fairhand.supernotepad.recording.model.IRecordModel;
import com.fairhand.supernotepad.recording.model.RecordModelImpl;
import com.fairhand.supernotepad.recording.service.RecordingService;
import com.fairhand.supernotepad.recording.view.IRecordView;

/**
 * @author FairHand
 * @date 2018/11/5
 */
public class RecordPresenterImpl
        implements IRecordPresenter, RecordModelImpl.OnSaveCallBack, RecordModelImpl.OnDragCallBack {
    
    private IRecordModel model;
    private IRecordView view;
    
    public RecordPresenterImpl(IRecordView view) {
        this.view = view;
        model = new RecordModelImpl();
    }
    
    @Override
    public void record(boolean isStart) {
        view.handleRecord(isStart);
    }
    
    @Override
    public void preSave() {
        // 准备保存，view层显示保存提示对话框
        view.showSaveDialog();
    }
    
    @Override
    public void save(String noteTitle) {
        // 开始保存，model层执行保存逻辑
        model.save(this, RecordingService.mFilePath, noteTitle);
    }
    
    @Override
    public void drag() {
        model.drag(this, RecordingService.mFilePath);
    }
    
    @Override
    public void saveSuccess() {
        view.saveSuccess();
    }
    
    @Override
    public void saveFailed() {
        view.saveFailed();
    }
    
    @Override
    public void dragSuccess() {
        view.dragSuccess();
    }
    
    @Override
    public void dragFailed() {
        view.dragFailed();
    }
    
    @Override
    public void fileNonentity() {
        view.canNotFindFile();
    }
    
}
