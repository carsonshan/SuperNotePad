package com.fairhand.supernotepad.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 记事实体类
 *
 * @author FairHand
 * @date 2018/11/3
 */
public class Note implements Serializable {
    
    private String key;
    
    /**
     * 记事图片ID
     */
    private int noteImageId;
    /**
     * 记事图片路径
     */
    private String noteImagePath;
    /**
     * 标题
     */
    private String noteTitle;
    /**
     * 时间
     */
    private String noteTime;
    /**
     * 内容
     */
    private String noteContent;
    /**
     * 记事的类型
     */
    private int kind;
    /**
     * 照片
     */
    private List<String> pictureIds;
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
    
    public int getNoteImageId() {
        return noteImageId;
    }
    
    public void setNoteImageId(int noteImageId) {
        this.noteImageId = noteImageId;
    }
    
    public String getNoteImagePath() {
        return noteImagePath;
    }
    
    public void setNoteImagePath(String noteImagePath) {
        this.noteImagePath = noteImagePath;
    }
    
    public String getNoteTitle() {
        return noteTitle;
    }
    
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    
    public String getNoteTime() {
        return noteTime;
    }
    
    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
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
    
    public List<String> getPictureIds() {
        return pictureIds;
    }
    
    public void setPictureIds(List<String> pictureIds) {
        this.pictureIds = pictureIds;
    }
    
}
