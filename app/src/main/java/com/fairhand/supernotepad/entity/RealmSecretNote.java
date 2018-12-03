package com.fairhand.supernotepad.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * @author Phanton
 * @date 2018/11/11 - 星期日 - 17:52
 */
public class RealmSecretNote extends RealmObject {
    
    private int kind;
    
    private String key;
    
    /**
     * 记事标题<br/>
     * // @Required——表示该字段非空
     */
    @Required
    private String noteTitle;
    
    /**
     * 记事内容
     * // @Required——表示该字段非空
     */
    @Required
    private String noteContent;
    
    /**
     * 记事时间
     */
    @Required
    private String noteTime;
    
    /**
     * 照片
     */
    @SuppressWarnings("unused")
    private RealmList<String> pictureIds;
    
    /**
     * 录音文件地址
     */
    private String recordingPath;
    
    /**
     * 摄像文件地址
     */
    private String videoPath;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getVideoPath() {
        return videoPath;
    }
    
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    
    public String getRecordingPath() {
        return recordingPath;
    }
    
    public void setRecordingPath(String recordingPath) {
        this.recordingPath = recordingPath;
    }
    
    public String getNoteTitle() {
        return noteTitle;
    }
    
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    
    public String getNoteContent() {
        return noteContent;
    }
    
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
    
    public int getKind() {
        return kind;
    }
    
    public void setKind(int kind) {
        this.kind = kind;
    }
    
    public String getNoteTime() {
        return noteTime;
    }
    
    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
    
    public RealmList<String> getPictureIds() {
        return pictureIds;
    }
    
}
