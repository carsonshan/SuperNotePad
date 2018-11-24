package com.fairhand.supernotepad.puzzle.affix;

/**
 * 选择照片滤镜类型回调接口
 *
 * @author Phanton
 * @date 11/22/2018 - Thursday - 9:26 PM
 */
public interface PickFilterKindCallBack {
    
    /**
     * 隐藏滤镜
     */
    void onCancel();
    
    /**
     * 原始
     */
    void onPickOriginPhoto();
    
    /**
     * 黑白
     */
    void onPickGrayFilter();
    
    /**
     * 卡通
     */
    void onPickCartoonFilter();
    
    /**
     * 怀旧
     */
    void onPickNostalgiaFilter();
    
    /**
     * 素描
     */
    void onPickSketchFilter();
    
}
