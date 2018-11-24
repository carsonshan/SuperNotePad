package com.fairhand.supernotepad.puzzle.affix;

/**
 * 图片保存的回调接口
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 7:13 PM
 */
public interface PhotoSaveCallback {
    
    /**
     * 保存成功
     */
    void onSuccess();
    
    /**
     * 保存失败
     */
    void onFailed();
    
}
