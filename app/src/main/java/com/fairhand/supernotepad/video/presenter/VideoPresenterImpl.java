package com.fairhand.supernotepad.video.presenter;

import com.fairhand.supernotepad.video.model.IVideoModel;
import com.fairhand.supernotepad.video.model.VideoModelImpl;
import com.fairhand.supernotepad.video.view.IVideoView;
import com.fairhand.supernotepad.video.view.VideoNoteActivity;

/**
 * @author FairHand
 * @date 2018/11/6
 */
public class VideoPresenterImpl implements IVideoPresenter, VideoModelImpl.OnSaveCallBack {
    
    private IVideoView view;
    private IVideoModel model;
    
    public VideoPresenterImpl(IVideoView view) {
        this.view = view;
        model = new VideoModelImpl();
    }
    
    @Override
    public void success() {
        view.saveSuccess();
    }
    
    @Override
    public void failed() {
        view.saveFailed();
    }
    
    @Override
    public void titleEmpty() {
        view.titleEmpty();
    }
    
    @Override
    public void preShooting() {
        view.startShooting();
    }
    
    @Override
    public void addComplete() {
        view.shootComplete();
    }
    
    @Override
    public void save(String noteTitle) {
        model.save(this, VideoNoteActivity.videoPath, noteTitle);
    }
    
    @Override
    public void onDestroy() {
        view = null;
    }
    
}
